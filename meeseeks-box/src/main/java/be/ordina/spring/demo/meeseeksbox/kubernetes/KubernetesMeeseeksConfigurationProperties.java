package be.ordina.spring.demo.meeseeksbox.kubernetes;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("kubernetes")
public class KubernetesMeeseeksConfigurationProperties {

    private String namespace = "default";
    private String replicaSet= "rs-meeseeks";



}
