package com.hanserwei.hannote.user.biz.domain.mapper;



import com.hanserwei.hannote.user.biz.domain.dataobject.PermissionDO;

import java.util.List;

/**
 * @author hanserwei
 */
public interface PermissionDOMapper {
    /**
     * 查询 APP 端所有被启用的权限
     *
     * @return 权限列表
     */
    List<PermissionDO> selectAppEnabledList();
}