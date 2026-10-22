package com.wizbao.cloudpicture.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wizbao.cloudpicture.model.dto.space.SpaceAddRequest;
import com.wizbao.cloudpicture.model.entity.Space;
import com.wizbao.cloudpicture.model.entity.User;

public interface SpaceService extends IService<Space> {

    void validSpace(Space space, boolean add);

    void fillSpaceBySpaceLevel(Space space);

    long addSpace(SpaceAddRequest spaceAddRequest, User loginUser);
}
