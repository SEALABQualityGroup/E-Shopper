package it.univaq.ing.accounts.services;

import java.net.SocketException;
import java.util.logging.Logger;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import it.univaq.ing.accounts.AccountsWebApplication;
import it.univaq.ing.accounts.repository.AccountRepository;
import it.univaq.ing.security.CustomCredentialProvider;



/**
 * Run as a micro-service, registering with the Discovery Server (Eureka).
 * <p>
 * Note that the configuration for this application is imported from
 * {@link AccountsWebApplication}. This is a deliberate separation of concerns
 * and allows the application to run:
 * <ul>
 * <li>Standalone - by executing {@link AccountsWebApplication#main(String[])}</li>
 * <li>As a microservice - by executing {@link AccountsServer#main(String[])}</li>
 * </ul>
 * 
 * @author LC
 */
@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(AccountsWebApplication.class)
@ComponentScan(basePackages={"it.univaq.ing.accounts","it.univaq.ing.security"})
public class AccountsServer{

	@Autowired
	protected AccountRepository accountRepository;

	protected static Logger logger = Logger.getLogger(AccountsServer.class.getName());

	/**
	 * Run the application using Spring Boot and an embedded servlet engine.
	 * 
	 * @param args
	 *            Program arguments - ignored.
	 * @throws SocketException 
	 */
	public static void main(String[] args) throws SocketException {
		System.setProperty("spring.config.name", "accounts-server");
		SpringApplication.run(AccountsServer.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.additionalCustomizers(new AuthCustomizer())
				.build();
	}
	
	static class AuthCustomizer implements RestTemplateCustomizer {

		@Override
		public void customize(RestTemplate restTemplate) {
			HttpClient client = HttpClientBuilder.create()
					.setDefaultCredentialsProvider(new CustomCredentialProvider()).build();

			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
		}
	}
}
