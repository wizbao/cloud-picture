package com.wizbao.cloudpicture.controller;

import com.wizbao.cloudpicture.common.BaseResponse;
import com.wizbao.cloudpicture.common.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Api(tags = "主接口")
public class MainController {

    /**
     * 健康检查
     */
    @GetMapping("/health")
    @ApiOperation(value = "健康检查")
    public BaseResponse<String> health() {
        return ResultUtils.success();
    }
}
