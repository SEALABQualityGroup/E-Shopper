package it.univaq.ing.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import it.univaq.ing.cart.services.CartServer;

/**
 * The cart web-application. This class has two uses:
 * <ol>
 * <li>Provide configuration and setup for {@link CartServer} ... or</li>
 * <li>Run as a stand-alone Spring Boot web-application for testing (in which
 * case there is <i>no</i> microservice registration</li>
 * </ol>
 * <p>
 * To execute as a microservice, run {@link CartServer} instead.
 * 
 * @author LC
 */
@SpringBootApplication
@EntityScan("it.univaq.ing.cart.domain")
@EnableJpaRepositories("it.univaq.ing.cart.repository")
@PropertySource("classpath:db-config.properties")
public class CartWebApplication {

	public static void main(String[] args){
		SpringApplication.run(CartWebApplication.class, args);
	}
}
