package com.github.miho73.lila.Repositories;

import com.github.miho73.lila.objects.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class UserRepository extends Database {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void addUser(User user, Connection con) throws SQLException {
        try {
            String sql = "INSERT INTO users (user_id, refresh_token, user_name, email, auth_from, join_date, privilege) VALUES (?, ?, ?, ?, ?, ?, 'u');";
            PreparedStatement psmt = con.prepareStatement(sql);

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            psmt.setString(1, user.getUserId());
            psmt.setString(2, user.getRefreshToken());
            psmt.setString(3, user.getUserName());
            psmt.setString(4, user.getEmail());
            psmt.setInt(5, user.getAuthFrom().getInteger());
            psmt.setTimestamp(6, timestamp);

            psmt.execute();
            logger.info("User added. user_id="+user.getUserId());
        } catch (SQLException e) {
            logger.error("SQLException: Cannot add user to database.", e);
            throw e;
        }
    }

    public boolean queryUserExistence(String user_id, Connection connection) throws SQLException {
        try {
            String sql = "SELECT COUNT(*) AS cnt FROM users WHERE user_id=?;";
            PreparedStatement psmt = connection.prepareStatement(sql);

            psmt.setString(1, user_id);

            ResultSet rs = psmt.executeQuery();
            if(!rs.next()) {
                throw new SQLException("No row was returned from query.");
            }
            if(rs.getInt("cnt") >= 2) {
                logger.warn("Duplicated user_id detected. user_id="+user_id);
                return false;
            }
            return rs.getInt("cnt") == 1;
        } catch (SQLException e) {
            logger.error("SQLException: Cannot query user existence.", e);
            throw e;
        }
    }

    public void updateUserLoginTime(String user_id, Connection connection) throws SQLException {
        try {
            String sql = "UPDATE users SET last_login=? WHERE user_id=?;";
            PreparedStatement psmt = connection.prepareStatement(sql);

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            psmt.setTimestamp(1, timestamp);
            psmt.setString(2, user_id);

            psmt.execute();
        } catch (SQLException e) {
            logger.error("SQLException: Cannot update user login time.", e);
            throw e;
        }
    }

    public User queryUser(String uid, Connection connection) throws SQLException {
        try {
            String sql = "SELECT * FROM users WHERE user_id=?;";
            PreparedStatement psmt = connection.prepareStatement(sql);

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            psmt.setString(1, uid);
            ResultSet rs = psmt.executeQuery();

            if(!rs.next()) {
                throw new SQLException("No row was returned from query.");
            }

            User user = new User();
            user.setUserName(rs.getString("user_name"));
            user.setUserId(rs.getString("user_id"));
            user.setEmail(rs.getString("email"));
            user.setRefreshToken(rs.getString("refresh_token"));
            user.setUserCode(rs.getInt("user_code"));
            user.setJoinDate(rs.getTimestamp("join_date"));
            user.setPrivilege(rs.getString("privilege"));
            user.setLastLogin(rs.getTimestamp("last_login"));
            switch (rs.getInt("auth_from")) {
                case 0 -> user.setAuthFrom(User.AUTH_SOURCES.GOOGLE);
                case 1 -> user.setAuthFrom(User.AUTH_SOURCES.KAKAO);
            }
            return user;
        } catch (SQLException e) {
            logger.error("SQLException: Cannot query user.", e);
            throw e;
        }
    }
}
