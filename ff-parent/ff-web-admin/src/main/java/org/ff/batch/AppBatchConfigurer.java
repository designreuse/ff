package org.ff.batch;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@ConditionalOnExpression("#{environment['base.run-batch'] == 'true'}")
@Slf4j
public class AppBatchConfigurer implements BatchConfigurer {

	private final EntityManagerFactory entityManagerFactory;

	private PlatformTransactionManager transactionManager;

	private JobRepository jobRepository;

	private JobLauncher jobLauncher;

	private JobExplorer jobExplorer;

	public AppBatchConfigurer(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public JobRepository getJobRepository() {
		return this.jobRepository;
	}

	@Override
	public PlatformTransactionManager getTransactionManager() {
		return this.transactionManager;
	}

	@Override
	public JobLauncher getJobLauncher() {
		return this.jobLauncher;
	}

	@Override
	public JobExplorer getJobExplorer() throws Exception {
		return this.jobExplorer;
	}

	@PostConstruct
	public void initialize() {
		try {
			log.info("Forcing the use of a JPA transactionManager");
			if (this.entityManagerFactory == null) {
				throw new Exception("Unable to initialize batch configurer : entityManagerFactory must not be null");
			}
			this.transactionManager = new JpaTransactionManager(this.entityManagerFactory);

			log.info("Forcing the use of a Map based JobRepository");
			MapJobRepositoryFactoryBean jobRepositoryFactory = new MapJobRepositoryFactoryBean(this.transactionManager);
			jobRepositoryFactory.afterPropertiesSet();
			this.jobRepository = jobRepositoryFactory.getObject();

			SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
			jobLauncher.setJobRepository(getJobRepository());
			jobLauncher.afterPropertiesSet();
			this.jobLauncher = jobLauncher;

			MapJobExplorerFactoryBean jobExplorerFactory = new MapJobExplorerFactoryBean(jobRepositoryFactory);
			jobExplorerFactory.afterPropertiesSet();
			this.jobExplorer = jobExplorerFactory.getObject();
		} catch (Exception e) {
			throw new IllegalStateException("Unable to initialize Spring Batch", e);
		}
	}

}
