package com.blueberry

import org.springframework.cloud.app.ApplicationInstanceInfo
import org.springframework.cloud.config.java.AbstractCloudConfig
import org.springframework.cloud.config.java.ServiceScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

/**
 * Configuration for cloud environment
 *
 * @author Ganeshji Marwaha
 * @since 9/12/14
 */
@Configuration
@ServiceScan        // Scans cloud services and maps them to the respective connection factories etc.
@Profile("cloud")   // Makes sure that this config is loaded only for the cloud profile
public class CloudConfig extends AbstractCloudConfig {
    @Bean
    public ApplicationInstanceInfo applicationInfo() {
        return cloud().getApplicationInstanceInfo();
    }
}
