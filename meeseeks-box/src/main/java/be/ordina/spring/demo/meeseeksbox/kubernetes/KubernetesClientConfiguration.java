package be.ordina.spring.demo.meeseeksbox.kubernetes;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.apis.AppsV1beta2Api;
import io.kubernetes.client.util.Config;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.io.IOException;


@org.springframework.context.annotation.Configuration
@Profile("kubernetes")
@EnableConfigurationProperties(KubernetesMeeseeksConfigurationProperties.class)
public class KubernetesClientConfiguration {



    @Bean
    public ApiClient client() throws IOException {
        final ApiClient client = Config.defaultClient();
        io.kubernetes.client.Configuration.setDefaultApiClient(client);
        return client;
    }

    @Bean
    public AppsV1beta2Api appsV1beta2Api() throws IOException {
        return new AppsV1beta2Api();
    }

}
