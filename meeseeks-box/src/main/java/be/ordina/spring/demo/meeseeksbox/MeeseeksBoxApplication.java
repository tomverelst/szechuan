package be.ordina.spring.demo.meeseeksbox;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableBinding(OutputChannels.class)
public class MeeseeksBoxApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(MeeseeksBoxApplication.class, args);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("*")
			.allowedMethods("OPTIONS", "POST", "DELETE");
	}

	@Slf4j
	@RestController
	@RequestMapping("/")
	@AllArgsConstructor
	static class MeeseeksBoxController {

		private final Meeseeks meeseeks;

		@PostMapping
		public void spawnMrMeeseeks() {
			meeseeks.spawn();
		}

		@DeleteMapping
		public void killMeeseekses() {
			meeseeks.kill();
		}
	}

}
