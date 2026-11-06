package com.wizbao.cloudpicture.config;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * 包装请求，使 InputStream 可以重复读取
 */
@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {

    /**
     * 缓存的请求体
     */
    private final String body;

    /**
     * 读取并缓存原始请求体
     *
     * @param request 原始请求
     */
    public RequestWrapper(HttpServletRequest request) {
        super(request);
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = request.getInputStream(); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            char[] charBuffer = new char[128];
            int bytesRead = -1;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
        } catch (IOException ignored) {
        }
        body = stringBuilder.toString();
    }

    /**
     * 返回可重复读取的输入流
     *
     * @return 输入流
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {
            /**
             * 流是否读取完毕
             *
             * @return 是否读取完毕
             */
            @Override
            public boolean isFinished() {
                return false;
            }

            /**
             * 流是否可读
             *
             * @return 是否可读
             */
            @Override
            public boolean isReady() {
                return false;
            }

            /**
             * 设置读取监听器
             *
             * @param readListener 读取监听器
             */
            @Override
            public void setReadListener(ReadListener readListener) {
            }

            /**
             * 读取下一个字节
             *
             * @return 字节值，结束时返回 -1
             */
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };

    }

    /**
     * 返回请求体读取器
     *
     * @return 读取器
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    /**
     * 获取缓存的请求体
     *
     * @return 请求体
     */
    public String getBody() {
        return this.body;
    }

}
