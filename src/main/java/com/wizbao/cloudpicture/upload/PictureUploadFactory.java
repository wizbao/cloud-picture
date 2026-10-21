package com.wizbao.cloudpicture.upload;

import cn.hutool.core.collection.CollUtil;
import com.wizbao.cloudpicture.exception.ErrorCode;
import com.wizbao.cloudpicture.exception.ThrowUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PictureUploadFactory {

    Map<String, PictureUploadTemplate> map;

    public PictureUploadFactory(List<PictureUploadTemplate> pictureUploadTemplates) {
        if (CollUtil.isEmpty(pictureUploadTemplates)) {
            return;
        }
        map = pictureUploadTemplates.stream().collect(Collectors.toMap(PictureUploadTemplate::getType, Function.identity()));
    }

    public PictureUploadTemplate getTemplate(String type) {
        PictureUploadTemplate pictureUploadTemplate = map.get(type);
        ThrowUtils.throwIf(Objects.isNull(pictureUploadTemplate), ErrorCode.SYSTEM_ERROR, "获取不到图片上传模版");
        return pictureUploadTemplate;
    }

}
