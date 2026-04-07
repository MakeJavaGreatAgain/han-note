package com.hanserwei.hannote.user.config;

import com.hanserwei.hannote.user.api.UserClient;
import com.hanserwei.hannote.user.constants.ApiConstants;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.web.service.registry.HttpServiceGroup;
import org.springframework.web.service.registry.ImportHttpServices;

/**
 * @author AKai
 */
@AutoConfiguration
@ConditionalOnMissingBean(UserClient.class)
@ImportHttpServices(
        group = ApiConstants.SERVICE_NAME,
        types = UserClient.class,
        clientType = HttpServiceGroup.ClientType.REST_CLIENT
)
public class UserApiAutoConfiguration {
}

