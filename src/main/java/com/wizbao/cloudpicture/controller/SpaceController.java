package com.wizbao.cloudpicture.controller;

import com.wizbao.cloudpicture.annotation.AuthCheck;
import com.wizbao.cloudpicture.common.BaseResponse;
import com.wizbao.cloudpicture.common.ResultUtils;
import com.wizbao.cloudpicture.constant.UserConstant;
import com.wizbao.cloudpicture.exception.BusinessException;
import com.wizbao.cloudpicture.exception.ErrorCode;
import com.wizbao.cloudpicture.exception.ThrowUtils;
import com.wizbao.cloudpicture.model.dto.space.SpaceUpdateRequest;
import com.wizbao.cloudpicture.model.entity.Space;
import com.wizbao.cloudpicture.service.SpaceService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/space")
@Api(tags = "空间接口")
public class SpaceController {

    @Resource
    private SpaceService spaceService;

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




}
