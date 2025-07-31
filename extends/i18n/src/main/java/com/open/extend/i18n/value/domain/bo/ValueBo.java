package com.open.extend.i18n.value.domain.bo;

import com.open.commons.validated.Save;
import com.open.commons.validated.Update;
import com.open.extend.i18n.value.domain.Value;
import com.open.starter.mybatisplus.core.domain.BaseAdminEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 值入参
 *
 * @author open
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Value.class, reverseConvertGenerate = false)
public class ValueBo extends BaseAdminEntity {

    @NotNull(message = "id不能为空", groups = {Update.class})
    private Long id;

    @NotNull(message = "编码不能为空", groups = {Save.class, Update.class})
    private String code;

    @NotNull(message = "名称不能为空", groups = {Save.class, Update.class})
    private String name;

    private String remark;
}
