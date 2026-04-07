package com.hanserwei.hannote.auth.rpc;

import com.hanserwei.framework.response.Response;
import com.hanserwei.hannote.user.api.UserClient;
import com.hanserwei.hannote.user.biz.domain.dataobject.RoleDO;
import com.hanserwei.hannote.user.biz.domain.dataobject.UserDO;
import com.hanserwei.hannote.user.biz.domain.dataobject.UserRoleRelDO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * @author AKai
 */
@Component
@Slf4j
public class UserRpcService {

    @Resource
    private UserClient userClient;

    public UserDO selectByPhone(String phone) {
        Response<?> response = userClient.selectByPhone(phone);
        if (Objects.isNull(response) || !response.isSuccess() || Objects.isNull(response.getData())) {
            log.warn("select user by phone fail, phone: {}, response: {}", phone, response);
            return null;
        }

        Object data = response.getData();
        if (!(data instanceof UserDO userDO)) {
            log.warn("select user by phone fail, phone: {}, responseData type invalid, data: {}", phone, data);
            return null;
        }

        return userDO;
    }
    public void insertUser(UserDO userDO) {
        userClient.insertUser(userDO);
    }
    public void insertUserRoleRel(UserRoleRelDO userRoleRelDO){
        userClient.insertUserRoleRel(userRoleRelDO);
    }
    public List<Long> selectUserRoleRelByUserId(Long userId) {
        Response<?> response = userClient.selectUserRoleRelByUserId(userId);
        if (response == null || !response.isSuccess() || response.getData() == null) {
            return Collections.emptyList();
        }

        Object data = response.getData();
        if (!(data instanceof List<?> list)) {
            throw new RuntimeException("selectUserRoleRelByUserId response data is not List");
        }

        List<Long> result = new ArrayList<>(list.size());
        for (Object item : list) {
            if (!(item instanceof Long)) {
                throw new RuntimeException("selectUserRoleRelByUserId response item is not Long: " + item);
            }
            result.add((Long) item);
        }

        return result;
    }
    public RoleDO selectByPrimaryKey(Long primaryKey) {
        Response<?> response = userClient.selectByPrimaryKey(primaryKey);
        if (response == null || !response.isSuccess() || response.getData() == null) {
            return null;
        }

        Object data = response.getData();
        if (!(data instanceof RoleDO roleDO)) {
            return null;
        }

        return roleDO;
    }
    public List<String> selectRoleKeyListByIdList(List<Long> roleIdList) {
        Response<?> response = userClient.selectRoleKeyListByIdList(roleIdList);
        if (response == null || !response.isSuccess() || response.getData() == null) {
            return Collections.emptyList();
        }

        Object data = response.getData();
        if (!(data instanceof List<?> list)) {
            throw new RuntimeException("selectRoleKeyListByIdList response data is not List");
        }

        List<String> result = new ArrayList<>(list.size());
        for (Object item : list) {
            if (!(item instanceof String str)) {
                throw new RuntimeException("selectRoleKeyListByIdList response item is not String: " + item);
            }
            result.add(str);
        }

        return result;
    }
}
