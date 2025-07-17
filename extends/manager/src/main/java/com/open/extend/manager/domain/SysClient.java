package com.open.extend.manager.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.open.starter.mybatisplus.core.domain.BaseAdminEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 授权管理对象 sys_client
 *
 * @author godLian
 * @date 2023-05-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_client")
public class SysClient extends BaseAdminEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 客户端id
     */
    private String clientId;
    /**
     * 客户端key
     */
    private String clientKey;
    /**
     * 客户端秘钥
     */
    private String clientSecret;
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
