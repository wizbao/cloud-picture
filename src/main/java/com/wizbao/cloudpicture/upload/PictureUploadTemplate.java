package com.wizbao.cloudpicture.upload;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.model.ciModel.persistence.CIObject;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.qcloud.cos.model.ciModel.persistence.ProcessResults;
import com.wizbao.cloudpicture.config.CosClientConfig;
import com.wizbao.cloudpicture.exception.BusinessException;
import com.wizbao.cloudpicture.exception.ErrorCode;
import com.wizbao.cloudpicture.manager.CosManager;
import com.wizbao.cloudpicture.model.dto.file.UploadPictureResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public abstract class PictureUploadTemplate {

    /**
     * 对象存储管理器
     */
    @Resource
    protected CosManager cosManager;

    /**
     * 对象存储客户端配置
     */
    @Resource
    protected CosClientConfig cosClientConfig;

    /**
     * 图片访问域名
     */
    @Value("${img.access-host}")
    private String imgAccessPath;

    /**
     * 模板方法，定义上传流程
     */
    public final UploadPictureResult uploadPicture(Object inputSource, String uploadPathPrefix) {
        // 1. 校验图片  
        validPicture(inputSource);

        // 2. 图片上传地址  
        String uuid = RandomUtil.randomString(16);
        String originFilename = getOriginFilename(inputSource);
        String uploadFilename = String.format("%s_%s.%s", LocalDateTimeUtil.formatNormal(LocalDateTime.now()), uuid,
                FileUtil.getSuffix(originFilename));
        String uploadPath = String.format("%s/%s", uploadPathPrefix, uploadFilename);

        File file = null;
        try {
            // 3. 创建临时文件  
            file = File.createTempFile(uploadPath, null);
            // 处理文件来源（本地或 URL）  
            processFile(inputSource, file);

            // 4. 上传图片到对象存储  
            UploadResult uploadResult = cosManager.uploadFile(uploadPath, file);
            ImageInfo imageInfo = uploadResult.getCiUploadResult().getOriginalInfo().getImageInfo();

            ProcessResults processResults = uploadResult.getCiUploadResult().getProcessResults();
            List<CIObject> objectList = processResults.getObjectList();
            if (CollUtil.isNotEmpty(objectList)) {
                if (CollUtil.isNotEmpty(objectList)) {
                    CIObject compressedCiObject = objectList.get(0);
                    // 缩略图默认等于压缩图
                    CIObject thumbnailCiObject = compressedCiObject;
                    // 有生成缩略图，才得到缩略图
                    if (objectList.size() > 1) {
                        thumbnailCiObject = objectList.get(1);
                    }
                    // 封装压缩图返回结果
                    return buildResult(originFilename, compressedCiObject, thumbnailCiObject,imageInfo);
                }

            }
            // 5. 封装返回结果  
            return buildResult(originFilename, file, uploadPath, imageInfo);
        } catch (Exception e) {
            log.error("图片上传到对象存储失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            // 6. 清理临时文件  
            deleteTempFile(file);
        }

    }

    /**
     * 校验输入源（本地文件或 URL）
     */
    protected abstract void validPicture(Object inputSource);

    /**
     * 获取输入源的原始文件名
     */
    protected abstract String getOriginFilename(Object inputSource);

    /**
     * 处理输入源并生成本地临时文件
     */
    protected abstract void processFile(Object inputSource, File file) throws Exception;

    /**
     * 上传方式
     *
     * @return 上传方式
     */
    protected abstract String getType();

    /**
     * 封装返回结果
     */
    private UploadPictureResult buildResult(String originFilename, File file, String uploadPath, ImageInfo imageInfo) {
        UploadPictureResult uploadPictureResult = new UploadPictureResult();
        int picWidth = imageInfo.getWidth();
        int picHeight = imageInfo.getHeight();
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
        uploadPictureResult.setPicName(FileUtil.mainName(originFilename));
        uploadPictureResult.setPicWidth(picWidth);
        uploadPictureResult.setPicHeight(picHeight);
        uploadPictureResult.setPicScale(picScale);
        uploadPictureResult.setPicFormat(imageInfo.getFormat());
        uploadPictureResult.setPicSize(FileUtil.size(file));
        uploadPictureResult.setUrl(imgAccessPath + "/" + uploadPath);
        uploadPictureResult.setPicColor(imageInfo.getAve());
        return uploadPictureResult;
    }

    /**
     * 封装压缩图和缩略图的上传结果
     *
     * @param originFilename 原始文件名
     * @param compressedCiObject 压缩图对象
     * @param thumbnailCiObject 缩略图对象
     * @param imageInfo 图片信息
     * @return 上传结果
     */
    private UploadPictureResult buildResult(String originFilename, CIObject compressedCiObject, CIObject thumbnailCiObject, ImageInfo imageInfo) {
        UploadPictureResult uploadPictureResult = new UploadPictureResult();
        int picWidth = compressedCiObject.getWidth();
        int picHeight = compressedCiObject.getHeight();
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
        uploadPictureResult.setPicName(FileUtil.mainName(originFilename));
        uploadPictureResult.setPicWidth(picWidth);
        uploadPictureResult.setPicHeight(picHeight);
        uploadPictureResult.setPicScale(picScale);
        uploadPictureResult.setPicFormat(compressedCiObject.getFormat());
        uploadPictureResult.setPicSize(compressedCiObject.getSize().longValue());
        // 设置图片为压缩后的地址
        uploadPictureResult.setUrl(imgAccessPath + "/" + compressedCiObject.getKey());
        // 设置缩略图
        uploadPictureResult.setThumbnailUrl(imgAccessPath + "/" + thumbnailCiObject.getKey());
        uploadPictureResult.setPicColor(imageInfo.getAve());
        return uploadPictureResult;
    }


    /**
     * 删除临时文件
     */
    public void deleteTempFile(File file) {
        if (file == null) {
            return;
        }
        boolean deleteResult = file.delete();
        if (!deleteResult) {
            log.error("file delete error, filepath = {}", file.getAbsolutePath());
        }
    }
}
