package com.example.app.config;

import lombok.Data;
/**
 * 请求结果配置类
 *
 * @param <T>
 */
@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 200;
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 200;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 400;
        return result;
    }

    public static <T> Result<T> error(String msg, Integer code) {
        Result result = new Result();
        result.msg = msg;
        result.code = code;
        return result;
    }

}
