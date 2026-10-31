package com.wizbao.cloudpicture.imageSearch;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wizbao.cloudpicture.exception.BusinessException;
import com.wizbao.cloudpicture.exception.ErrorCode;
import com.wizbao.cloudpicture.exception.ThrowUtils;
import com.wizbao.cloudpicture.imageSearch.model.ImageSearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 调用 SerpAPI Bing Reverse Image 完成以图搜图。
 * 接口说明：https://serpapi.com/bing-reverse-image-api
 */
@Slf4j
@Component
public class ImageSearchApi {

    private static final String SEARCH_URL = "https://serpapi.com/search.json";

    private static final String ENGINE = "bing_reverse_image";

    @Value("${serpapi.api-key}")
    private String apiKey;

    /**
     * 根据图片地址搜索相似图片。
     *
     * @param imageSearchUrl 待搜索的图片地址
     * @return 相似图片列表
     */
    public List<ImageSearchResult> searchPictureByPicture(String imageSearchUrl) {
        log.info("以图搜图开始, imageSearchUrl={}", imageSearchUrl);
        ThrowUtils.throwIf(StrUtil.isBlank(imageSearchUrl), ErrorCode.PARAMS_ERROR, "图片地址不能为空");
        ThrowUtils.throwIf(!StrUtil.startWithAny(imageSearchUrl, "http://", "https://"),
                ErrorCode.PARAMS_ERROR, "图片地址不合法");
        ThrowUtils.throwIf(StrUtil.isBlank(apiKey), ErrorCode.SYSTEM_ERROR, "未配置 SerpAPI Key");

        String body;
        try (HttpResponse response = HttpRequest.get(SEARCH_URL)
                .form("engine", ENGINE)
                .form("image_url", imageSearchUrl)
                .form("api_key", apiKey)
                .form("count", 20) // 每页数量
                .timeout(20000)
                .execute()) {
            body = response.body();
            if (!response.isOk()) {
                log.error("以图搜图请求失败, status={}, imageSearchUrl={}", response.getStatus(), imageSearchUrl);
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "以图搜图失败");
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("以图搜图请求异常, imageSearchUrl={}", imageSearchUrl, e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "以图搜图失败");
        }
        List<ImageSearchResult> imageSearchResult = parseImageSearchResult(body);
        log.info("以图搜图结束,  result={}", imageSearchResult);
        return imageSearchResult;
    }

    /**
     * 将 SerpAPI 返回解析为相似图片列表。
     * 相似图取 related_content，包含该图的页面取 pages_with_this_image。
     *
     * @param body SerpAPI JSON 响应
     * @return 相似图片列表
     */
    List<ImageSearchResult> parseImageSearchResult(String body) {
        ThrowUtils.throwIf(!JSONUtil.isTypeJSONObject(body), ErrorCode.OPERATION_ERROR, "以图搜图返回异常");
        JSONObject root = JSONUtil.parseObj(body);
        String error = root.getStr("error");
        if (StrUtil.isNotBlank(error)) {
            log.error("以图搜图接口返回错误: {}", error);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "以图搜图失败");
        }
        JSONObject metadata = root.getJSONObject("search_metadata");
        if (metadata != null) {
            String status = metadata.getStr("status");
            ThrowUtils.throwIf(StrUtil.isNotBlank(status) && !"Success".equalsIgnoreCase(status),
                    ErrorCode.OPERATION_ERROR, "以图搜图失败");
        }

        List<ImageSearchResult> imageSearchResult = new ArrayList<>();
        Set<String> seen = new LinkedHashSet<>();
        appendImageSearchResult(imageSearchResult, seen, root.getJSONArray("related_content"));
        appendImageSearchResult(imageSearchResult, seen, root.getJSONArray("pages_with_this_image"));
        return imageSearchResult;
    }

    private void appendImageSearchResult(List<ImageSearchResult> imageSearchResult, Set<String> seen, JSONArray items) {
        if (CollUtil.isEmpty(items)) {
            return;
        }
        for (Object item : items) {
            JSONObject image = item instanceof JSONObject ? (JSONObject) item : JSONUtil.parseObj(item);
            String thumbUrl = StrUtil.blankToDefault(image.getStr("thumbnail"), image.getStr("cdn_original"));
            String fromUrl = StrUtil.blankToDefault(image.getStr("source"), image.getStr("original"));
            if (StrUtil.isBlank(thumbUrl) && StrUtil.isBlank(fromUrl)) {
                continue;
            }
            String uniqueKey = StrUtil.blankToDefault(thumbUrl, "") + "|" + StrUtil.blankToDefault(fromUrl, "");
            if (!seen.add(uniqueKey)) {
                continue;
            }
            ImageSearchResult result = new ImageSearchResult();
            result.setThumbUrl(thumbUrl);
            result.setFromUrl(fromUrl);
            imageSearchResult.add(result);
        }
    }
}
