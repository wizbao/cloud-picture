package com.wizbao.cloudpicture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wizbao.cloudpicture.model.dto.space.spaceuser.SpaceUserAddRequest;
import com.wizbao.cloudpicture.model.dto.space.spaceuser.SpaceUserQueryRequest;
import com.wizbao.cloudpicture.model.entity.SpaceUser;
import com.wizbao.cloudpicture.model.vo.space.spaceuser.SpaceUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface SpaceUserService extends IService<SpaceUser> {

    /**
     * 添加空间成员
     *
     * @param spaceUserAddRequest 添加成员请求
     * @return 成员记录 id
     */
    long addSpaceUser(SpaceUserAddRequest spaceUserAddRequest);

    /**
     * 校验空间成员
     *
     * @param spaceUser 空间成员
     * @param add 是否为创建操作
     */
    void validSpaceUser(SpaceUser spaceUser, boolean add);

    /**
     * 获取空间成员查询条件
     *
     * @param spaceUserQueryRequest 空间成员查询请求
     * @return 查询条件
     */
    QueryWrapper<SpaceUser> getQueryWrapper(SpaceUserQueryRequest spaceUserQueryRequest);

    /**
     * 获取空间成员封装
     *
     * @param spaceUser 空间成员
     * @param request 请求
     * @return 空间成员封装
     */
    SpaceUserVO getSpaceUserVO(SpaceUser spaceUser, HttpServletRequest request);

    /**
     * 获取空间成员封装列表
     *
     * @param spaceUserList 空间成员列表
     * @return 空间成员封装列表
     */
    List<SpaceUserVO> getSpaceUserVOList(List<SpaceUser> spaceUserList);
}
