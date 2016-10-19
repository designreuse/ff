//package org.ff;
//
//import javax.annotation.Resource;
//import javax.sql.DataSource;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.Profile;
//import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
//
//@Configuration
//@Profile(value = { "prod-mysql", "prod-mssql" } )
//public class WebSphereDevJPADatabaseConfiguration {
//
//	private static final String JNDI_NAME = "jdbc/FundFinderDS";
//
//	@Bean
//	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
//		return new PropertySourcesPlaceholderConfigurer();
//	}
//
//	@Bean
//	@Primary
//	@Resource(name = JNDI_NAME)
//	public DataSource dataSource() {
//		JndiDataSourceLookup jndiDataSourceLookup = new JndiDataSourceLookup();
//		jndiDataSourceLookup.setResourceRef(true);
//		return jndiDataSourceLookup.getDataSource(JNDI_NAME);
//	}
//
//	@Bean
//	public JdbcTemplate jdbcTemplate() {
//		return new JdbcTemplate(dataSource());
//	}
//
//}
