package com.open.extend.manager.controller.bo;

import javax.validation.constraints.NotBlank;

import com.open.commons.pojo.LoginBody;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 三方登录对象
 *
 * @author open
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class XcxLoginBody extends LoginBody {

    /**
     * 小程序id(多个小程序时使用)
     */
    private String appid;

    /**
     * 小程序code
     */
    @NotBlank(message = "{xcx.code.not.blank}")
    private String xcxCode;

}
