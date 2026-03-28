package com.hanserwei.hannote.auth.domain.mapper;

import com.hanserwei.hannote.auth.domain.dataobject.RoleDO;

import java.util.List;

/**
 * @author hanserwei
 */
public interface RoleDOMapper {
    /**
     * 查询所有被启用的角色
     *
     * @return 启用角色列表
     */
    List<RoleDO> selectEnabledList();
}