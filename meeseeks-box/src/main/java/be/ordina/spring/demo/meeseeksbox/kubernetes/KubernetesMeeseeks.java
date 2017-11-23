package be.ordina.spring.demo.meeseeksbox.kubernetes;

import be.ordina.spring.demo.GlipGlop;
import be.ordina.spring.demo.RickAndMortyQuote;
import be.ordina.spring.demo.meeseeksbox.Meeseeks;
import be.ordina.spring.demo.meeseeksbox.OutputChannels;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.apis.AppsV1beta2Api;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1ReplicationController;
import io.kubernetes.client.models.V1beta2ReplicaSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@Profile("kubernetes")
public class KubernetesMeeseeks implements Meeseeks {

    private static final Long TASK_TIMEOUT = 10_000L;

    private final AppsV1beta2Api api;
    private final OutputChannels outputChannels;
    private final KubernetesMeeseeksConfigurationProperties properties;
    private final ThreadPoolTaskExecutor taskExecutor;

    public KubernetesMeeseeks(
            final AppsV1beta2Api api,
            final OutputChannels outputChannels,
            final KubernetesMeeseeksConfigurationProperties properties) {
        this.api = api;
        this.outputChannels = outputChannels;
        this.properties = properties;
        this.taskExecutor = new ThreadPoolTaskExecutor();
        this.taskExecutor.setMaxPoolSize(1);
        this.taskExecutor.setCorePoolSize(1);
        this.taskExecutor.setWaitForTasksToCompleteOnShutdown(false);
    }

    @Override
    public void spawn() {
        this.outputChannels.microverse().send(MessageBuilder
                .withPayload(new GlipGlop(RickAndMortyQuote.MR_MEESEEKS_SPAWN, "3"))
                .build());

        setReplicas(rc -> Math.min(rc.getSpec().getReplicas() + 1, 3));
    }

    @Override
    public void kill() {
        setReplicas(rc -> 0);
    }

    @FunctionalInterface
    interface Replicas {

        Integer calculate(V1beta2ReplicaSet rc);

    }

    private void setReplicas(final Replicas replicas){
        taskExecutor.execute(() -> {
            try {
                log.info("Checking if we can scale Mr. Meeseeks...");

                final V1beta2ReplicaSet replicaSet = api.readNamespacedReplicaSet(
                        properties.getReplicaSet(),
                        properties.getNamespace(), null, false, false);


                final Integer numberOfReplicas = replicas.calculate(replicaSet);

                if(!Objects.equals(numberOfReplicas, replicaSet.getSpec().getReplicas())) {
                    replicaSet.getSpec().setReplicas(numberOfReplicas);

                    log.info("Scaling Mr. Meeseeks to {}...", replicaSet.getSpec().getReplicas());

                    final V1beta2ReplicaSet result = api.replaceNamespacedReplicaSet(
                            properties.getReplicaSet(),
                            properties.getNamespace(),
                            replicaSet,
                            null);

                    log.info("Scaled Mr. Meeseeks to {}!", result.getSpec().getReplicas());
                }
            } catch(ApiException ex){
                log.error("Could not scale Mr. Meeseeks!", ex);
            }
        }, TASK_TIMEOUT);

    }

}
