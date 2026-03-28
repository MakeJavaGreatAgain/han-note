package com.hanserwei.hannote.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author hanserwei
 */
@SpringBootApplication
@EnableDiscoveryClient
public class HannoteGateWayApplication {
    static void main(String[] args) {
        SpringApplication.run(HannoteGateWayApplication.class, args);
    }
}
