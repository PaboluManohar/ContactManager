package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class Myconfig  {
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService userDetailsService()
	{
		return new UserDetailsServiceImpl();
	}
	
    @Bean
public DaoAuthenticationProvider authenticationProvider()
 
{
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());

        
return provider;
    }
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((auth) ->
                        auth
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/user/**").hasRole("USER")
                                .requestMatchers("/**").permitAll()
                )
                .formLogin(lf-> lf.loginPage("/signin")
//                		.loginProcessingUrl("/doLogin")
                		.defaultSuccessUrl("/user/index")
                		)
                .authenticationProvider(authenticationProvider());
		
				return http.build();
	}

}


























































/*  
 * 
 * _______________formLogin__________________
 * 
 *
 * default login pagewill be generated. Example Configurations The most basic
 * configuration defaults to automatically generating a login page atthe URL
 * "/login", redirecting to "/login?error" for authentication failure.
 * Thedetails of the login page can be found on
 * FormLoginConfigurer.loginPage(String) @Configuration
 * 
 * @EnableWebSecurity public class FormLoginSecurityConfig {
 * 
 * @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http)
 * throws Exception { http .authorizeRequests((authorizeRequests) ->
 * authorizeRequests .requestMatchers("/**").hasRole("USER") )
 * .formLogin(withDefaults()); return http.build(); }
 * 
 * @Bean public UserDetailsService userDetailsService() { UserDetails user =
 * User.withDefaultPasswordEncoder() .username("user") .password("password")
 * .roles("USER") .build(); return new InMemoryUserDetailsManager(user); } }
 * 
 * The configuration below demonstrates customizing the defaults. @Configuration
 * 
 * @EnableWebSecurity public class FormLoginSecurityConfig {
 * 
 * @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http)
 * throws Exception { http .authorizeRequests((authorizeRequests) ->
 * authorizeRequests .requestMatchers("/**").hasRole("USER") )
 * .formLogin((formLogin) -> formLogin .usernameParameter("username")
 * .passwordParameter("password") .loginPage("/authentication/login")
 * .failureUrl("/authentication/login?failed")
 * .loginProcessingUrl("/authentication/login/process") ); return http.build();
 * }
 * 
 * @Bean public UserDetailsService userDetailsService() { UserDetails user =
 * User.withDefaultPasswordEncoder() .username("user") .password("password")
 * .roles("USER") .build(); return new InMemoryUserDetailsManager(user); } }
 */
