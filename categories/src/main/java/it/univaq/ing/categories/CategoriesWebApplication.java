package it.univaq.ing.categories;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import it.univaq.ing.categories.services.CategoriesServer;

/**
 * The categories web-application. This class has two uses:
 * <ol>
 * <li>Provide configuration and setup for {@link CategoriesServer} ... or</li>
 * <li>Run as a stand-alone Spring Boot web-application for testing (in which
 * case there is <i>no</i> microservice registration</li>
 * </ol>
 * <p>
 * To execute as a microservice, run {@link CategoriesServer} instead.
 * 
 * @author LC
 */
@SpringBootApplication
@EntityScan("it.univaq.ing.categories.domain")
@EnableJpaRepositories("it.univaq.ing.categories.repository")
@PropertySource("classpath:db-config.properties")
//@EnableFeignClients
//@Configuration
public class CategoriesWebApplication {

	public static void main(String[] args){
		SpringApplication.run(CategoriesWebApplication.class, args);
	}
}
