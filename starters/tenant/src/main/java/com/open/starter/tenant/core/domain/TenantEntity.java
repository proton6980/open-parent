package com.open.starter.tenant.core.domain;

import com.open.starter.mybatisplus.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户基类
 *
 * @author godLian
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantEntity extends BaseEntity {

    /**
     * 租户编号
     */
    private String tenantId;

}
