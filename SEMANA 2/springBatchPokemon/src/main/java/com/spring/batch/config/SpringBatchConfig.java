package com.spring.batch.config;

import com.spring.batch.entity.Pokemon;
import com.spring.batch.entity.PokemonMongo;
import com.spring.batch.repository.PokemonRepository;
import com.spring.batch.repository.PokemonMongoRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.data.domain.Sort;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private PokemonRepository pokemonRepository;

    private PokemonMongoRepository pokemonMongoRepository;


    @Bean
    public FlatFileItemReader<Pokemon> reader() {
        FlatFileItemReader<Pokemon> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/Pokemon.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<Pokemon> lineMapper() {
        DefaultLineMapper<Pokemon> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false); //cambio a true
        lineTokenizer.setNames("id", "name", "type1", "type2", "total", "hp", "attack", "defense", "spAtk", "spDef", "speed", "generation"
        );

        BeanWrapperFieldSetMapper<Pokemon> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Pokemon.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }

    @Bean
    public PokemonProcessor processor() {
        return new PokemonProcessor();
    }

    @Bean
    public TypeDomainProcessor pokeNameProcessor() {
        return new TypeDomainProcessor();
    }

    @Bean
    public RepositoryItemWriter<Pokemon> writer() {
        RepositoryItemWriter<Pokemon> writer = new RepositoryItemWriter<>();
        writer.setRepository(pokemonRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public RepositoryItemReader<Pokemon> databaseReader() {
        RepositoryItemReader<Pokemon> reader = new RepositoryItemReader<>();
        reader.setRepository(pokemonRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(10);

        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        reader.setSort(sorts);

        return reader;
    }

    @Bean
    public RepositoryItemWriter<PokemonMongo> mongoWriter() {
        RepositoryItemWriter<PokemonMongo> writer = new RepositoryItemWriter<>();
        writer.setRepository(pokemonMongoRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("csv-step").<Pokemon, Pokemon>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("second-step").<Pokemon, PokemonMongo>chunk(10)
                .reader(databaseReader())
                .processor(pokeNameProcessor())
                .writer(mongoWriter())
                .build();
    }

    @Bean
    public Job runJob() {
        return jobBuilderFactory.get("importPokemon")
                .flow(step1())
                .next(step2())
                .end().build();

    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }

}
