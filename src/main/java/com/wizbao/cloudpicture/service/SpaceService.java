package com.wizbao.cloudpicture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wizbao.cloudpicture.model.dto.space.SpaceAddRequest;
import com.wizbao.cloudpicture.model.dto.space.SpaceQueryRequest;
import com.wizbao.cloudpicture.model.entity.Space;
import com.wizbao.cloudpicture.model.entity.User;
import com.wizbao.cloudpicture.model.vo.space.SpaceVO;

import javax.servlet.http.HttpServletRequest;

public interface SpaceService extends IService<Space> {

    /**
     * 校验空间
     *
     * @param space 空间
     * @param add 是否为创建操作
     */
    void validSpace(Space space, boolean add);

    /**
     * 按空间级别填充容量限额
     *
     * @param space 空间
     */
    void fillSpaceBySpaceLevel(Space space);

    /**
     * 创建空间
     *
     * @param spaceAddRequest 创建空间请求
     * @param loginUser 登录用户
     * @return 空间 id
     */
    long addSpace(SpaceAddRequest spaceAddRequest, User loginUser);

    /**
     * 获取空间查询条件
     *
     * @param spaceQueryRequest 空间查询请求
     * @return 查询条件
     */
    QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest);

    /**
     * 删除空间及其图片
     *
     * @param id 空间 id
     * @param loginUser 登录用户
     */
    void deleteSpace(Long id, User loginUser);

    /**
     * 获取空间封装
     *
     * @param space 空间
     * @param request 请求
     * @return 空间封装
     */
    SpaceVO getSpaceVO(Space space, HttpServletRequest request);

    /**
     * 分页获取空间封装
     *
     * @param spacePage 空间分页
     * @param request 请求
     * @return 空间封装分页
     */
    Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage,HttpServletRequest request);

    /**
     * 校验空间操作权限，仅本人或管理员可操作
     *
     * @param loginUser 登录用户
     * @param space 空间
     */
    void checkSpaceAuth(User loginUser, Space space);
}
