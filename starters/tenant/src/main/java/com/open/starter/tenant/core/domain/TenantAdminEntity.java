package com.open.starter.tenant.core.domain;

import com.open.starter.mybatisplus.core.domain.BaseAdminEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 管理后台租户基类
 *
 * @author godLian
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantAdminEntity extends BaseAdminEntity {

    /**
     * 租户编号
     */
    private String tenantId;

}
