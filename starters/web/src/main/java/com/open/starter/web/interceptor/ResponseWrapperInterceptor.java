package com.open.starter.web.interceptor;

import com.open.commons.pojo.R;
import com.open.commons.utils.JacksonUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * response响应包装拦截器
 *
 * @author godLian
 */
@RestControllerAdvice
public class ResponseWrapperInterceptor implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        // 排除已经包装过的类型
        return !returnType.getParameterType().equals(R.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        if (body instanceof String) {
            // 对于字符串要特殊处理，否则会被当作普通对象序列化成JSON
            return JacksonUtils.toJsonString(R.ok(body));
        }
        return R.ok(body);
    }
}
