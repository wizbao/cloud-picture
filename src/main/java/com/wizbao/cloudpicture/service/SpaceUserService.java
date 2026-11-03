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

    long addSpaceUser(SpaceUserAddRequest spaceUserAddRequest);

    void validSpaceUser(SpaceUser spaceUser, boolean add);

    QueryWrapper<SpaceUser> getQueryWrapper(SpaceUserQueryRequest spaceUserQueryRequest);

    SpaceUserVO getSpaceUserVO(SpaceUser spaceUser, HttpServletRequest request);

    List<SpaceUserVO> getSpaceUserVOList(List<SpaceUser> spaceUserList);
}
