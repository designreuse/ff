package org.ff;

import java.text.Collator;
import java.util.Locale;

import org.ff.base.properties.BaseProperties;
import org.ff.jpa.SpringSecurityAuditorAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@SpringBootApplication
@EnableCaching
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableScheduling
@EnableTransactionManagement
public class Application extends SpringBootServletInitializer {

	@Autowired
	private BaseProperties properties;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	@Bean
	AuditorAware<String> springSecurityAuditorAware() {
		return new SpringSecurityAuditorAware();
	}

	@Bean
	public Collator collator() {
		return Collator.getInstance(new Locale(properties.getColatorLocale()));
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver resolver = new SessionLocaleResolver();
		resolver.setDefaultLocale(Locale.ENGLISH);
		return resolver;
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasenames("i18n/messages");
		source.setUseCodeAsDefaultMessage(true);
		return source;
	}

}
