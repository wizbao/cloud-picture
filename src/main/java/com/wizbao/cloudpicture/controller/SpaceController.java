package com.wizbao.cloudpicture.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wizbao.cloudpicture.annotation.AuthCheck;
import com.wizbao.cloudpicture.common.BaseResponse;
import com.wizbao.cloudpicture.common.DeleteRequest;
import com.wizbao.cloudpicture.common.ResultUtils;
import com.wizbao.cloudpicture.constant.UserConstant;
import com.wizbao.cloudpicture.exception.BusinessException;
import com.wizbao.cloudpicture.exception.ErrorCode;
import com.wizbao.cloudpicture.exception.ThrowUtils;
import com.wizbao.cloudpicture.model.dto.space.SpaceAddRequest;
import com.wizbao.cloudpicture.model.dto.space.SpaceQueryRequest;
import com.wizbao.cloudpicture.model.dto.space.SpaceUpdateRequest;
import com.wizbao.cloudpicture.model.entity.Picture;
import com.wizbao.cloudpicture.model.entity.Space;
import com.wizbao.cloudpicture.model.entity.User;
import com.wizbao.cloudpicture.model.vo.PictureVO;
import com.wizbao.cloudpicture.model.vo.SpaceVO;
import com.wizbao.cloudpicture.service.SpaceService;
import com.wizbao.cloudpicture.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/space")
@Api(tags = "空间接口")
public class SpaceController {

    @Resource
    private SpaceService spaceService;
    @Resource
    private UserService userService;

    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateSpace(@RequestBody SpaceUpdateRequest spaceUpdateRequest) {
        if (spaceUpdateRequest == null || spaceUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 将实体类和 DTO 进行转换
        Space space = new Space();
        BeanUtils.copyProperties(spaceUpdateRequest, space);
        // 自动填充数据
        spaceService.fillSpaceBySpaceLevel(space);
        // 数据校验
        spaceService.validSpace(space, false);
        // 判断是否存在
        long id = spaceUpdateRequest.getId();
        Space oldSpace = spaceService.getById(id);
        ThrowUtils.throwIf(oldSpace == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = spaceService.updateById(space);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @PostMapping("/add")
    public BaseResponse<Boolean> addSpace(@RequestBody SpaceAddRequest spaceAddRequest, HttpServletRequest request) {
        if (spaceAddRequest == null || StrUtil.isBlank(spaceAddRequest.getSpaceName()) || Objects.isNull(spaceAddRequest.getSpaceLevel())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        spaceService.addSpace(spaceAddRequest, loginUser);
        return ResultUtils.success(true);
    }

    /**
     * 分页获取空间信息列表（仅管理员可用）
     */
    @PostMapping("/list/page")
    @ApiOperation(value = "分页获取空间信息列表（仅管理员可用）")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Space>> listSpaceByPage(@RequestBody SpaceQueryRequest spaceQueryRequest) {
        ThrowUtils.throwIf(spaceQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long current = spaceQueryRequest.getCurrent();
        long size = spaceQueryRequest.getPageSize();
        ThrowUtils.throwIf(current < 1 || size < 1, ErrorCode.PARAMS_ERROR, "分页参数错误");

        Page<Space> spacePage = spaceService.page(
                new Page<>(current, size),
                spaceService.getQueryWrapper(spaceQueryRequest));
        return ResultUtils.success(spacePage);
    }

    /**
     * 删除图片
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除空间")
    public BaseResponse<Boolean> deleteSpace(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        spaceService.deleteSpace(deleteRequest.getId(), loginUser);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取图片（封装类）
     */
    @GetMapping("/get/vo")
    @ApiOperation(value = "根据 id 获取空间（封装类）")
    public BaseResponse<SpaceVO> getSpaceVOById(@ApiParam("空间 id") long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Space space = spaceService.getById(id);
        ThrowUtils.throwIf(space == null, ErrorCode.NOT_FOUND_ERROR);

        // 获取封装类
        return ResultUtils.success(spaceService.getSpaceVO(space, request));
    }
}
