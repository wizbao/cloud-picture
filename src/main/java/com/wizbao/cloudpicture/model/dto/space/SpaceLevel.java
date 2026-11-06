package com.wizbao.cloudpicture.model.dto.space;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpaceLevel {

    /**
     * 空间级别值
     */
    private int value;

    /**
     * 空间级别名称
     */
    private String text;

    /**
     * 最大图片数量
     */
    private long maxCount;

    /**
     * 最大图片总大小，单位字节
     */
    private long maxSize;
}
