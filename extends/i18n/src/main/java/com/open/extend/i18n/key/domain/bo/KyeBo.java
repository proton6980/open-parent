package com.open.extend.i18n.key.domain.bo;

import com.open.commons.validated.Save;
import com.open.commons.validated.Update;
import com.open.extend.i18n.language.domain.Language;
import com.open.starter.mybatisplus.core.domain.BaseAdminEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 键入参
 *
 * @author godLian
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Language.class, reverseConvertGenerate = false)
public class KyeBo extends BaseAdminEntity {

    @NotNull(message = "id不能为空", groups = {Update.class})
    private Long id;

    @NotNull(message = "编码不能为空", groups = {Save.class, Update.class})
    private String code;

    @NotNull(message = "名称不能为空", groups = {Save.class, Update.class})
    private String name;

    private String remark;
}
