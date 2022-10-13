package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations jdbc;

    private final static String UNKNOWN = "UNKNOWN";

    @Override
    public long insert(Book book) {
        var kh = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource()
                .addValue("name", book.getName())
                .addValue("author_firstname", book.getAuthor().getFirstName())
                .addValue("author_secondname", book.getAuthor().getSecondName())
                .addValue("genre_name", book.getGenre().getName());
        jdbc.update("insert into books (name, author_id, genre_id) " +
                " values (:name, " +
                " (select id from authors where first_name = :author_firstname and second_name = :author_secondname), " +
                " (select id from genres where name = :genre_name))", params, kh);
        return kh.getKey().longValue();
    }

    @Override
    public long insert(String bookName) {
        var kh = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource()
                .addValue("name", bookName)
                .addValue("author_firstname", UNKNOWN)
                .addValue("author_secondname", UNKNOWN)
                .addValue("genre_name", UNKNOWN);
        jdbc.update("insert into books (name, author_id, genre_id) " +
                " values (:name, " +
                " (select id from authors where first_name = :author_firstname and second_name = :author_secondname), " +
                " (select id from genres where name = :genre_name))", params, kh);
        return kh.getKey().longValue();
    }

    @Override
    public int update(Book book) {
        var params = new MapSqlParameterSource();
        params.addValue("id", book.getId());
        params.addValue("name", book.getName());
        return jdbc.update("update books set name = :name where id = :id", params);
    }

    @Override
    public int updateBookAuthor(long bookId, long authorId) {
        var params = new MapSqlParameterSource();
        params.addValue("id", bookId);
        params.addValue("author_id", authorId);
        return jdbc.update("update books set author_id = :author_id where id = :id", params);
    }

    @Override
    public int updateBookGenre(long bookId, long genreId) {
        var params = new MapSqlParameterSource();
        params.addValue("id", bookId);
        params.addValue("genre_id", genreId);
        return jdbc.update("update books set genre_id = :genre_id where id = :id", params);
    }

    @Override
    public Book getById(long id) {
        return jdbc.queryForObject(
                "select b.id id, b.name name, b.author_id author_id, " +
                        " a.first_name author_firstname, a.second_name author_secondname," +
                        " g.id genre_id, g.name genre_name " +
                        " from books b, authors a, genres g " +
                        " where b.id = :id and b.author_id = a.id and b.genre_id = g.id",
                new MapSqlParameterSource("id", id), new BookMapper()
        );
    }

    @Override
    public Book getByName(String name) {
        return jdbc.queryForObject(
                "select b.id id, b.name name, b.author_id author_id, " +
                        " a.first_name author_firstname, a.second_name author_secondname, " +
                        " g.id genre_id, g.name genre_name " +
                        " from books b, authors a, genres g " +
                        " where b.author_id = a.id and b.genre_id = g.id and b.name like :name ",
                new MapSqlParameterSource("name", name), new BookMapper()
        );
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("select b.id id, b.name name, b.author_id author_id, " +
                " a.first_name author_firstname, a.second_name author_secondname, " +
                " g.id genre_id, g.name genre_name " +
                " from books b, authors a, genres g " +
                " where b.author_id = a.id and b.genre_id = g.id ", new BookMapper());
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from books where id = :id", new MapSqlParameterSource("id", id));
    }

    private static class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            long authorId = resultSet.getLong("author_id");
            String authorFirstName = resultSet.getString("author_firstname");
            String authorSecondName = resultSet.getString("author_secondname");
            Author author = new Author(authorId, authorFirstName, authorSecondName);
            long genreId = resultSet.getLong("genre_id");
            String genreName = resultSet.getString("genre_name");
            Genre genre = new Genre(genreId, genreName);
            return new Book(id, name, author, genre);
        }
    }
}
