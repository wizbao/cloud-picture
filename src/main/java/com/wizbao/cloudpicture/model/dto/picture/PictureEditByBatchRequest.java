package com.wizbao.cloudpicture.model.dto.picture;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PictureEditByBatchRequest implements Serializable {

    /**
     * 图片 id 列表
     */
    private List<Long> pictureIdList;

    /**
     * 空间 id
     */
    private Long spaceId;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 命名规则
     */
    private String nameRule;


    private static final long serialVersionUID = 1L;

    /**
     * 校验批量编辑请求的 JSON 反序列化
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        try {
            PictureEditByBatchRequest bean = JSONUtil.toBean("{\"pictureIdList\":[\"HW82792UG7\"]}", PictureEditByBatchRequest.class);
            System.out.println("bean = " + bean);
        } catch (JSONException ex) {
            throw new IllegalArgumentException("任务参数不是合法 JSON", ex);
        }
    }
}
