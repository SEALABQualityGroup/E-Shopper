package it.univaq.ing.accounts;



import java.net.SocketException;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import it.univaq.ing.accounts.services.AccountsServer;


/**
 * The accounts web-application. This class has two uses:
 * <ol>
 * <li>Provide configuration and setup for {@link AccountsServer} ... or</li>
 * <li>Run as a stand-alone Spring Boot web-application for testing (in which
 * case there is <i>no</i> microservice registration</li>
 * </ol>
 * <p>
 * To execute as a microservice, run {@link AccountsServer} instead.
 * 
 * @author LC
 */
@SpringBootApplication
@EntityScan("it.univaq.ing.accounts.domain")
@EnableJpaRepositories("it.univaq.ing.accounts.repository")
@PropertySource("classpath:db-config.properties")
public class AccountsWebApplication {

	protected static Logger logger = Logger.getLogger(AccountsWebApplication.class.getName());

	public static void main(String[] args) throws SocketException {
		SpringApplication.run(AccountsWebApplication.class, args);
	}
}
