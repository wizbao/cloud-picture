package com.wizbao.cloudpicture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wizbao.cloudpicture.model.dto.picture.*;
import com.wizbao.cloudpicture.model.entity.Picture;
import com.wizbao.cloudpicture.model.entity.User;
import com.wizbao.cloudpicture.model.vo.PictureVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface PictureService extends IService<Picture> {

    /**
     * 上传图片
     *
     * @param inputSource          图片源，可以是文件或图片地址
     * @param pictureUploadRequest 图片上传请求
     * @param loginUser            登录用户
     * @return 图片封装
     */
    PictureVO uploadPicture(Object inputSource, PictureUploadRequest pictureUploadRequest, User loginUser);

    /**
     * 获取图片查询条件
     *
     * @param pictureQueryRequest 图片查询请求
     * @return 查询条件
     */
    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);

    /**
     * 获取图片封装
     *
     * @param picture 图片
     * @param request 请求
     * @return 图片封装
     */
    PictureVO getPictureVO(Picture picture, HttpServletRequest request);

    /**
     * 分页获取图片封装
     *
     * @param picturePage 图片分页
     * @param request 请求
     * @return 图片封装分页
     */
    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage, HttpServletRequest request);

    /**
     * 校验图片
     *
     * @param picture 图片
     */
    void validPicture(Picture picture);

    /**
     * 图片审核
     *
     * @param pictureReviewRequest 审核请求
     * @param loginUser            登录用户
     */
    void doPictureReview(PictureReviewRequest pictureReviewRequest, User loginUser);

    /**
     * 填充审核参数
     *
     * @param picture   图片
     * @param loginUser 登录用户
     */
    void fillReviewParams(Picture picture, User loginUser);

    /**
     * 批量抓取和创建图片
     *
     * @param pictureUploadByBatchRequest 批量上传请求
     * @param loginUser                   登录用户
     * @return 成功创建的图片数
     */
    Integer uploadPictureByBatch(
            PictureUploadByBatchRequest pictureUploadByBatchRequest,
            User loginUser
    );

    /**
     * 异步清理不再被引用的图片文件
     *
     * @param oldPicture 已删除的图片
     */
    @Async
    void clearPictureFile(Picture oldPicture);

    /**
     * 图片更新和删除的权限校验
     *
     * @param loginUser 登录用户
     * @param picture   图片
     */
    void checkPictureAuth(User loginUser, Picture picture);

    /**
     * 删除图片并释放空间额度
     *
     * @param pictureId 图片 id
     * @param loginUser 登录用户
     */
    void deletePicture(long pictureId, User loginUser);

    /**
     * 编辑图片
     *
     * @param pictureEditRequest 编辑图片请求
     * @param loginUser 登录用户
     */
    void editPicture(PictureEditRequest pictureEditRequest, User loginUser);

    /**
     * 按空间查询图片
     *
     * @param spaceId 空间 id
     * @return 图片列表
     */
    List<Picture> listPictureBySpaceId(Long spaceId);

    /**
     * 按主色调搜索空间内相似图片
     *
     * @param spaceId 空间 id
     * @param picColor 目标颜色
     * @param loginUser 登录用户
     * @return 相似图片列表
     */
    List<PictureVO> searchPictureByColor(Long spaceId, String picColor, User loginUser);

    /**
     * 批量编辑图片的分类、标签和名称
     *
     * @param pictureEditByBatchRequest 批量编辑请求
     * @param loginUser 登录用户
     */
    @Transactional(rollbackFor = Exception.class)
    void editPictureByBatch(PictureEditByBatchRequest pictureEditByBatchRequest, User loginUser);

    /**
     * 多线程批量编辑图片的分类和标签
     *
     * @param pictureEditByBatchRequest 批量编辑请求
     * @param loginUser 登录用户
     */
    @Transactional(rollbackFor = Exception.class)
    void editPictureByBatchMultiThread(PictureEditByBatchRequest pictureEditByBatchRequest, User loginUser);
}
