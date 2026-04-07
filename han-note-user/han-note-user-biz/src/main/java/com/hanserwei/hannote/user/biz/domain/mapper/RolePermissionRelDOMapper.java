package com.hanserwei.hannote.user.biz.domain.mapper;


import com.hanserwei.hannote.user.biz.domain.dataobject.RolePermissionRelDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hanserwei
 */
public interface RolePermissionRelDOMapper {
    /**
     * 根据角色 ID 集合批量查询
     *
     * @param roleIds 角色 ID 集合
     * @return 角色权限关系列表
     */
    List<RolePermissionRelDO> selectByRoleIds(@Param("roleIds") List<Long> roleIds);
}