package it.univaq.ing.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import it.univaq.ing.order.services.OrderServer;

/**
 * The order web-application. This class has two uses:
 * <ol>
 * <li>Provide configuration and setup for {@link OrderServer} ... or</li>
 * <li>Run as a stand-alone Spring Boot web-application for testing (in which
 * case there is <i>no</i> microservice registration</li>
 * </ol>
 * <p>
 * To execute as a microservice, run {@link OrderServer} instead.
 * 
 * @author LC
 */
@SpringBootApplication
@EntityScan("it.univaq.ing.order.domain")
@EnableJpaRepositories("it.univaq.ing.order.repository")
@PropertySource("classpath:db-config.properties")
public class OrderWebApplication {

	public static void main(String[] args){
		SpringApplication.run(OrderWebApplication.class, args);
	}
}
