package com.open.extend.i18n.key.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.open.commons.exception.OpenException;
import com.open.commons.utils.MapstructUtils;
import com.open.extend.i18n.key.domain.Key;
import com.open.extend.i18n.key.domain.bo.KyeBo;
import com.open.extend.i18n.key.domain.vo.KeyVo;
import com.open.extend.i18n.key.mapper.IKeyMapper;
import com.open.extend.i18n.key.service.KeyService;
import com.open.extend.i18n.language.domain.Language;
import com.open.extend.i18n.language.mapper.ILanguageMapper;
import com.open.extend.i18n.value.domain.Value;
import com.open.extend.i18n.value.mapper.IValueMapper;
import com.open.starter.mybatisplus.core.page.PageQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 键业务实现
 *
 * @author open
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnMissingBean(KeyService.class)
public class KeyServiceImpl extends ServiceImpl<IKeyMapper, Key> implements KeyService {
    private final ILanguageMapper languageMapper;
    private final IValueMapper valueMapper;

    @Override
    public Page<KeyVo> pageVo(PageQuery pageQuery, KyeBo bo) {
        return this.baseMapper.selectVoPage(pageQuery.build(), Wrappers.lambdaQuery(Key.class));
    }

    @Override
    public Boolean save(KyeBo bo) {
        Key entity = MapstructUtils.convert(bo, Key.class);
        return this.save(entity);
    }

    @Override
    public Boolean update(KyeBo bo) {
        Key oldEntity = this.getById(bo.getId());
        Key newEntity = MapstructUtils.convert(bo, oldEntity);
        return this.updateById(newEntity);
    }

    @Override
    @Cacheable(cacheNames = "ttlCache", key = "'language:json:' + #languageCode")
    public Map<String, String> build(String languageCode) {
        Language language = this.languageMapper.selectOne(Wrappers.lambdaQuery(Language.class)
                .eq(Language::getCode, languageCode));
        if (Objects.isNull(language)) {
            throw new OpenException(StrUtil.format("languageCode {} is not exist", languageCode));
        }
        List<Key> keys = this.list();
        if (CollUtil.isEmpty(keys)) {
            return Collections.emptyMap();
        }
        List<Value> values = this.valueMapper.selectList(Wrappers.lambdaQuery(Value.class)
                .eq(Value::getLanguageId, language.getId())
                .in(Value::getKeyId, keys.stream().map(Key::getId).toArray()));

        Map<Long, String> valueMap = values.stream().collect(Collectors.toMap(Value::getKeyId, Value::getContent));
        return keys.stream().collect(Collectors.toMap(Key::getCode, key -> valueMap.getOrDefault(key.getId(), key.getName())));
    }

    @Override
    public String build(String languageCode, String keyCode, Object... args) {
        Key key = this.getOneOpt(Wrappers.lambdaQuery(Key.class)
                        .eq(Key::getCode, keyCode))
                .orElseThrow(() -> new OpenException("i18n-db", 500, "keyCode {} is not exist", keyCode));
        Language language = this.languageMapper.selectOne(Wrappers.lambdaQuery(Language.class)
                .eq(Language::getCode, languageCode));
        if (Objects.isNull(language)) {
            throw new OpenException("i18n-db", 500, "languageCode {} is not exist", languageCode);
        }
        Value value = this.valueMapper.selectOne(Wrappers.lambdaQuery(Value.class)
                .eq(Value::getKeyId, key.getId())
                .eq(Value::getLanguageId, language.getId()));
        if (Objects.isNull(value)) {
            value = new Value()
                    .setKeyId(key.getId())
                    .setLanguageId(language.getId())
                    .setContent(key.getName());
        }
        return StrUtil.format(value.getContent(), args);
    }
}
