package com.rntgroup.repository;

import com.rntgroup.entity.Friendship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class FriendshipRepository {

    private static final String FIND_ALL_BY_USER_ID_SQL = """
            SELECT *
            FROM friendships
            WHERE userId1 = ? OR userId2 = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO friendships (userId1, userId2)
            VALUES (?, ?)
            """;

    private static final String DELETE_SQL = """
            DELETE
            FROM friendships
            WHERE userId1 = ? AND userId2 = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Friendship> findAllByUserId(Long userId) {
        return jdbcTemplate.query(FIND_ALL_BY_USER_ID_SQL, new Object[]{userId, userId}, friendshipRowMapper);
    }

    public void save(Friendship friendship) {
        jdbcTemplate.update(SAVE_SQL, friendship.getUserId1(), friendship.getUserId2());
    }

    public void delete(Friendship friendship) {
        jdbcTemplate.update(DELETE_SQL, friendship.getUserId1(), friendship.getUserId2());
    }

    private final RowMapper<Friendship> friendshipRowMapper = (ResultSet rs, int rowNum) -> Friendship.builder()
            .userId1(rs.getLong("userId1"))
            .userId2(rs.getLong("userId2"))
            .timestamp(rs.getTimestamp("timestamp").toLocalDateTime())
            .build();
}
