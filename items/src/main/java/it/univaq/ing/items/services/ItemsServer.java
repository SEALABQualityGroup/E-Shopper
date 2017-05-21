package it.univaq.ing.items.services;

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

import it.univaq.ing.items.ItemsWebApplication;
import it.univaq.ing.items.repository.ItemRepository;
import it.univaq.ing.security.CustomCredentialProvider;



/**
 * Run as a micro-service, registering with the Discovery Server (Eureka).
 * <p>
 * Note that the configuration for this application is imported from
 * {@link ItemsWebApplication}. This is a deliberate separation of concerns
 * and allows the application to run:
 * <ul>
 * <li>Standalone - by executing {@link ItemsWebApplication#main(String[])}</li>
 * <li>As a microservice - by executing {@link ItemsServer#main(String[])}</li>
 * </ul>
 * 
 * @author Paul Chapman
 */
@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(ItemsWebApplication.class)
@ComponentScan(basePackages={"it.univaq.ing.items","it.univaq.ing.security"})
public class ItemsServer{

	@Autowired
	protected ItemRepository itemRepository;

	protected Logger logger = Logger.getLogger(ItemsServer.class.getName());

	public static void main(String[] args) {
		
		System.setProperty("spring.config.name", "items-server");
		SpringApplication.run(ItemsServer.class, args);
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
