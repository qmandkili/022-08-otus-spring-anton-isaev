package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public long insert(Author author) {
        var kh = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource()
                .addValue("first_name", author.getFirstName())
                .addValue("second_name", author.getSecondName());
        jdbc.update("insert into authors (first_name, second_name) " +
                " values (:first_name, :second_name)", params, kh);
        return kh.getKey().longValue();
    }

    @Override
    public Author getById(long id) {
        return jdbc.queryForObject("select id, first_name, second_name from authors where id = :id",
                new MapSqlParameterSource("id", id), new AuthorMapper());
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("select id, first_name, second_name from authors", new AuthorMapper());
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from authors where id = :id", new MapSqlParameterSource("id", id));
    }

    @Override
    public int update(Author author) {
        var params = new MapSqlParameterSource();
        params.addValue("id", author.getId());
        params.addValue("first_name", author.getFirstName());
        params.addValue("second_name", author.getSecondName());
        return jdbc.update("update authors set first_name = :first_name, " +
                " second_name = :second_name where id = :id", params);
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String firstName = resultSet.getString("first_name");
            String secondName = resultSet.getString("second_name");
            return new Author(id, firstName, secondName);
        }
    }
}
