package be.ordina.spring.demo.meeseeksbox.kubernetes;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.apis.AppsV1beta2Api;
import io.kubernetes.client.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.io.IOException;


@Slf4j
@org.springframework.context.annotation.Configuration
@Profile("kubernetes")
@EnableConfigurationProperties(KubernetesMeeseeksConfigurationProperties.class)
public class KubernetesClientConfiguration {


    @Bean
    public ApiClient client() throws IOException {
        final ApiClient client = Config.fromCluster();
        io.kubernetes.client.Configuration.setDefaultApiClient(client);
        log.info("[Api Client] Base path: {}", client.getBasePath());
        return client;
    }

    @Bean
    public AppsV1beta2Api appsV1beta2Api() throws IOException {
        return new AppsV1beta2Api(client());
    }

}
