package com.open.extend.i18n.language.mapper;

import com.open.extend.i18n.language.domain.Language;
import com.open.extend.i18n.language.domain.vo.LanguageVo;
import com.open.starter.mybatisplus.core.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 语言表数据库操作接口
 *
 * @author godLian
 */
@Mapper
public interface ILanguageMapper extends IBaseMapper<Language, LanguageVo> {
}
