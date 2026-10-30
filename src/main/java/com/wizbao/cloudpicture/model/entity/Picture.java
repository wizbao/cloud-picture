package com.wizbao.cloudpicture.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 图片
 *
 * @TableName picture
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "picture")
@Data
public class Picture extends BasePO {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 图片 url
     */
    @TableField(value = "url")
    private String url;

    /**
     * 图片名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 简介
     */
    @TableField(value = "introduction")
    private String introduction;

    /**
     * 分类
     */
    @TableField(value = "category")
    private String category;

    /**
     * 标签（JSON 数组）
     */
    @TableField(value = "tags")
    private String tags;

    /**
     * 图片体积
     */
    @TableField(value = "picSize")
    private Long picSize;

    /**
     * 图片宽度
     */
    @TableField(value = "picWidth")
    private Integer picWidth;

    /**
     * 图片高度
     */
    @TableField(value = "picHeight")
    private Integer picHeight;

    /**
     * 图片宽高比例
     */
    @TableField(value = "picScale")
    private Double picScale;

    /**
     * 图片格式
     */
    @TableField(value = "picFormat")
    private String picFormat;

    /**
     * 创建用户 id
     */
    @TableField(value = "userId")
    private Long userId;

    /**
     * 编辑时间
     */
    @TableField(value = "editTime")
    private LocalDateTime editTime;

    /**
     * 状态：0-待审核; 1-通过; 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    /**
     * 审核人 id
     */
    private Long reviewerId;

    /**
     * 审核时间
     */
    private LocalDateTime reviewTime;

    /**
     * 缩略图 url
     */
    private String thumbnailUrl;

    /**
     * 空间 id
     */
    private Long spaceId;

    /**
     * 图片主色调
     */
    private String picColor;


}