package com.wizbao.cloudpicture.manager.auth;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.wizbao.cloudpicture.constant.SpaceUserPermissionConstant;
import com.wizbao.cloudpicture.manager.auth.model.SpaceUserAuthConfig;
import com.wizbao.cloudpicture.manager.auth.model.SpaceUserRole;
import com.wizbao.cloudpicture.model.entity.Space;
import com.wizbao.cloudpicture.model.entity.SpaceUser;
import com.wizbao.cloudpicture.model.entity.User;
import com.wizbao.cloudpicture.model.enums.SpaceRoleEnum;
import com.wizbao.cloudpicture.model.enums.SpaceTypeEnum;
import com.wizbao.cloudpicture.service.SpaceUserService;
import com.wizbao.cloudpicture.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class SpaceUserAuthManager {

    /**
     * 用户服务
     */
    @Resource
    private UserService userService;

    /**
     * 空间成员服务
     */
    @Resource
    private SpaceUserService spaceUserService;

    /**
     * 空间用户权限配置
     */
    public static final SpaceUserAuthConfig SPACE_USER_AUTH_CONFIG;

    static {
        String json = ResourceUtil.readUtf8Str("biz/spaceUserAuthConfig.json");
        SPACE_USER_AUTH_CONFIG = JSONUtil.toBean(json, SpaceUserAuthConfig.class);
    }

    /**
     * 根据角色获取权限列表
     *
     * @param spaceUserRole 空间角色
     * @return 权限列表
     */
    public List<String> getPermissionsByRole(String spaceUserRole) {
        if (StrUtil.isBlank(spaceUserRole)) {
            return new ArrayList<>();
        }
        SpaceUserRole role = SPACE_USER_AUTH_CONFIG.getRoles()
                .stream()
                .filter(r -> r.getKey().equals(spaceUserRole))
                .findFirst()
                .orElse(null);
        if (role == null) {
            return new ArrayList<>();
        }
        return role.getPermissions();
    }


    /**
     * 获取权限列表
     *
     * @param space     空间
     * @param loginUser 登录用户
     * @return 权限列表
     */
    public List<String> getPermissionList(Space space, User loginUser) {
        if (loginUser == null) {
            return new ArrayList<>();
        }
        // 管理员权限
        List<String> ADMIN_PERMISSIONS = getPermissionsByRole(SpaceRoleEnum.ADMIN.getValue());
        // 公共图库
        if (space == null) {
            if (userService.isAdmin(loginUser)) {
                return ADMIN_PERMISSIONS;
            }
            return Collections.singletonList(SpaceUserPermissionConstant.PICTURE_VIEW);
        }
        SpaceTypeEnum spaceTypeEnum = SpaceTypeEnum.getEnumByValue(space.getSpaceType());
        if (spaceTypeEnum == null) {
            return new ArrayList<>();
        }
        // 根据空间获取对应的权限
        switch (spaceTypeEnum) {
            case PRIVATE:
                // 私有空间，仅本人或管理员有所有权限
                if (space.getUserId().equals(loginUser.getId()) || userService.isAdmin(loginUser)) {
                    return ADMIN_PERMISSIONS;
                } else {
                    return new ArrayList<>();
                }
            case TEAM:
                // 团队空间，查询 SpaceUser 并获取角色和权限
                SpaceUser spaceUser = spaceUserService.lambdaQuery()
                        .eq(SpaceUser::getSpaceId, space.getId())
                        .eq(SpaceUser::getUserId, loginUser.getId())
                        .one();
                if (spaceUser == null) {
                    return new ArrayList<>();
                } else {
                    return getPermissionsByRole(spaceUser.getSpaceRole());
                }
        }
        return new ArrayList<>();
    }


}
