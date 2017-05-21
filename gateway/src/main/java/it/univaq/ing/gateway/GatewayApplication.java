package it.univaq.ing.gateway;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import it.univaq.ing.security.CustomCredentialProvider;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@ComponentScan(basePackages={"it.univaq.ing.gateway","it.univaq.ing.security"})
@PropertySource("classpath:db-config.properties")
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
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
