package ru.otus.spring.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import io.changock.migration.api.annotations.ChangeLog;
import lombok.extern.slf4j.Slf4j;
import ru.otus.spring.domain.model.mongo.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ChangeLog(order = "001")
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDB", author = "anton.isaev", runAlways = true)
    public void dropDB(MongoDatabase database) {
        log.info("Drop database {}", database.getName());
        database.drop();
    }

    @ChangeSet(order = "002", id = "insertInitData", author = "anton.isaev")
    public void insertInitData(MongockTemplate mongoTemplate) {
        Genre genre1 = new Genre("genre_name_1");
        Genre genre2 = new Genre("genre_name_2");
        Genre genre3 = new Genre("genre_name_3");
        Genre genre4 = new Genre("genre_name_4");
        Genre genre5 = new Genre("genre_name_5");
        Author author1 = new Author("author_first_name_1", "author_second_name_1");
        Author author2 = new Author("author_first_name_2", "author_second_name_2");
        Author author3 = new Author("author_first_name_3", "author_second_name_3");
        Comment comment1 = new Comment("comment_text_1");
        Comment comment2 = new Comment("comment_text_2");
        List<Comment> comments = List.of(comment1, comment2);

        Book book1 = new Book("book_name_1", author1, genre1, comments);
        Book book2 = new Book("book_name_2", author1, genre2, new ArrayList<>());

        mongoTemplate.save(genre1);
        mongoTemplate.save(genre2);
        mongoTemplate.save(genre3);
        mongoTemplate.save(genre4);
        mongoTemplate.save(genre5);
        mongoTemplate.save(author1);
        mongoTemplate.save(author2);
        mongoTemplate.save(author3);
        mongoTemplate.save(comment1);
        mongoTemplate.save(comment2);
        mongoTemplate.save(book1);
        mongoTemplate.save(book2);
    }
}
