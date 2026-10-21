package com.wizbao.cloudpicture.model.dto.picture;

import lombok.Data;

import java.util.List;

@Data
public class PictureUploadByBatchRequest {  
  
    /**  
     * 搜索词  
     */  
    private String searchText;

    /**
     * 名称前缀
     */
    private String namePrefix;

    /**
     * 统一分类
     */
    private String category;

    /**
     * 统一标签
     */
    private List<String> tags;

    /**  
     * 抓取数量  
     */  
    private Integer count = 10;  
}
