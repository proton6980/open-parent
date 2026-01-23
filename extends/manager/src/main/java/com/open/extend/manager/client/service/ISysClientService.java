package com.open.extend.manager.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.open.extend.manager.client.domain.SysClient;
import com.open.extend.manager.client.domain.bo.SysClientBo;
import com.open.extend.manager.client.domain.vo.SysClientVo;
import com.open.common.core.pojo.page.PageQuery;
import com.open.common.core.pojo.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 客户端管理Service接口
 *
 * @author open
 */
public interface ISysClientService extends IService<SysClient> {

    /**
     * 查询客户端管理
     */
    SysClientVo queryById(Long id);

    /**
     * 查询客户端信息基于客户端id
     */
    SysClientVo queryByClientId(String clientId);

    /**
     * 查询客户端管理列表
     */
    TableDataInfo<SysClientVo> queryPageList(SysClientBo bo, PageQuery pageQuery);

    /**
     * 查询客户端管理列表
     */
    List<SysClientVo> queryList(SysClientBo bo);

    /**
     * 新增客户端管理
     */
    Boolean insertByBo(SysClientBo bo);

    /**
     * 修改客户端管理
     */
    Boolean updateByBo(SysClientBo bo);

    /**
     * 修改状态
     */
    int updateUserStatus(String clientId, String status);

    /**
     * 校验并批量删除客户端管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
