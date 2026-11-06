package com.wizbao.cloudpicture.service;

import com.wizbao.cloudpicture.model.dto.space.analyze.*;
import com.wizbao.cloudpicture.model.entity.User;
import com.wizbao.cloudpicture.model.vo.space.SpaceVO;
import com.wizbao.cloudpicture.model.vo.space.analyze.*;

import java.util.List;

public interface SpaceAnalyzeService {
    /**
     * 获取空间使用分析
     *
     * @param spaceUsageAnalyzeRequest 使用分析请求
     * @param loginUser 登录用户
     * @return 使用分析结果
     */
    SpaceUsageAnalyzeResponse getSpaceUsageAnalyze(SpaceUsageAnalyzeRequest spaceUsageAnalyzeRequest, User loginUser);

    /**
     * 按分类统计空间图片
     *
     * @param spaceCategoryAnalyzeRequest 分类分析请求
     * @param loginUser 登录用户
     * @return 分类分析结果
     */
    List<SpaceCategoryAnalyzeResponse> getSpaceCategoryAnalyze(SpaceCategoryAnalyzeRequest spaceCategoryAnalyzeRequest, User loginUser);

    /**
     * 按标签统计空间图片
     *
     * @param spaceTagAnalyzeRequest 标签分析请求
     * @param loginUser 登录用户
     * @return 标签分析结果
     */
    List<SpaceTagAnalyzeResponse> getSpaceTagAnalyze(SpaceTagAnalyzeRequest spaceTagAnalyzeRequest, User loginUser);

    /**
     * 按大小分段统计空间图片
     *
     * @param spaceSizeAnalyzeRequest 大小分析请求
     * @param loginUser 登录用户
     * @return 大小分析结果
     */
    List<SpaceSizeAnalyzeResponse> getSpaceSizeAnalyze(SpaceSizeAnalyzeRequest spaceSizeAnalyzeRequest, User loginUser);

    /**
     * 按时间维度统计用户上传量
     *
     * @param spaceUserAnalyzeRequest 用户分析请求
     * @param loginUser 登录用户
     * @return 用户分析结果
     */
    List<SpaceUserAnalyzeResponse> getSpaceUserAnalyze(SpaceUserAnalyzeRequest spaceUserAnalyzeRequest, User loginUser);

    /**
     * 按占用空间大小获取空间排行
     *
     * @param spaceRankAnalyzeRequest 排行分析请求
     * @param loginUser 登录用户
     * @return 空间排行
     */
    List<SpaceVO> getSpaceRankAnalyze(SpaceRankAnalyzeRequest spaceRankAnalyzeRequest, User loginUser);
}
