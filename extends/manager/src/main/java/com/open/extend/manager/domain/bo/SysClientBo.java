package com.open.extend.manager.domain.bo;

import com.open.commons.validated.Save;
import com.open.commons.validated.Update;
import com.open.extend.manager.domain.SysClient;
import com.open.starter.mybatisplus.core.domain.BaseAdminEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 授权管理业务对象 sys_client
 *
 * @author godLian
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysClient.class, reverseConvertGenerate = false)
public class SysClientBo extends BaseAdminEntity {
    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = {Update.class})
    private Long id;
    /**
     * 客户端id
     */
    private String clientId;
    /**
     * 客户端key
     */
    @NotBlank(message = "客户端key不能为空", groups = {Save.class, Update.class})
    private String clientKey;
    /**
     * 客户端秘钥
     */
    @NotBlank(message = "客户端秘钥不能为空", groups = {Save.class, Update.class})
    private String clientSecret;
    /**
     * 授权类型
     */
    @NotNull(message = "授权类型不能为空", groups = {Save.class, Update.class})
    private List<String> grantTypeList;
    /**
     * 授权类型
     */
    private String grantType;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * token活跃超时时间
     */
    private Long activeTimeout;
    /**
     * token固定超时时间
     */
    private Long timeout;
    /**
     * 状态（0正常 1停用）
     */
    private String status;
}
