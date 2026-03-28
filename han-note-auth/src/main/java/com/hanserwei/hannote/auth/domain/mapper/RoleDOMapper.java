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

    /**
     * 根据主键查询角色
     *
      * @param roleId 角色ID
     * @return 角色信息
     */
    RoleDO selectByPrimaryKey(Long roleId);

    /**
     * 根据角色ID列表查询角色权限字符列表
     *
     * @param roleIdList 角色ID列表
     * @return 角色权限字符列表
     */
    List<String> selectRoleKeyListByIdList(List<Long> roleIdList);
}