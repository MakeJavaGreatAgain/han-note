package com.hanserwei.hannote.user.config;

import com.hanserwei.hannote.user.api.FileClient;
import com.hanserwei.hannote.user.constants.ApiConstants;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.web.service.registry.HttpServiceGroup;
import org.springframework.web.service.registry.ImportHttpServices;

/**
 * @author hanserwei
 */
@AutoConfiguration
@ConditionalOnMissingBean(FileClient.class)
@ImportHttpServices(
    group = ApiConstants.SERVICE_NAME,
    types = FileClient.class,
    clientType = HttpServiceGroup.ClientType.REST_CLIENT
)
public class OssApiAutoConfiguration {
}
