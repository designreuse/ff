package org.ff.batch;

import javax.persistence.EntityManagerFactory;

import org.ff.batch.city.CityItem;
import org.ff.batch.city.CityItemFieldSetMapper;
import org.ff.batch.city.CityItemProcessor;
import org.ff.batch.nkd.NkdItem;
import org.ff.batch.nkd.NkdItemFieldSetMapper;
import org.ff.batch.nkd.NkdItemProcessor;
import org.ff.jpa.domain.City;
import org.ff.jpa.domain.Nkd;
import org.ff.jpa.repository.CityRepository;
import org.ff.jpa.repository.NkdRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private NkdRepository nkdRepository;

	@Bean
	public BatchConfigurer configurer(EntityManagerFactory entityManagerFactory) {
		return new AppBatchConfigurer(entityManagerFactory);
	}

	@Bean
	public Job batchJob() {
		return jobBuilderFactory.get("batchJob")
				.incrementer(new RunIdIncrementer())
				.listener(new JobExecutionListener() {
					@Override
					public void beforeJob(JobExecution arg0) {
						log.debug("Before batch job");
					}
					@Override
					public void afterJob(JobExecution arg0) {
						log.debug("After batch job");
					}
				})
				.flow(cityStep()).next(nkdStep())
				.end()
				.build();
	}

	@Bean
	public Step cityStep() {
		return stepBuilderFactory.get("cityStep")
				.<CityItem, City> chunk(1)
				.reader(cityReader())
				.processor(cityProcessor())
				.writer(cityWriter())
				.build();
	}

	@Bean
	public ItemReader<CityItem> cityReader() {
		FlatFileItemReader<CityItem> reader = new FlatFileItemReader<CityItem>();
		reader.setResource(new ClassPathResource("csv/cities.csv"));
		reader.setLinesToSkip(1);
		reader.setLineMapper(cityLineMapper());
		return reader;
	}

	@Bean
	public LineMapper<CityItem> cityLineMapper() {
		DefaultLineMapper<CityItem> lineMapper = new DefaultLineMapper<CityItem>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(true);
		lineTokenizer.setNames(new String[] { "id", "city", "zupanija", "skupina" });
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(cityFieldSetMapper());

		return lineMapper;
	}

	@Bean
	public CityItemFieldSetMapper cityFieldSetMapper() {
		return new CityItemFieldSetMapper();
	}

	@Bean
	public ItemProcessor<CityItem, City> cityProcessor() {
		return new CityItemProcessor();
	}

	@Bean
	public ItemWriter<City> cityWriter() {
		RepositoryItemWriter<City> itemWriter = new RepositoryItemWriter<>();
		itemWriter.setRepository(cityRepository);
		itemWriter.setMethodName("save");
		return itemWriter;
	}

	@Bean
	public Step nkdStep() {
		return stepBuilderFactory.get("nkdStep")
				.<NkdItem, Nkd> chunk(100)
				.reader(nkdReader())
				.processor(nkdProcessor())
				.writer(nkdWriter())
				.build();
	}

	@Bean
	public ItemReader<NkdItem> nkdReader() {
		FlatFileItemReader<NkdItem> reader = new FlatFileItemReader<NkdItem>();
		reader.setResource(new ClassPathResource("csv/nkd.csv"));
		reader.setLinesToSkip(1);
		reader.setLineMapper(nkdLineMapper());
		return reader;
	}

	@Bean
	public LineMapper<NkdItem> nkdLineMapper() {
		DefaultLineMapper<NkdItem> lineMapper = new DefaultLineMapper<NkdItem>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(true);
		lineTokenizer.setNames(new String[] { "id", "sektor", "naziv_sektora", "podrucje", "djelatnost", "naziv_djelatnosti" });
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(nkdFieldSetMapper());

		return lineMapper;
	}

	@Bean
	public NkdItemFieldSetMapper nkdFieldSetMapper() {
		return new NkdItemFieldSetMapper();
	}

	@Bean
	public ItemProcessor<NkdItem, Nkd> nkdProcessor() {
		return new NkdItemProcessor();
	}

	@Bean
	public ItemWriter<Nkd> nkdWriter() {
		RepositoryItemWriter<Nkd> itemWriter = new RepositoryItemWriter<>();
		itemWriter.setRepository(nkdRepository);
		itemWriter.setMethodName("save");
		return itemWriter;
	}

}
