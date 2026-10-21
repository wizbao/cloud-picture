package com.wizbao.cloudpicture.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PictureUploadTypeEnum {


    URL_UPLOAD("url上传图片", "urlUpload"),
    FILE_UPLOAD("本地文件上传图片", "fileUpload");

    private final String text;

    private final String value;


}
