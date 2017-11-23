package be.ordina.spring.demo.meeseeksbox.cloudfoundry;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.doppler.DopplerClient;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.cloudfoundry.uaa.UaaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("cloud")
@EnableConfigurationProperties(CloudFoundryClientConfigProperties.class)
public class CloudFoundryClientConfiguration {

    private final CloudFoundryClientConfigProperties properties;

    CloudFoundryClientConfiguration(CloudFoundryClientConfigProperties properties) {
        this.properties = properties;
    }

    @Bean
    public ConnectionContext cloudFoundryConnectionContext() {
        return DefaultConnectionContext.builder()
            .apiHost(properties.getApiHost())
            .skipSslValidation(properties.isSkipSslValidation())
            .build();
    }

    @Bean
    public CloudFoundryClient cloudFoundryClient(ConnectionContext context, TokenProvider tokenProvider) {
        return ReactorCloudFoundryClient.builder()
            .tokenProvider(tokenProvider)
            .connectionContext(context)
            .build();
    }

    @Bean
    public UaaClient uaaClient(ConnectionContext context, TokenProvider tokenProvider) {
        return ReactorUaaClient.builder()
            .tokenProvider(tokenProvider)
            .connectionContext(context)
            .build();
    }

    @Bean
    @Autowired
    public DopplerClient dopplerClient(ConnectionContext context, TokenProvider tokenProvider) {
        return ReactorDopplerClient.builder()
            .tokenProvider(tokenProvider)
            .connectionContext(context)
            .build();
    }

    @Bean
    public CloudFoundryOperations cloudFoundryOperations(CloudFoundryClient cfClient, UaaClient uaaClient,
                                                         DopplerClient dopplerClient) {
        return DefaultCloudFoundryOperations.builder()
            .cloudFoundryClient(cfClient)
            .dopplerClient(dopplerClient)
            .uaaClient(uaaClient)
            .organization(properties.getTargetOrg())
            .space(properties.getTargetSpace())
            .build();
    }

    @Bean
    @ConditionalOnProperty(name = { "username", "password" }, prefix = "cloudfoundry.client")
    public TokenProvider tokenProvider() {
        return PasswordGrantTokenProvider.builder()
            .username(properties.getUsername())
            .password(properties.getPassword())
            .build();
    }
}
