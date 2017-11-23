package be.ordina.spring.demo.meeseeksbox.cloudfoundry;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "cloudfoundry.client")
public class CloudFoundryClientConfigProperties {

    private String apiHost;

    private String clientId;

    private String clientSecret = "";

    private String username;

    private String password;

    private boolean skipSslValidation = false;

    private String targetOrg;

    private String targetSpace;
}
