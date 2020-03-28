package com.reptile.carwebreptileyqy.util;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;

/**
 * @Desciption:
 * @Author: Erting.Wang
 * @Date: 2019/7/24 15:00
 */
public class HttpUtil {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36";

    // 超时设置
    private static final RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(300000)
            .setConnectionRequestTimeout(300000)
            .setSocketTimeout(300000)
            .build();

    // 编码设置
    private static final ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .setMalformedInputAction(CodingErrorAction.IGNORE)
            .setUnmappableInputAction(CodingErrorAction.IGNORE)
            .setCharset(Consts.UTF_8)
            .build();

    private static HttpClientBuilder getBuilder() {
        List<Header> headers = new ArrayList<>();
        Header header = new BasicHeader("User-Agent", USER_AGENT);
        headers.add(header);
        return HttpClients.custom().setDefaultConnectionConfig(connectionConfig).setDefaultHeaders(headers).setDefaultRequestConfig(requestConfig);
    }

    /**
     * 发送HttpGet请求
     *
     * @param url 请求地址
     * @return
     */
    public static String sendGet(String url) throws IOException {
        return sendGetWithHeader(url, null);
    }

    /**
     * 发送HttpGet请求
     *
     * @param url      请求地址
     * @param headerMap 头部信息
     * @return
     */
    public static String sendGetWithHeader(String url, Map<String, String> headerMap) throws IOException {
        String result;
        HttpGet httpGet = new HttpGet(url);

        if (headerMap != null) {
            headerMap.forEach((key, val) -> {
                httpGet.setHeader(key, val);
            });
        }

        try (CloseableHttpClient httpclient = getBuilder().build();
             CloseableHttpResponse response = httpclient.execute(httpGet)) {
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
        }
        return result;
    }

    /**
     * 发送HttpPost请求，参数为json字符串
     *
     * @param url     请求地址
     * @param jsonStr json字符串
     * @return
     */
    public static String sendPost(String url, String jsonStr) throws IOException {
        return sendPostWithHeader(url, jsonStr, null);
    }

    /**
     * 发送HttpPost请求，参数为json字符串
     *
     * @param url      请求地址
     * @param jsonStr  json字符串
     * @param headerMap 头部信息
     * @return
     */
    public static String sendPostWithHeader(String url, String jsonStr, Map<String, String> headerMap) throws IOException {
        return sendPostWithHeaderFull(url, jsonStr, "application/json", Consts.UTF_8, headerMap);
    }

    public static String sendPostWithHeaderFull(String url, String jsonStr, String contentType, Charset charset, Map<String, String> headerMap) throws IOException {
        String result;
        // 设置entity
        StringEntity stringEntity = new StringEntity(jsonStr, charset);
        stringEntity.setContentType(contentType);

        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(stringEntity);

        if (headerMap != null) {
            headerMap.forEach((key, val) -> {
                httpPost.setHeader(key, val);
            });
        }

        try (CloseableHttpClient httpclient = getBuilder().build(); CloseableHttpResponse httpResponse = httpclient.execute(httpPost);) {
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
        }
        return result;
    }


    public static void main(String[] args) {

        LocalDateTime now = LocalDateTime.now();

        System.out.println(now.toEpochSecond(ZoneOffset.of("+8")));
        System.out.println(now.toEpochSecond(ZoneOffset.UTC));
        System.out.println(now.toEpochSecond(ZoneOffset.of("+0")));

        //System.out.println(UTCTimeUtil.getUTCTime().getTime() / 1000);
    }
}
