package com.rntgroup.repository;

import com.rntgroup.entity.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class LikeRepository {

    private static final String FIND_ALL_BY_POST_ID_SQL = """
            SELECT *
            FROM likes
            WHERE postId = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO likes (postId, userId, timestamp)
            VALUES (?, ?, ?)
            """;

    private static final String DELETE_SQL = """
            DELETE
            FROM likes
            WHERE postId = ? AND userId = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Like> findAllByPostId(Long postId) {
        return jdbcTemplate.query(FIND_ALL_BY_POST_ID_SQL, new Object[]{postId}, likeRowMapper);
    }

    public void save(Like like) {
        jdbcTemplate.update(SAVE_SQL, like.getPostId(), like.getUserId(), like.getTimestamp());
    }

    public void delete(Like like) {
        jdbcTemplate.update(DELETE_SQL, like.getPostId(), like.getUserId());
    }

    private final RowMapper<Like> likeRowMapper = (ResultSet rs, int rowNum) -> Like.builder()
            .postId(rs.getLong("postId"))
            .userId(rs.getLong("userId"))
            .timestamp(rs.getTimestamp("timestamp").toLocalDateTime())
            .build();
}
