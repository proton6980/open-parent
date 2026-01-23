package com.open.starter.email.expand;

import cn.hutool.core.util.StrUtil;
import com.open.common.email.mamba.MambaEmailClient;
import com.open.starter.cache.utils.RedisUtils;

import java.time.Duration;

/**
 * 曼巴邮件客户端 token 缓存版
 *
 * @author open
 */
public class MambaCacheEmailClient extends MambaEmailClient {

    public MambaCacheEmailClient(String apiKey, String privateKey) {
        super(apiKey, privateKey);
    }

    @Override
    public String authenticate(Duration expire) {
        String token = RedisUtils.getCacheObject("mamba:email:token");
        if (StrUtil.isBlank(token)) {
            token = super.authenticate(expire);
        }
        RedisUtils.setCacheObject("mamba:email:token", token, expire.plusSeconds(-100));
        return token;
    }
}
