package com.wizbao.cloudpicture.imageSearch;

import cn.hutool.json.JSONUtil;
import com.wizbao.cloudpicture.exception.BusinessException;
import com.wizbao.cloudpicture.imageSearch.model.ImageSearchResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Weber
 * @description 校验以图搜图结果解析
 * @since 2026-10-27
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("local")
class ImageSearchApiTest {

    @Resource
    private ImageSearchApi imageSearchApi;

    @Test
    void parseImageSearchResultMapsThumbnailAndSource() {
        String body = "{"
                + "\"search_metadata\":{\"status\":\"Success\"},"
                + "\"related_content\":["
                + "{\"thumbnail\":\"https://thumb.example/a.jpg\",\"source\":\"https://page.example/a\"},"
                + "{\"thumbnail\":\"https://thumb.example/a.jpg\",\"source\":\"https://page.example/a\"}"
                + "],"
                + "\"pages_with_this_image\":["
                + "{\"cdn_original\":\"https://cdn.example/b.jpg\",\"original\":\"https://img.example/b.jpg\"}"
                + "]"
                + "}";

        List<ImageSearchResult> imageSearchResult = imageSearchApi.parseImageSearchResult(body);

        Assertions.assertEquals(2, imageSearchResult.size());
        Assertions.assertEquals("https://thumb.example/a.jpg", imageSearchResult.get(0).getThumbUrl());
        Assertions.assertEquals("https://page.example/a", imageSearchResult.get(0).getFromUrl());
        Assertions.assertEquals("https://cdn.example/b.jpg", imageSearchResult.get(1).getThumbUrl());
        Assertions.assertEquals("https://img.example/b.jpg", imageSearchResult.get(1).getFromUrl());
    }

    @Test
    void parseImageSearchResultRejectsErrorResponse() {
        Assertions.assertThrows(BusinessException.class,
                () -> imageSearchApi.parseImageSearchResult("{\"error\":\"Invalid API key\"}"));
    }

    @Test
    void testImageSearch() {
        List<ImageSearchResult> imageSearchResults = imageSearchApi.searchPictureByPicture("https://www.topmost10.com/wp-content/uploads/2022/02/Bill-Gate.jpg");
        Assertions.assertNotNull(imageSearchResults);
        log.info("imageSearchResult：{}", JSONUtil.toJsonStr(imageSearchResults));
    }
}
