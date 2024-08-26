package com.rntgroup.repository;

import com.rntgroup.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class PostRepository {

    private static final String FIND_BY_ID_SQL = """
            SELECT *
            FROM posts
            WHERE id = ?
            """;

    private static final String FIND_ALL_BY_USER_ID_SQL = """
            SELECT *
            FROM posts
            WHERE userId = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT *
            FROM posts
            """;

    private static final String SAVE_SQL = """
            INSERT INTO posts (userId, text, timestamp)
            VALUES (?, ?, ?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE posts
            SET text = ?
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE
            FROM posts
            WHERE id = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Post findById(Long id) {
        return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, new Object[]{id}, postRowMapper);
    }

    public List<Post> findAllByUserId(Long userId) {
        return jdbcTemplate.query(FIND_ALL_BY_USER_ID_SQL, new Object[]{userId}, postRowMapper);
    }

    public List<Post> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, postRowMapper);
    }

    public Post save(Post post) {
        jdbcTemplate.update(SAVE_SQL, post.getUserId(), post.getText(), post.getTimestamp());
        return post;
    }

    public void update(Post post) {
        jdbcTemplate.update(UPDATE_SQL, post.getText(), post.getId());
    }

    public void delete(Long id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }

    private final RowMapper<Post> postRowMapper = (ResultSet rs, int rowNum) -> Post.builder()
            .id(rs.getLong("id"))
            .userId(rs.getLong("userId"))
            .text(rs.getString("text"))
            .timestamp(rs.getTimestamp("timestamp").toLocalDateTime())
            .build();
}
