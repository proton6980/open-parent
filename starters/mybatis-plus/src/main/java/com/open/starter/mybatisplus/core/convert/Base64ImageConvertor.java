package com.open.starter.mybatisplus.core.convert;

import cn.hutool.core.util.StrUtil;
import org.mapstruct.Named;

import java.util.Base64;

/**
 * Base64 图片转换器
 *
 * @author godLian
 */
public class Base64ImageConvertor {

    /**
     * Base64 转 byte[]
     *
     * @param base64 Base64字符串
     * @return byte[]
     */
    @Named("string2ByteArray")
    public byte[] string2ByteArray(String base64) {
        if (StrUtil.isBlank(base64)) {
            return null;
        }
        return Base64.getDecoder().decode(base64);
    }

    /**
     * byte[] 转 Base64
     *
     * @param bytes 字节数组
     * @return Base64
     */
    @Named("byteArray2String")
    public String byteArray2String(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(bytes);
    }
}
