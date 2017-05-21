package it.univaq.ing.items;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import it.univaq.ing.items.services.ItemsServer;
/**
 * 
 * The items web-application. This class has two uses:
 * <ol>
 * <li>Provide configuration and setup for {@link ItemsServer} ... or</li>
 * <li>Run as a stand-alone Spring Boot web-application for testing (in which
 * case there is <i>no</i> microservice registration</li>
 * </ol>
 * <p>
 * To execute as a microservice, run {@link ItemsServer} instead.
 * 
 * @author LC
 */

@SpringBootApplication
@EntityScan("it.univaq.ing.items.domain")
@EnableJpaRepositories("it.univaq.ing.items.repository")
@PropertySource("classpath:db-config.properties")
public class ItemsWebApplication {

	public static void main(String[] args){
		SpringApplication.run(ItemsWebApplication.class, args);
	}
}
