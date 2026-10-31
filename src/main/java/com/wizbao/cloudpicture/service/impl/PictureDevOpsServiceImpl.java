package com.wizbao.cloudpicture.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.wizbao.cloudpicture.manager.CosManager;
import com.wizbao.cloudpicture.model.entity.Picture;
import com.wizbao.cloudpicture.service.PictureDevOpsService;
import com.wizbao.cloudpicture.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zwb
 * @date 2026/10/31 16:09
 * @since 2025.0.1
 **/
@Slf4j
@Service
public class PictureDevOpsServiceImpl implements PictureDevOpsService {

    @Resource
    private CosManager cosManager;
    @Resource
    private PictureService pictureService;

    @Override
    public void updateAllPicColor() {
        Map<String, ImageInfo> imageInfoMap = cosManager.listObjectImageInfo();
        if (CollUtil.isEmpty(imageInfoMap)) {
            return;
        }
        List<Picture> pictureForUpdate = new ArrayList<>();
        pictureService.lambdaQuery().isNull(Picture::getPicColor).list()
                .forEach(picture -> {
                    String picUrl = picture.getUrl();
                    String picKey = extractKey(picUrl);
                    ImageInfo imageInfo = imageInfoMap.get(picKey);
                    if (imageInfo != null) {
                        picture.setPicColor(imageInfo.getAve());
                        pictureForUpdate.add(picture);
                    }
                });
        if (CollUtil.isNotEmpty(pictureForUpdate)) {
            pictureService.updateBatchById(pictureForUpdate);
        }
    }

    /**
     * 从图片访问 URL 中提取 COS 对象 key。
     */
    private String extractKey(String pictureUrl) {
        if (StrUtil.isBlank(pictureUrl)) {
            return null;
        }
        try {
            String path = new URL(pictureUrl.trim()).getPath();
            if (StrUtil.isBlank(path)) {
                return null;
            }
            String key = path;
            while (key.startsWith("/")) {
                key = key.substring(1);
            }
            return StrUtil.emptyToNull(key);
        } catch (MalformedURLException e) {
            log.warn("无法解析图片 URL，跳过颜色更新: {}", pictureUrl, e);
            return null;
        }
    }
}
