package com.open.starter.web.core.encryptor;

import com.open.common.core.utils.StringUtils;
import com.open.starter.web.core.EncryptContext;
import com.open.starter.web.enums.AlgorithmType;
import com.open.starter.web.enums.EncodeType;
import com.open.starter.web.utils.EncryptUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * sm2算法实现
 *
 * @author 老马
 * @version 4.6.0
 */
@Slf4j
public class Sm2Encryptor extends AbstractEncryptor {

    private final EncryptContext context;

    public Sm2Encryptor(EncryptContext context) {
        super(context);
        String privateKey = context.getPrivateKey();
        String publicKey = context.getPublicKey();
        if (StringUtils.isAnyEmpty(privateKey, publicKey)) {
            log.error("SM2公私钥均需要提供，公钥加密，私钥解密。");
            throw new IllegalArgumentException("Both SM2 public and private keys need to be provided, public key encryption and private key decryption.");
        }
        this.context = context;
    }

    /**
     * 获得当前算法
     */
    @Override
    public AlgorithmType algorithm() {
        return AlgorithmType.SM2;
    }

    /**
     * 加密
     *
     * @param value      待加密字符串
     * @param encodeType 加密后的编码格式
     */
    @Override
    public String encrypt(String value, EncodeType encodeType) {
        if (encodeType == EncodeType.HEX) {
            return EncryptUtils.encryptBySm2Hex(value, context.getPublicKey());
        } else {
            return EncryptUtils.encryptBySm2(value, context.getPublicKey());
        }
    }

    /**
     * 解密
     *
     * @param value 待加密字符串
     */
    @Override
    public String decrypt(String value) {
        return EncryptUtils.decryptBySm2(value, context.getPrivateKey());
    }
}
