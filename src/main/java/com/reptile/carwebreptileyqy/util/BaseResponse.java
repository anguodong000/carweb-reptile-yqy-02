package com.reptile.carwebreptileyqy.util;

import org.apache.commons.lang3.StringUtils;

public class BaseResponse<T> {
    public static final String DEFAULT_CODE = "200";
    public static final String DEFAULT_MESSAGE = "ok";

    private String code = "200";
    private String message = "OK";
    private String extraCode;
    private String extraMessage;
    private String tracestack;
    private String url;
    private T data;

    public BaseResponse(String code, String message, String extraCode, String extraMessage, T data) {
        this.code = (String) StringUtils.defaultIfBlank(code, "200");
        this.message = (String)StringUtils.defaultIfBlank(message, "ok");
        this.extraCode = StringUtils.trimToEmpty(extraCode);
        this.extraMessage = StringUtils.trimToEmpty(extraMessage);
        this.data = data;
    }

    public BaseResponse(String code, String message, T data) {
        this(code, message, (String)null, (String)null, data);
    }

    public BaseResponse(T data) {
        this((String)null, (String)null, data);
    }

    public BaseResponse() {
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getExtraCode() {
        return this.extraCode;
    }

    public String getExtraMessage() {
        return this.extraMessage;
    }

    public String getTracestack() {
        return this.tracestack;
    }

    public String getUrl() {
        return this.url;
    }

    public T getData() {
        return this.data;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setExtraCode(String extraCode) {
        this.extraCode = extraCode;
    }

    public void setExtraMessage(String extraMessage) {
        this.extraMessage = extraMessage;
    }

    public void setTracestack(String tracestack) {
        this.tracestack = tracestack;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setData(T data) {
        this.data = data;
    }
}
