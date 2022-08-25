package com.github.miho73.lila.Repositories;

import com.github.miho73.lila.objects.Problem;
import com.github.miho73.lila.objects.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class ProblemRepository extends Database {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void addProblem(Problem problem, Connection connection) throws SQLException {
        try {
            String sql = "INSERT INTO problems (problem_name, content, html_content, solution, html_solution, answer, tags, difficulty, branch, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

            PreparedStatement psmt = connection.prepareStatement(sql);

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            psmt.setString(1, problem.getName());
            psmt.setString(2, problem.getContent());
            psmt.setString(3, problem.getHtmlContent());
            psmt.setString(4, problem.getSolution());
            psmt.setString(5, problem.getHtmlSolution());
            psmt.setString(6, problem.getAnswer());
            psmt.setInt(7, problem.getTag());
            psmt.setInt(8, problem.getDifficultyCode());
            psmt.setInt(9, problem.getBranchCode());
            psmt.setInt(10, problem.getStatusCode());

            psmt.execute();
        } catch (SQLException e) {
            logger.error("SQLException: Cannot add problem to database.", e);
            throw e;
        }
    }

    public int getProblemCount(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) AS cnt FROM problems;";

        PreparedStatement psmt = connection.prepareStatement(sql);
        ResultSet rs = psmt.executeQuery();
        rs.next();
        return rs.getInt("cnt");
    }
}
