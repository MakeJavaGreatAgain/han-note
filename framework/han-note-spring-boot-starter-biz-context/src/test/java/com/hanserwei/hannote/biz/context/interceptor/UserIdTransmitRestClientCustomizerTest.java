package com.hanserwei.hannote.biz.context.interceptor;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.hanserwei.framework.constant.GlobalConstants;
import com.hanserwei.hannote.biz.context.holer.LoginUserContextHolder;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author hanserwei
 */
class UserIdTransmitRestClientCustomizerTest {

    private final UserIdTransmitRestClientCustomizer customizer = new UserIdTransmitRestClientCustomizer();

    @Test
    void shouldTransmitUserIdFromContext() {
        RestClient.Builder restClientBuilder = RestClient.builder().baseUrl("http://localhost");
        customizer.customize(restClientBuilder);
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        RestClient restClient = restClientBuilder.build();

        server.expect(requestTo("http://localhost/test"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(GlobalConstants.USER_ID, "123"))
                .andRespond(withSuccess());

        LoginUserContextHolder.callWithUserId(123L, () -> {
            restClient.get().uri("/test").retrieve().toBodilessEntity();
            return null;
        });

        server.verify();
    }

    @Test
    void shouldNotOverrideExistingUserIdHeader() {
        RestClient.Builder restClientBuilder = RestClient.builder().baseUrl("http://localhost");
        customizer.customize(restClientBuilder);
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        RestClient restClient = restClientBuilder.build();

        server.expect(requestTo("http://localhost/test"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(GlobalConstants.USER_ID, "456"))
                .andRespond(withSuccess());

        LoginUserContextHolder.callWithUserId(123L, () -> {
            restClient.get()
                    .uri("/test")
                    .header(GlobalConstants.USER_ID, "456")
                    .retrieve()
                    .toBodilessEntity();
            return null;
        });

        server.verify();
    }
}
