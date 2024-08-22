package com.rntgroup.repository;

import com.rntgroup.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class UserRepository {

    private static final String FIND_BY_ID_SQL = """
            SELECT *
            FROM users
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT *
            FROM users
            """;

    private static final String FIND_ALL_BY_FRIENDS_AND_LIKES_AT_SPECIFIED_PERIOD = """
            WITH FriendsCount AS (SELECT userId1,
                                         COUNT(userId2) AS friends_count
                                  FROM friendships
                                  GROUP BY userId1),
                 LikesCount AS (SELECT userId,
                                       COUNT(*) AS likes_count
                                FROM likes
                                WHERE EXTRACT(MONTH FROM timestamp) = ?
                                  AND EXTRACT(YEAR FROM timestamp) = ?
                                GROUP BY userId)
            SELECT *
            FROM users u
                     JOIN FriendsCount fc ON u.id = fc.userId1
                     JOIN LikesCount lc ON u.id = lc.userId
            WHERE fc.friends_count > ?
              AND lc.likes_count > ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO users (name, surname, birthdate)
            VALUES (?, ?, ?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE users
            SET name = ?,
                surname = ?,
                birthdate = ?
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE
            FROM users
            WHERE id = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findById(Long id) {
        return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, new Object[]{id}, userRowMapper);
    }

    public List<User> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, userRowMapper);
    }

    public List<User> findAllByFriendsAndLikesAtSpecifiedPeriod(int friendsCount,
                                                                int likesCount,
                                                                int year,
                                                                int month) {
        return jdbcTemplate.query(FIND_ALL_BY_FRIENDS_AND_LIKES_AT_SPECIFIED_PERIOD,
                new Object[]{month, year, friendsCount, likesCount},
                userRowMapper);
    }

    public User save(User user) {
        jdbcTemplate.update(SAVE_SQL, user.getName(), user.getSurname(), user.getBirthdate());
        return user;
    }

    public void update(User user) {
        jdbcTemplate.update(UPDATE_SQL, user.getName(), user.getSurname(), user.getBirthdate(), user.getId());
    }

    public void delete(Long id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }

    private final RowMapper<User> userRowMapper = (ResultSet rs, int rowNum) -> User.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .surname(rs.getString("surname"))
            .birthdate(rs.getDate("birthdate").toLocalDate())
            .build();
}
