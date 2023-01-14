package ru.otus.spring.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.domain.model.h2.AuthorDao;
import ru.otus.spring.domain.model.h2.BookDao;
import ru.otus.spring.domain.model.h2.CommentDao;
import ru.otus.spring.domain.model.h2.GenreDao;
import ru.otus.spring.domain.model.mongo.Author;
import ru.otus.spring.domain.model.mongo.Book;
import ru.otus.spring.domain.model.mongo.Comment;
import ru.otus.spring.domain.model.mongo.Genre;
import ru.otus.spring.service.ProcessorService;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final MongoTemplate mongoTemplate;
    private final ProcessorService processorService;

    public static final String MIGRATION_JOB_NAME = "MigrationFromMongoToH2";
    private final static Integer CHUNK_SIZE = 5;

    @StepScope
    @Bean
    public MongoItemReader<Genre> genreMongoItemReader() {
        var reader = new MongoItemReader<Genre>();
        reader.setTemplate(mongoTemplate);
        reader.setQuery("{}");
        reader.setSort(Map.of("name", Sort.Direction.ASC));
        reader.setTargetType(Genre.class);
        return reader;
    }

    @StepScope
    @Bean
    public MongoItemReader<Author> authorMongoItemReader() {
        var reader = new MongoItemReader<Author>();
        reader.setTemplate(mongoTemplate);
        reader.setQuery("{}");
        reader.setSort(Map.of("firstName", Sort.Direction.ASC));
        reader.setTargetType(Author.class);
        return reader;
    }

    @StepScope
    @Bean
    public MongoItemReader<Comment> commentMongoItemReader() {
        var reader = new MongoItemReader<Comment>();
        reader.setTemplate(mongoTemplate);
        reader.setQuery("{}");
        reader.setSort(Map.of("text", Sort.Direction.ASC));
        reader.setTargetType(Comment.class);
        return reader;
    }

    @StepScope
    @Bean
    public MongoItemReader<Book> bookMongoItemReader() {
        var reader = new MongoItemReader<Book>();
        reader.setTemplate(mongoTemplate);
        reader.setQuery("{}");
        reader.setSort(Map.of("name", Sort.Direction.ASC));
        reader.setTargetType(Book.class);
        return reader;
    }

    @StepScope
    @Bean
    public ItemProcessor<Genre, GenreDao> genreItemProcessor(ProcessorService service) {
        return service::processGenre;
    }

    @StepScope
    @Bean
    public ItemProcessor<Author, AuthorDao> authorItemProcessor(ProcessorService service) {
        return service::processAuthor;
    }

    @StepScope
    @Bean
    public ItemProcessor<Book, BookDao> bookItemProcessor(ProcessorService service) {
        return service::processBook;
    }

    @StepScope
    @Bean
    public ItemProcessor<Comment, CommentDao> commentItemProcessor(ProcessorService service) {
        return service::processComment;
    }

    @StepScope
    @Bean
    public ItemWriter<GenreDao> genreItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<GenreDao>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO genres (id, name) VALUES (:id, :name)")
                .dataSource(dataSource)
                .build();
    }

    @StepScope
    @Bean
    public ItemWriter<AuthorDao> authorItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<AuthorDao>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO authors (id, first_name, second_name) VALUES (:id, :firstName, :secondName)")
                .dataSource(dataSource)
                .build();
    }

    @StepScope
    @Bean
    public ItemWriter<BookDao> bookItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<BookDao>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO books (id, name, genre_id, author_id) VALUES (:id, :name, :genreId, :authorId)")
                .dataSource(dataSource)
                .build();
    }

    @StepScope
    @Bean
    public ItemWriter<CommentDao> commentItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CommentDao>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO comments (id, text, book_id) VALUES (:id, :text, :bookId)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step importGenres() {
        return stepBuilderFactory.get("importGenres")
                .<Genre, GenreDao>chunk(CHUNK_SIZE)
                .reader(genreMongoItemReader())
                .processor(genreItemProcessor(processorService))
                .writer(genreItemWriter(dataSource))
                .listener(new ItemReadListener() {
                    public void beforeRead() {
                        log.info("Начало чтения жанра");
                    }

                    public void afterRead(Object o) {
                        log.info("Конец чтения жанра: %s", ((Genre) o).getName());
                    }

                    public void onReadError(Exception e) {
                        log.info("Ошибка чтения жанра");
                    }
                })
                .build();
    }

    @Bean
    public Step importAuthors() {
        return stepBuilderFactory.get("importAuthors")
                .<Author, AuthorDao>chunk(CHUNK_SIZE)
                .reader(authorMongoItemReader())
                .processor(authorItemProcessor(processorService))
                .writer(authorItemWriter(dataSource))
                .listener(new ItemReadListener() {
                    public void beforeRead() {
                        log.info("Начало чтения автора");
                    }

                    public void afterRead(Object o) {
                        log.info("Конец чтения автора: %s",
                                ((Author) o).getSecondName() + " " + ((Author) o).getFirstName());
                    }

                    public void onReadError(Exception e) {
                        log.info("Ошибка чтения автора");
                    }
                })
                .build();
    }

    @Bean
    public Step importBooks() {
        return stepBuilderFactory.get("importBooks")
                .<Book, BookDao>chunk(CHUNK_SIZE)
                .reader(bookMongoItemReader())
                .processor(bookItemProcessor(processorService))
                .writer(bookItemWriter(dataSource))
                .listener(new ItemReadListener() {
                    public void beforeRead() {
                        log.info("Начало чтения книги");
                    }

                    public void afterRead(Object o) {
                        log.info("Конец чтения книги: %s", ((Book) o).getName());
                    }

                    public void onReadError(Exception e) {
                        log.info("Ошибка чтения книги");
                    }
                })
                .build();
    }

    @Bean
    public Step importComments() {
        return stepBuilderFactory.get("importComments")
                .<Comment, CommentDao>chunk(CHUNK_SIZE)
                .reader(commentMongoItemReader())
                .processor(commentItemProcessor(processorService))
                .writer(commentItemWriter(dataSource))
                .listener(new ItemReadListener() {
                    public void beforeRead() {
                        log.info("Начало чтения комментария");
                    }

                    public void afterRead(Object o) {
                        log.info("Конец чтения комментария: %s", ((Comment) o).getText());
                    }

                    public void onReadError(Exception e) {
                        log.info("Ошибка чтения комментария");
                    }
                })
                .build();
    }

    @Bean
    public Job migrateMongoToH2() {
        return jobBuilderFactory.get(MIGRATION_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(importGenres())
                .next(importAuthors())
                .next(importBooks())
                .next(importComments())
                .listener(new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                log.info("Начало работы");
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                log.info("Конец работы");
            }
        })
                .build();
    }
}
