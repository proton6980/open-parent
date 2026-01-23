package com.open.common.business.service;

import com.open.common.business.pojo.dto.OssDTO;

import java.util.List;

/**
 * 通用 OSS服务
 *
 * @author open
 */
public interface OssService {

    /**
     * 通过ossId查询对应的url
     *
     * @param ossIds ossId串逗号分隔
     * @return url串逗号分隔
     */
    String selectUrlByIds(String ossIds);

    /**
     * 通过ossId查询列表
     *
     * @param ossIds ossId串逗号分隔
     * @return 列表
     */
    List<OssDTO> selectByIds(String ossIds);
}
