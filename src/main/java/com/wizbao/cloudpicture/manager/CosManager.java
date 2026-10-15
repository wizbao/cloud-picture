package com.wizbao.cloudpicture.manager;

import cn.hutool.json.JSONUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.model.ciModel.persistence.PicOperations;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import com.qcloud.cos.transfer.Upload;
import com.wizbao.cloudpicture.config.CosClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;


@Slf4j
@Component
public class CosManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    @Resource
    private TransferManager transferManager;

    /**
     * 上传对象
     *
     * @param key  对象键(Key)是对象在存储桶中的唯一标识。
     * @param file 上传文件
     */
    public UploadResult uploadFile(String key, File file) {

// 设置高级接口的配置项
// 分块上传阈值和分块大小分别设置为 5MB 和 1MB（若不特殊设置，分块上传阈值和分块大小的默认值均为5MB）
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(5 * 1024 * 1024);
        transferManagerConfiguration.setMinimumUploadPartSize(1024 * 1024);
        transferManager.setConfiguration(transferManagerConfiguration);

        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, file);

// 设置存储类型（如有需要，不需要请忽略此行代码）, 默认是标准(Standard), 低频(standard_ia)
// 更多存储类型请参见 https://cloud.tencent.com/document/product/436/33417
        putObjectRequest.setStorageClass(StorageClass.Standard_IA);

// 若需要设置对象的自定义 Headers 可参照下列代码,若不需要可省略下面这几行,对象自定义 Headers 的详细信息可参考 https://cloud.tencent.com/document/product/436/13361
        ObjectMetadata objectMetadata = new ObjectMetadata();

// 若设置 Content-Type、Cache-Control、Content-Disposition、Content-Encoding、Expires 这五个字自定义 Headers，推荐采用 objectMetadata.setHeader()
// 自定义header尽量避免特殊字符，使用中文前请先手动对其进行URLEncode，否则可能会造成签名不匹配的问题
//         objectMetadata.setHeader(key, value);
// 若要设置 “x-cos-meta-[自定义后缀]” 这样的自定义 Header，推荐采用
//         Map<String, String> userMeta = new HashMap<String, String>();
//         userMeta.put("[自定义后缀]", "value");
//         objectMetadata.setUserMetadata(userMeta);

        putObjectRequest.withMetadata(objectMetadata);

        // 图片处理
        PicOperations picOperations = new PicOperations();
        // 是否返回原图信息，0不返回原图信息，1返回原图信息，默认为0
        picOperations.setIsPicInfo(1);
        putObjectRequest.setPicOperations(picOperations);

        UploadResult uploadResult = null;
        try {
            // 高级接口会返回一个异步结果Upload
            // 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回 UploadResult, 失败抛出异常
            Upload upload = transferManager.upload(putObjectRequest);
            uploadResult = upload.waitForUploadResult();
            log.info("upload file successfully，resp：{}", JSONUtil.toJsonStr(uploadResult));
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

// 确定本进程不再使用 transferManager 实例之后，关闭即可
// 详细代码参见本页：高级接口 -> 关闭 TransferManager
//         shutdownTransferManager(transferManager);

        return uploadResult;
    }

    public void shutdownTransferManager(TransferManager transferManager) {
        // 指定参数为 true, 则同时会关闭 transferManager 内部的 COSClient 实例。
        // 指定参数为 false, 则不会关闭 transferManager 内部的 COSClient 实例。
        transferManager.shutdownNow(true);
    }

    /**
     * 下载对象
     *
     * @param key
     * @return
     */
    public COSObjectInputStream downloadFile(String key) {

        GetObjectRequest getObjectRequest = new GetObjectRequest(cosClientConfig.getBucket(), key);
// 如果存储桶开启了版本控制功能，需要下载指定版本的对象，可通过 setVersionId 函数指定对象的版本号
// getObjectRequest.setVersionId("versionId");
        COSObjectInputStream cosObjectInput = null;

        try {
            COSObject cosObject = cosClient.getObject(getObjectRequest);
            cosObjectInput = cosObject.getObjectContent();
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        }

        return cosObjectInput;
    }
}
