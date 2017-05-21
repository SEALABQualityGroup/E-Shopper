package it.univaq.ing.cart.services;

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

import it.univaq.ing.cart.CartWebApplication;
import it.univaq.ing.cart.repository.CartRepository;
import it.univaq.ing.security.CustomCredentialProvider;


/**
 * Run as a micro-service, registering with the Discovery Server (Eureka).
 * <p>
 * Note that the configuration for this application is imported from
 * {@link CartWebApplication}. This is a deliberate separation of concerns
 * and allows the application to run:
 * <ul>
 * <li>Standalone - by executing {@link CartWebApplication#main(String[])}</li>
 * <li>As a microservice - by executing {@link CartServer#main(String[])}</li>
 * </ul>
 * 
 * @author LC
 */
@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(CartWebApplication.class)
@ComponentScan(basePackages={"it.univaq.ing.cart","it.univaq.ing.security"})
public class CartServer{

	@Autowired
	protected CartRepository cartRepository;

	protected static Logger logger = Logger.getLogger(CartServer.class.getName());


	public static void main(String[] args) throws SocketException {

		System.setProperty("spring.config.name", "cart-server");
		SpringApplication.run(CartServer.class, args);
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
