package it.univaq.ing.order.services;

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

import it.univaq.ing.order.OrderWebApplication;
import it.univaq.ing.order.repository.OrderRepository;
import it.univaq.ing.security.CustomCredentialProvider;


/**
 * Run as a micro-service, registering with the Discovery Server (Eureka).
 * <p>
 * Note that the configuration for this application is imported from
 * {@link OrderWebApplication}. This is a deliberate separation of concerns
 * and allows the application to run:
 * <ul>
 * <li>Standalone - by executing {@link OrderWebApplication#main(String[])}
 * </li>
 * <li>As a microservice - by executing {@link OrderServer#main(String[])}</li>
 * </ul>
 * 
 * @author LC
 */
@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(OrderWebApplication.class)
@ComponentScan(basePackages={"it.univaq.ing.order","it.univaq.ing.security"})
public class OrderServer{

	@Autowired
	protected OrderRepository orderRepository;
	
	
	protected Logger logger = Logger.getLogger(OrderServer.class.getName());

	public static void main(String[] args) {

		System.setProperty("spring.config.name", "order-server");
		SpringApplication.run(OrderServer.class, args);
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
