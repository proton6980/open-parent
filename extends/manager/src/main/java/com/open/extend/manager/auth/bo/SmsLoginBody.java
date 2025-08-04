package com.open.extend.manager.auth.bo;

import javax.validation.constraints.NotBlank;

import com.open.commons.pojo.LoginBody;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信登录对象
 *
 * @author open
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SmsLoginBody extends LoginBody {

    /**
     * 手机号前缀
     */
    @NotBlank(message = "{user.phonenumber.not.blank}")
    private String mobilePrefix;

    /**
     * 手机号后缀
     */
    @NotBlank(message = "{user.phonenumber.not.blank}")
    private String mobileSuffix;

    /**
     * 短信code
     */
    @NotBlank(message = "{sms.code.not.blank}")
    private String smsCode;

}
