package org.ff.security;

import org.ff.common.security.AppAuthenticationEntryPoint;
import org.ff.common.security.AppAuthenticationResultHandler;
import org.ff.common.security.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration {

	@Configuration
	@Order(1)
	public static class ApiConfigurationAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		private AppUserDetailsService userDetailsService;

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService).passwordEncoder(new ShaPasswordEncoder());
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			log.info("Configuring API security...");
			http.csrf().disable();
			http.antMatcher("/e/api/**").authorizeRequests().anyRequest().authenticated();
			http.httpBasic();
			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		}

	}

	@Configuration
	@Order(2)
	public static class WebConfigurationAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		private AppUserDetailsService userDetailsService;

		@Autowired
		private AppAuthenticationEntryPoint authenticationEntryPoint;

		@Autowired
		private AppAuthenticationResultHandler authenticationResultHandler;

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService).passwordEncoder(new ShaPasswordEncoder());
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			log.info("Configuring WEB security...");
			http.csrf().disable();
			http.authorizeRequests().antMatchers("/styles/**", "/external/**", "/webjars/**", "/inspinia/**", "/login**", "/api/v1/users/**").permitAll();
			http.authorizeRequests().anyRequest().fullyAuthenticated();
			http.formLogin().loginPage("/login.html").permitAll().successHandler(authenticationResultHandler).failureHandler(authenticationResultHandler);
			http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login.html").permitAll();
			http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
		}

	}

}
