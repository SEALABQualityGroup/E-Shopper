package it.univaq.ing.login;



import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The accounts web-application. This class has two uses:
 * <ol>
 * <li>Provide configuration and setup for {@link LoginServer} ... or</li>
 * <li>Run as a stand-alone Spring Boot web-application for testing (in which
 * case there is <i>no</i> microservice registration</li>
 * </ol>
 * <p>
 * To execute as a microservice, run {@link LoginServer} instead.
 * 
 * @author LC
 */
@SpringBootApplication
@EntityScan("it.univaq.ing.login.domain")
@EnableJpaRepositories("it.univaq.ing.login.repository")
@PropertySource("classpath:db-config.properties")
public class LoginWebApplication {

	protected Logger logger = Logger.getLogger(LoginWebApplication.class
			.getName());

	public static void main(String[] args) {
		SpringApplication.run(LoginWebApplication.class, args);
	}
}
