package com.wizbao.cloudpicture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wizbao.cloudpicture.model.dto.space.SpaceAddRequest;
import com.wizbao.cloudpicture.model.dto.space.SpaceQueryRequest;
import com.wizbao.cloudpicture.model.entity.Space;
import com.wizbao.cloudpicture.model.entity.User;
import com.wizbao.cloudpicture.model.vo.SpaceVO;

import javax.servlet.http.HttpServletRequest;

public interface SpaceService extends IService<Space> {

    void validSpace(Space space, boolean add);

    void fillSpaceBySpaceLevel(Space space);

    long addSpace(SpaceAddRequest spaceAddRequest, User loginUser);

    QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest);

    void deleteSpace(Long id, User loginUser);

    SpaceVO getSpaceVO(Space space, HttpServletRequest request);

    Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage,HttpServletRequest request);
}
