package it.univaq.ing.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import it.univaq.ing.products.services.ProductsServer;

/**
 * 
 * The products web-application. This class has two uses:
 * <ol>
 * <li>Provide configuration and setup for {@link ProductsServer} ... or</li>
 * <li>Run as a stand-alone Spring Boot web-application for testing (in which
 * case there is <i>no</i> microservice registration</li>
 * </ol>
 * <p>
 * To execute as a microservice, run {@link ProductsServer} instead.
 * @author laura-pc
 *
 */
@SpringBootApplication
@EntityScan("it.univaq.ing.products.domain")
@EnableJpaRepositories("it.univaq.ing.products.repository")
@PropertySource("classpath:db-config.properties")
public class ProductsWebApplication {

	public static void main(String[] args){
		SpringApplication.run(ProductsWebApplication.class, args);
	}
}