package it.univaq.ing.wishlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import it.univaq.ing.wishlist.services.WishListServer;

/**
 * The categories web-application. This class has two uses:
 * <ol>
 * <li>Provide configuration and setup for {@link WishListServer} ... or</li>
 * <li>Run as a stand-alone Spring Boot web-application for testing (in which
 * case there is <i>no</i> microservice registration</li>
 * </ol>
 * <p>
 * To execute as a microservice, run {@link WishListServer} instead.
 * 
 * @author LC
 */
@SpringBootApplication
@EntityScan("it.univaq.ing.wishlist.domain")
@EnableJpaRepositories("it.univaq.ing.wishlist.repository")
@PropertySource("classpath:db-config.properties")
public class WishListWebApplication {

	public static void main(String[] args){
		SpringApplication.run(WishListWebApplication.class, args);
	}
}
