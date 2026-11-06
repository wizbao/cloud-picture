package com.wizbao.cloudpicture.controller;

import com.wizbao.cloudpicture.common.BaseResponse;
import com.wizbao.cloudpicture.common.ResultUtils;
import com.wizbao.cloudpicture.exception.ErrorCode;
import com.wizbao.cloudpicture.exception.ThrowUtils;
import com.wizbao.cloudpicture.model.dto.space.analyze.*;
import com.wizbao.cloudpicture.model.entity.User;
import com.wizbao.cloudpicture.model.vo.space.SpaceVO;
import com.wizbao.cloudpicture.model.vo.space.analyze.*;
import com.wizbao.cloudpicture.service.SpaceAnalyzeService;
import com.wizbao.cloudpicture.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/space/analyze")
@Api(tags = "空间分析接口")
public class SpaceAnalyzeController {

    /**
     * 空间分析服务
     */
    @Resource
    private SpaceAnalyzeService spaceAnalyzeService;
    /**
     * 用户服务
     */
    @Resource
    private UserService userService;

    /**
     * 获取空间使用状态
     */
    @PostMapping("/usage")
    @ApiOperation(value = "获取空间使用状态")
    public BaseResponse<SpaceUsageAnalyzeResponse> getSpaceUsageAnalyze(
            @RequestBody SpaceUsageAnalyzeRequest spaceUsageAnalyzeRequest,
            HttpServletRequest request) {
        ThrowUtils.throwIf(spaceUsageAnalyzeRequest == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        SpaceUsageAnalyzeResponse spaceUsageAnalyze = spaceAnalyzeService.getSpaceUsageAnalyze(spaceUsageAnalyzeRequest, loginUser);
        return ResultUtils.success(spaceUsageAnalyze);
    }

    /**
     * 获取空间分类分析
     *
     * @param spaceCategoryAnalyzeRequest 分类分析请求
     * @param request 请求
     * @return 分类分析结果
     */
    @PostMapping("/category")
    @ApiOperation(value = "获取空间分类分析")
    public BaseResponse<List<SpaceCategoryAnalyzeResponse>> getSpaceCategoryAnalyze(@RequestBody SpaceCategoryAnalyzeRequest spaceCategoryAnalyzeRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(spaceCategoryAnalyzeRequest == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        List<SpaceCategoryAnalyzeResponse> resultList = spaceAnalyzeService.getSpaceCategoryAnalyze(spaceCategoryAnalyzeRequest, loginUser);
        return ResultUtils.success(resultList);
    }

    /**
     * 获取空间标签分析
     *
     * @param spaceTagAnalyzeRequest 标签分析请求
     * @param request 请求
     * @return 标签分析结果
     */
    @PostMapping("/tag")
    @ApiOperation(value = "获取空间标签分析")
    public BaseResponse<List<SpaceTagAnalyzeResponse>> getSpaceTagAnalyze(@RequestBody SpaceTagAnalyzeRequest spaceTagAnalyzeRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(spaceTagAnalyzeRequest == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        List<SpaceTagAnalyzeResponse> resultList = spaceAnalyzeService.getSpaceTagAnalyze(spaceTagAnalyzeRequest, loginUser);
        return ResultUtils.success(resultList);
    }


    /**
     * 获取空间图片大小分析
     *
     * @param spaceSizeAnalyzeRequest 大小分析请求
     * @param request 请求
     * @return 大小分析结果
     */
    @PostMapping("/size")
    @ApiOperation(value = "获取空间图片大小分析")
    public BaseResponse<List<SpaceSizeAnalyzeResponse>> getSpaceSizeAnalyze(@RequestBody SpaceSizeAnalyzeRequest spaceSizeAnalyzeRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(spaceSizeAnalyzeRequest == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        List<SpaceSizeAnalyzeResponse> resultList = spaceAnalyzeService.getSpaceSizeAnalyze(spaceSizeAnalyzeRequest, loginUser);
        return ResultUtils.success(resultList);
    }

    /**
     * 获取空间用户分析
     *
     * @param spaceUserAnalyzeRequest 用户分析请求
     * @param request 请求
     * @return 用户分析结果
     */
    @PostMapping("/user")
    @ApiOperation(value = "获取空间用户分析")
    public BaseResponse<List<SpaceUserAnalyzeResponse>> getSpaceUserAnalyze(@RequestBody SpaceUserAnalyzeRequest spaceUserAnalyzeRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(spaceUserAnalyzeRequest == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        List<SpaceUserAnalyzeResponse> resultList = spaceAnalyzeService.getSpaceUserAnalyze(spaceUserAnalyzeRequest, loginUser);
        return ResultUtils.success(resultList);
    }

    /**
     * 获取空间排行
     *
     * @param spaceRankAnalyzeRequest 排行分析请求
     * @param request 请求
     * @return 空间排行
     */
    @PostMapping("/rank")
    @ApiOperation(value = "获取空间排行")
    public BaseResponse<List<SpaceVO>> getSpaceRankAnalyze(@RequestBody SpaceRankAnalyzeRequest spaceRankAnalyzeRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(spaceRankAnalyzeRequest == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        List<SpaceVO> resultList = spaceAnalyzeService.getSpaceRankAnalyze(spaceRankAnalyzeRequest, loginUser);
        return ResultUtils.success(resultList);
    }

}
