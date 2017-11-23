package be.ordina.spring.demo.meeseeksbox.cloudfoundry;

import be.ordina.spring.demo.GlipGlop;
import be.ordina.spring.demo.RickAndMortyQuote;
import be.ordina.spring.demo.meeseeksbox.Meeseeks;
import be.ordina.spring.demo.meeseeksbox.OutputChannels;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.applications.GetApplicationRequest;
import org.cloudfoundry.operations.applications.ScaleApplicationRequest;
import org.cloudfoundry.operations.applications.StartApplicationRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.support.MessageBuilder;

@Slf4j
@AllArgsConstructor
@Profile("cloud")
public class CloudFoundryMeeseeks implements Meeseeks {

    private static final String MR_MEESEEKS_APP_NAME = "meeseeks";

    private final CloudFoundryOperations cloudFoundryOperations;
    private final OutputChannels outputChannels;

    @Override
    public void spawn() {
        log.info("Checking whether Mr Meeseeks can be scaled...");

        this.outputChannels.microverse().send(MessageBuilder
            .withPayload(new GlipGlop(RickAndMortyQuote.MR_MEESEEKS_SPAWN, "3"))
            .build());

        this.cloudFoundryOperations.applications()
            .get(GetApplicationRequest.builder().name(MR_MEESEEKS_APP_NAME).build())
            .subscribe(response -> {
                int currentInstances = response.getInstances();
                log.info("Mr Meeseeks is running [{}] instances", currentInstances);
                int instanceCount = Math.min(3, currentInstances + 1);
                this.cloudFoundryOperations.applications()
                        .scale(ScaleApplicationRequest.builder()
                                .name(MR_MEESEEKS_APP_NAME)
                                .instances(instanceCount)
                                .build()).subscribe((scaleResponse) -> {
                    if (!"started".equalsIgnoreCase(response.getRequestedState())) {
                        log.info("Starting Mr Meeseeks", currentInstances);
                        this.cloudFoundryOperations.applications()
                                .start(StartApplicationRequest.builder()
                                        .name(MR_MEESEEKS_APP_NAME)
                                        .build()).subscribe();
                    }
                });
            }, throwable -> log.error("Could not find Meeseeks application", throwable));
    }

    @Override
    public void kill() {
        this.cloudFoundryOperations.applications()
            .get(GetApplicationRequest.builder().name(MR_MEESEEKS_APP_NAME).build())
            .subscribe(response -> {
                log.info("Scaling Mr Meeseeks to 0 instances...");
                this.cloudFoundryOperations.applications()
                        .scale(ScaleApplicationRequest.builder()
                                .instances(0)
                                .name(MR_MEESEEKS_APP_NAME)
                                .build()).subscribe();
            }, throwable -> log.error("Could not terminate Mr Meeseeks", throwable));
    }
}
