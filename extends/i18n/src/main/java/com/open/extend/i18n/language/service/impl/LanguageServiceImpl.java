package com.open.extend.i18n.language.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.open.commons.utils.MapstructUtils;
import com.open.extend.i18n.key.domain.Key;
import com.open.extend.i18n.key.mapper.IKeyMapper;
import com.open.extend.i18n.language.domain.Language;
import com.open.extend.i18n.language.domain.bo.LanguageBo;
import com.open.extend.i18n.language.domain.vo.LanguageVo;
import com.open.extend.i18n.language.mapper.ILanguageMapper;
import com.open.extend.i18n.language.service.LanguageService;
import com.open.extend.i18n.value.domain.Value;
import com.open.extend.i18n.value.mapper.IValueMapper;
import com.open.commons.pojo.page.PageQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

/**
 * 语言业务实现
 *
 * @author open
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnMissingBean(LanguageService.class)
public class LanguageServiceImpl extends ServiceImpl<ILanguageMapper, Language> implements LanguageService {
    private final IKeyMapper keyMapper;
    private final IValueMapper valueMapper;

    @Override
    public Page<LanguageVo> pageVo(PageQuery pageQuery, LanguageBo bo) {
        return this.baseMapper.selectVoPage(pageQuery.build(), Wrappers.lambdaQuery(Language.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean save(LanguageBo bo) {
        Language entity = MapstructUtils.convert(bo, Language.class);
        boolean saveFlag = this.save(entity);
        if (saveFlag) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    List<Key> keys = keyMapper.selectList();
                    for (Key key : keys) {
                        valueMapper.insert(new Value()
                                .setKeyId(key.getId())
                                .setLanguageId(entity.getId())
                                .setContent(key.getName())
                                .setRemark(key.getRemark()));
                    }
                }
            });
        }
        return saveFlag;
    }

    @Override
    public Boolean update(LanguageBo bo) {
        Language oldEntity = this.getById(bo.getId());
        Language newEntity = MapstructUtils.convert(bo, oldEntity);
//        newEntity.setIcon(Base64Utils.decodeFromString(bo.getIconBase64()));
        return this.updateById(newEntity);
    }
}
