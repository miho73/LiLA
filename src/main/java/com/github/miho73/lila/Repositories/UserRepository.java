package com.github.miho73.lila.Repositories;

import com.github.miho73.lila.objects.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Repository
public class UserRepository extends Database {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void addUser(User user, Connection con) throws SQLException {
        try {
            String sql = "INSERT INTO users (user_id, refresh_token, user_name, email, auth_from, join_date) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psmt = con.prepareStatement(sql);

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            psmt.setString(1, user.getUserId());
            psmt.setString(2, user.getRefreshToken());
            psmt.setString(3, user.getUserName());
            psmt.setString(4, user.getEmail());
            psmt.setInt(5, user.getAuthFrom().getInteger());
            psmt.setTimestamp(6, timestamp);

            psmt.execute();
        } catch (SQLException e) {
            logger.error("cannot add user", e);
            throw new SQLException(e);
        }
    }
}
