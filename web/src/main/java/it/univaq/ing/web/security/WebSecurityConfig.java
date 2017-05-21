package it.univaq.ing.web.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * 
 * @author LC
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@ComponentScan("it.univaq.ing.security")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomWebAuthenticationProvider customAuthenticationProvider;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers("/css/**/*").permitAll()
        .antMatchers("/js/**/*").permitAll()
        .antMatchers("/images/**/*").permitAll()
        .antMatchers("/fonts/**/*").permitAll()
        .antMatchers("/login").permitAll()
        .antMatchers("/signup").permitAll()
        .antMatchers("/shop").permitAll()
        .antMatchers("/contact").permitAll()
        .antMatchers("/returnLogin").permitAll()
        .antMatchers("/findItemsByCategotyId/*").permitAll()
        .antMatchers("/findItemsRandomByProductId/*").permitAll()
        .antMatchers("/sendMail").permitAll()
        .antMatchers("/forgotPasswordLogin").permitAll()
        .antMatchers("/sendEmailForgotPassword").permitAll()   
        .antMatchers("/findItemsByIdProduct/*").permitAll() 
        .antMatchers("/findItemByProductFilter/*/**").permitAll() 
        .antMatchers("/findItemByCategoryFilter/*/**").permitAll() 
        .antMatchers("/findProductById/*").permitAll() 
        

        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .defaultSuccessUrl("/login",true)
        .and()
        .csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	auth.authenticationProvider(customAuthenticationProvider);
    }
}