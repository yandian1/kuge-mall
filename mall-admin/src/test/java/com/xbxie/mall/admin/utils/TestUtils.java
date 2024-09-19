package com.kuge.mall.admin.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.kuge.mall.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.StringUtils;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * created by xbxie on 2024/4/21
 */
@Component
public class TestUtils {
    @Autowired
    private MockMvc mockMvc;

    /**
     * @param url
     * @param payload
     * @param t
     * @return T
     * @param <T>
     */
    public <T> T getResData(String url, Object payload, TypeReference<T> t) {
        String contentAsString = "";
        try {
            contentAsString = basePerformAssert(url, payload).andReturn().getResponse().getContentAsString(Charset.forName("utf8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return JSON.parseObject(contentAsString, t);
    }

    /**
     * @param url
     * @param payload
     * @param httpHeaders
     * @param t
     * @return
     * @param <T>
     */
    public <T> T getResData(String url, Object payload, HttpHeaders httpHeaders, TypeReference<T> t) {
        String contentAsString = "";
        try {
            contentAsString = basePerformAssert(url, payload, httpHeaders).andReturn().getResponse().getContentAsString(Charset.forName("utf8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return JSON.parseObject(contentAsString, t);
    }

    /**
     * @param url
     * @param payload
     * @return R<Void>
     */
    public R<Void> getResData(String url, Object payload) {
        String contentAsString = "";
        try {
            contentAsString = basePerformAssert(url, payload).andReturn().getResponse().getContentAsString(Charset.forName("utf8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if (StringUtils.isEmpty(contentAsString)) {
            contentAsString = "{code:-1000}";
        }
        return JSON.parseObject(contentAsString, new TypeReference<R<Void>>(){});
    }

    /**
     * @param url
     * @param payload
     * @param httpHeaders
     * @return R<Void>
     */
    public R<Void> getResData(String url, Object payload, HttpHeaders httpHeaders) {
        String contentAsString = "";
        try {
            contentAsString = basePerformAssert(url, payload, httpHeaders).andReturn().getResponse().getContentAsString(Charset.forName("utf8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if (StringUtils.isEmpty(contentAsString)) {
            contentAsString = "{code:-1000}";
        }
        return JSON.parseObject(contentAsString, new TypeReference<R<Void>>(){});
    }

    public MockHttpServletResponse getResponse(String url, Object payload) {
        return basePerformAssert(url, payload).andReturn().getResponse();
    }

    /**
     * @param url
     * @param payload
     * @return ResultActions
     */
    public ResultActions basePerformAssert(String url, Object payload) {
        MockHttpServletRequestBuilder request = post(url)
                .content(JSON.toJSONString(payload))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        ResultActions actions;
        try {
            actions = mockMvc.perform(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return actions;
    }

    /**
     * @param url
     * @param payload
     * @param httpHeaders
     * @return ResultActions
     */
    public ResultActions basePerformAssert(String url, Object payload, HttpHeaders httpHeaders) {
        MockHttpServletRequestBuilder request = post(url).headers(httpHeaders)
            .content(JSON.toJSONString(payload))
            .contentType(MediaType.APPLICATION_JSON_VALUE);

        ResultActions actions;
        try {
            actions = mockMvc.perform(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return actions;
    }

    /**
     * @param url
     * @param payload
     * @param code
     * @param message
     * @return ResultActions
     */
    public ResultActions simplePerformAssert(String url, Object payload, int code, String message) {
        ResultActions actions;

        try {
            actions = basePerformAssert(url, payload)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.code").value(code));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return actions;
    }

    public ResultActions simplePerformAssert(String url, Object payload, HttpHeaders httpHeaders, int code, String message) {
        ResultActions actions;

        try {
            actions = basePerformAssert(url, payload, httpHeaders)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.code").value(code));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return actions;
    }

    /**
     * @param url
     * @param payload
     * @param message
     */
    public void assertSuccess(String url, Object payload, String message) {
        simplePerformAssert(url, payload, 0, message);
    }

    public void assertSuccess(String url, Object payload, HttpHeaders httpHeaders, String message) {
        simplePerformAssert(url, payload, httpHeaders,0, message);
    }

    /**
     * @param url
     * @param payload
     * @param message
     */
    public void assertFail(String url, Object payload, String message) {
        simplePerformAssert(url, payload, 500, message);
    }

    public void assertFail(String url, Object payload, HttpHeaders httpHeaders, String message) {
        simplePerformAssert(url, payload, httpHeaders, 500, message);
    }

    /**
     * @param url
     * @param list
     * @param message
     */
    public void assertFailBatch(String url, List<?> list, String message)  {
        for (Object item : list) {
            assertFail(url, item, message);
        }
    }

    /**
     * @param tableName
     * @param id
     * @return boolean
     */
    public boolean deletePhysical(String tableName, Long id) {
        return SqlRunner.db().delete("delete from " + tableName + " where id = " + id);
    }

    /**
     * @param tableName
     * @param idList
     * @return boolean
     */
    public boolean deleteBatchPhysical(String tableName, List<Long> idList) {
        String idsStr = idList.stream().map(String::valueOf).collect(Collectors.joining(","));
        return SqlRunner.db().delete("delete from " + tableName + " where id in ( " + idsStr + " )");
    }
}
