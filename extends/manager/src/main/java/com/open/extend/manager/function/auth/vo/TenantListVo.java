package com.open.extend.manager.function.auth.vo;

import com.open.extend.manager.function.tenant.domain.vo.SysTenantVo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

/**
 * 租户列表
 *
 * @author open
 */
@Data
@AutoMapper(target = SysTenantVo.class)
public class TenantListVo {

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 域名
     */
    private String domain;

}
