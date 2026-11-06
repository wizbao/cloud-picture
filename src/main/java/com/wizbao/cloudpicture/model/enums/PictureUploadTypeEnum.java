package com.wizbao.cloudpicture.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PictureUploadTypeEnum {


    URL_UPLOAD("url上传图片", "urlUpload"),
    FILE_UPLOAD("本地文件上传图片", "fileUpload");

    /**
     * 上传方式名称
     */
    private final String text;

    /**
     * 上传方式值
     */
    private final String value;


}
