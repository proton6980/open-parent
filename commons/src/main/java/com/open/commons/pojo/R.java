package com.open.commons.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应对象
 *
 * @param <T> 范型
 * @author open
 */
@Data
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class R<T> {
    /**
     * 响应码
     */
    private int code;
    /**
     * 响应消息
     */
    private String msg;
    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应成功
     *
     * @param <T> 范型
     * @return 响应对象
     */
    public static <T> R<T> ok() {
        return new R<>(200, "success", null);
    }

    /**
     * 响应成功
     *
     * @param data 数据
     * @param <T>  范型
     * @return 响应对象
     */
    public static <T> R<T> ok(T data) {
        return new R<>(200, "success", data);
    }

    /**
     * 响应失败
     *
     * @return 响应对象
     */
    public static R<Void> fail() {
        return new R<>(500, "failure", null);
    }

    /**
     * 响应失败
     *
     * @param msg 错误信息
     * @return 响应对象
     */
    public static R<Void> fail(String msg) {
        return new R<>(500, msg, null);
    }

    /**
     * 响应失败
     *
     * @param code 错误码
     * @param msg  错误信息
     * @return 响应对象
     */
    public static R<Void> fail(int code, String msg) {
        R<Void> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

}
