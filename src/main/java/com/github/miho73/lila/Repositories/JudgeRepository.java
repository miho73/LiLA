package com.github.miho73.lila.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Slf4j
@Repository("JudgeRepository")
public class JudgeRepository extends Database {

    public int getJudgeCount(Connection connection) throws SQLException {
        try {
            String sql = "SELECT COUNT(*) AS cnt FROM judges;";

            PreparedStatement psmt = connection.prepareStatement(sql);
            ResultSet rs = psmt.executeQuery();
            rs.next();
            return rs.getInt("cnt");
        } catch (SQLException e) {
            log.error("SQLException: Failed to count judges in database", e);
            throw e;
        }
    }

    public void reportJudge(Connection connection, JSONObject judgeResult, JSONArray userAnswer, JSONArray judged) throws SQLException {
        try {
            String sql = "INSERT INTO judges (problem_code, judge_time, user_answer, user_score, full_score, correct) VALUES (?, ?, ?, ?, ?, ?);";

            PreparedStatement psmt = connection.prepareStatement(sql);

            Timestamp now = new Timestamp(System.currentTimeMillis());

            psmt.setInt(1, judgeResult.getInt("problem_code"));
            psmt.setTimestamp(2, now);
            psmt.setString(3, userAnswer.toString(0));
            psmt.setInt(4, judgeResult.getInt("your-score"));
            psmt.setInt(5, judgeResult.getInt("full-score"));
            psmt.setString(6, judged.toString(0));

            psmt.execute();
        } catch (SQLException e) {
            log.error("SQLException: Failed to register judge in database", e);
            throw e;
        }
    }
}
