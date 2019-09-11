package it.univaq.ing.web.services;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import it.univaq.ing.security.CustomCredentialProvider;
import it.univaq.ing.web.controller.HomeController;
import it.univaq.ing.web.controller.WebAccountsController;
import it.univaq.ing.web.controller.WebCartController;
import it.univaq.ing.web.controller.WebCategoriesController;
import it.univaq.ing.web.controller.WebItemsController;
import it.univaq.ing.web.controller.WebLoginController;
import it.univaq.ing.web.controller.WebOrderController;
import it.univaq.ing.web.controller.WebProductsController;
import it.univaq.ing.web.controller.WebWishListController;

/**
 * Accounts web-server. Works as a microservice client, fetching data from the
 * Account-Service. Uses the Discovery Server (Eureka) to find the microservice.
 * 
 * @author LC
 */
@SpringBootApplication
@EnableDiscoveryClient
// Disable component scanner ...
@ComponentScan(basePackages={"it.univaq.ing.web.security","it.univaq.ing.security"})
@EnableAutoConfiguration
@EnableAsync
public class WebServer {

	/**
	 * URL uses the logical name of account-service - upper or lower case,
	 * doesn't matter.
	 */
//	public static final String ACCOUNTS_SERVICE_URL = "http://ACCOUNTS-SERVER";
//	
//	public static final String ORDER_SERVICE_URL = "http://ORDER-SERVER";
//	
//	public static final String PRODUCTS_SERVICE_URL = "http://PRODUCTS-SERVER";
//	
//	public static final String CATEGORIES_SERVICE_URL = "http://CATEGORIES-SERVER";
//	
//	public static final String ITEMS_SERVICE_URL = "http://ITEMS-SERVER";
//
//	private static final String LOGIN_SERVICE_URL = "http://LOGIN-SERVER";
//	
//	private static final String CART_SERVICE_URL = "http://CART-SERVER";
//	
//	private static final String WISH_LIST_SERVICE_URL = "http://WISHLIST-SERVER";

	public static final String ACCOUNTS_SERVICE_URL = "http://GATEWAY/accounts";
	
	public static final String ORDER_SERVICE_URL = "http://GATEWAY/order";
	
	public static final String PRODUCTS_SERVICE_URL = "http://GATEWAY/products";
		
	public static final String CATEGORIES_SERVICE_URL = "http://GATEWAY/categories";
	
	public static final String ITEMS_SERVICE_URL = "http://GATEWAY/items";

	private static final String LOGIN_SERVICE_URL = "http://GATEWAY/login";
	
	private static final String CART_SERVICE_URL = "http://GATEWAY/cart";
	
	private static final String WISH_LIST_SERVICE_URL = "http://GATEWAY/wishlist";
	/**
	 * Run the application using Spring Boot and an embedded servlet engine.
	 * 
	 * @param args
	 *            Program arguments - ignored.
	 */
	public static void main(String[] args) {
		System.setProperty("spring.config.name", "web-server");
		SpringApplication.run(WebServer.class, args);
	}

	/**
	 * The AccountsService encapsulates the interaction with the micro-service.
	 * 
	 * @return A new service instance.
	 */	
	@Bean
	public WebAccountsService accountsService() {
		return new WebAccountsService(ACCOUNTS_SERVICE_URL);
	}

	/**
	 * Create the controller, passing it the {@link WebAccountsService} to use.
	 * 
	 * @return
	 */
	@Bean
	public WebAccountsController accountsController() {
		return new WebAccountsController(accountsService());
	}
	
	/**
	 * The OrderService encapsulates the interaction with the micro-service.
	 * 
	 * @return A new service instance.
	 */	
	@Bean
	public WebOrderService orderService() {
		return new WebOrderService(ORDER_SERVICE_URL);
	}

	/**
	 * Create the controller, passing it the {@link WebOrderService} to use.
	 * 
	 * @return
	 */
	@Bean
	public WebOrderController orderController() {
		return new WebOrderController(orderService());
	}
	
	/**
	 * The CartService encapsulates the interaction with the micro-service.
	 * 
	 * @return A new service instance.
	 */	
	@Bean
	public WebCartService cartService() {
		return new WebCartService(CART_SERVICE_URL);
	}

	/**
	 * Create the controller, passing it the {@link WebCartService} to use.
	 * 
	 * @return
	 */
	@Bean
	public WebCartController cartController() {
		return new WebCartController(cartService());
	}

	@Bean
	public HomeController homeController() {
		return new HomeController();
	}
	
	/**
	 * The ProductsService encapsulates the interaction with the micro-service.
	 * 
	 * @return A new service instance.
	 */
	@Bean
	public WebProductsService productsService() {
		return new WebProductsService(PRODUCTS_SERVICE_URL);
	}

	/**
	 * Create the controller, passing it the {@link WebProductsController} to use.
	 * 
	 * @return
	 */
	@Bean
	public WebProductsController productsController() {
		return new WebProductsController(productsService());
	}
	
	/**
	 * The CategoriesService encapsulates the interaction with the micro-service.
	 * 
	 * @return A new service instance.
	 */
	@Bean
	public WebCategoriesService categoriesService() {
		return new WebCategoriesService(CATEGORIES_SERVICE_URL);
	}

	/**
	 * Create the controller, passing it the {@link WebCategoriesService} to use.
	 * 
	 * @return
	 */
	@Bean
	public WebCategoriesController categoriesController() {
		return new WebCategoriesController(categoriesService());
	}
	
	/**
	 * The ItemsService encapsulates the interaction with the micro-service.
	 * 
	 * @return A new service instance.
	 */
	@Bean
	public WebItemsService itemsService() {
		return new WebItemsService(ITEMS_SERVICE_URL);
	}

	/**
	 * Create the controller, passing it the {@link WebItemsService} to use.
	 * 
	 * @return
	 */
	@Bean
	public WebItemsController itemsController() {
		return new WebItemsController(itemsService());
	}
	
	/**LoginService encapsulates the interaction with the micro-service.
	 * 
	 * @return A new service instance.
	 */
	@Bean
	public WebLoginService loginService() {
		return new WebLoginService(LOGIN_SERVICE_URL);
	}

	/**
	 * Create the controller, passing it the {@link WebLoginService} to use.
	 * 
	 * @return
	 */
	@Bean
	public WebLoginController loginController() {
		return new WebLoginController(loginService());
	}
	
	/**
	 * The WishListService encapsulates the interaction with the micro-service.
	 * 
	 * @return A new service instance.
	 */
	@Bean
	public WebWishListService wishListService() {
		return new WebWishListService(WISH_LIST_SERVICE_URL);
	}

	/**
	 * Create the controller, passing it the {@link WebWishListController} to use.
	 * 
	 * @return
	 */
	@Bean
	public WebWishListController wishListController() {
		return new WebWishListController(wishListService());
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