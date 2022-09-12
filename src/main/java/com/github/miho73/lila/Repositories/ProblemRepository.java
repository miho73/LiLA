package com.github.miho73.lila.Repositories;

import com.github.miho73.lila.objects.Problem;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Vector;

@Slf4j
@Repository
public class ProblemRepository extends Database {

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
            log.error("SQLException: failed to add problem to database.", e);
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

    public Problem getProblem(Connection connection, int problemCode) throws SQLException {
        String sql = "SELECT * FROM problems WHERE problem_code=?;";

        PreparedStatement psmt = connection.prepareStatement(sql);
        psmt.setInt(1, problemCode);

        ResultSet rs = psmt.executeQuery();
        if(!rs.next()) {
            log.warn("problem with code "+problemCode+" was not found.");
            return null;
        }

        Problem problem = new Problem();
        problem.setCode(rs.getInt("problem_code"));
        problem.setName(rs.getString("problem_name"));
        problem.setContent(rs.getString("content"));
        problem.setHtmlContent(rs.getString("html_content"));
        problem.setSolution(rs.getString("solution"));
        problem.setHtmlSolution(rs.getString("html_solution"));
        problem.setAnswer(rs.getString("answer"));
        problem.setTag(rs.getInt("tags"));
        problem.setDifficulty(rs.getInt("difficulty"));
        problem.setBranch(rs.getInt("branch"));
        problem.setStatus(rs.getInt("status"));

        return problem;
    }

    public void updateProblem(Problem problem, Connection connection) throws SQLException {
        try {
            String sql = "UPDATE problems SET problem_name=?, content=?, html_content=?, solution=?, html_solution=?, answer=?, tags=?, difficulty=?, branch=?, status=? WHERE problem_code=?;";

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
            psmt.setInt(11, problem.getCode());

            psmt.execute();
        } catch (SQLException e) {
            log.error("SQLException: failed to update problem to database.", e);
            throw e;
        }
    }

    public List<Problem> searchProblems(Connection connection, boolean queryEanbled, String query, String flagSql) throws SQLException {
        String sql;
        if(queryEanbled) {
            sql = "SELECT * FROM problems WHERE (content LIKE '%' || ? || '%' OR problem_name LIKE '%' || ? || '%') AND "+flagSql+";";
        }
        else {
            sql = "SELECT * FROM problems WHERE "+flagSql+";";
        }
        log.info("search query: "+sql);

        try {
            PreparedStatement psmt = connection.prepareStatement(sql);

            if(queryEanbled) {
                psmt.setString(1, query);
                psmt.setString(2, query);
            }

            ResultSet rs = psmt.executeQuery();

            List<Problem> searched = new Vector<>();
            while (rs.next()) {
                Problem problem = new Problem();
                problem.setCode(rs.getInt("problem_code"));
                problem.setName(rs.getString("problem_name"));
                problem.setTag(rs.getInt("tags"));
                problem.setDifficulty(rs.getInt("difficulty"));
                problem.setBranch(rs.getInt("branch"));
                problem.setStatus(rs.getInt("status"));
                searched.add(problem);
            }

            return searched;
        } catch (SQLException e) {
            log.error("SQLException: failed to search problem. query="+sql+". text="+query, e);
            throw e;
        }
    }
}
