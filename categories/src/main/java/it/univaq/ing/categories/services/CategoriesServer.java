package it.univaq.ing.categories.services;

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

import it.univaq.ing.categories.CategoriesWebApplication;
import it.univaq.ing.categories.repository.CategoryRepository;
import it.univaq.ing.security.CustomCredentialProvider;



/**
 * Run as a micro-service, registering with the Discovery Server (Eureka).
 * <p>
 * Note that the configuration for this application is imported from
 * {@link CategoriesWebApplication}. This is a deliberate separation of concerns
 * and allows the application to run:
 * <ul>
 * <li>Standalone - by executing {@link CategoriesWebApplication#main(String[])}</li>
 * <li>As a microservice - by executing {@link CategoriesServer#main(String[])}</li>
 * </ul>
 * 
 * @author Paul Chapman
 */
@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(CategoriesWebApplication.class)
@ComponentScan(basePackages={"it.univaq.ing.categories","it.univaq.ing.security"})
public class CategoriesServer{

	@Autowired
	protected CategoryRepository categoryRepository;

	protected Logger logger = Logger.getLogger(CategoriesServer.class.getName());


	public static void main(String[] args) {
		
		System.setProperty("spring.config.name", "categories-server");
		SpringApplication.run(CategoriesServer.class, args);
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
