package com.wizbao.cloudpicture.controller.devops;

import com.wizbao.cloudpicture.annotation.AuthCheck;
import com.wizbao.cloudpicture.common.BaseResponse;
import com.wizbao.cloudpicture.common.ResultUtils;
import com.wizbao.cloudpicture.constant.UserConstant;
import com.wizbao.cloudpicture.service.PictureDevOpsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zwb
 * @date 2026/10/31 16:05
 * @since 2025.0.1
 **/
@Api(tags = "图片运维接口")
@RestController
@RequestMapping("/devops/picture")
public class PictureDevOpsController {

    @Resource
    private PictureDevOpsService pictureDevOpsService;

    /**
     * 刷新历史图片的主色调
     */
    @PostMapping("/updateAllPicColor")
    @ApiOperation(value = "刷新历史图片的主色调")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Void> updateAllPicColor() {
        pictureDevOpsService.updateAllPicColor();
        return ResultUtils.success();
    }

}
