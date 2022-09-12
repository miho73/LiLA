package com.github.miho73.lila.services;

import com.github.miho73.lila.Repositories.ProblemRepository;
import com.github.miho73.lila.objects.Exception.LiLACParsingException;
import com.github.miho73.lila.objects.Problem;
import com.github.miho73.lila.utils.LiLACRenderer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service("ProblemService")
public class ProblemService {

    @Autowired
    ProblemRepository problemRepository;

    public int PROBLEM_COUNT;

    @PostConstruct
    public void initProblemSystem() {
        Connection connection = null;
        try {
            connection = problemRepository.openConnection();
            PROBLEM_COUNT = problemRepository.getProblemCount(connection);
            log.info("Problem count set to "+PROBLEM_COUNT);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createProblem(Problem problem) throws SQLException, LiLACParsingException {
        Connection connection = problemRepository.openConnectionForEdit();
        try {
            problem.setHtmlContent(LiLACRenderer.render(problem.getContent()));
            problem.setHtmlSolution(LiLACRenderer.render(problem.getSolution()));

            problemRepository.addProblem(problem, connection);
            problemRepository.commitAndClose(connection);
            PROBLEM_COUNT++;
            log.info("Problem "+PROBLEM_COUNT+" created");
        } catch (Exception e) {
            if(connection != null) {
                problemRepository.rollbackAndClose(connection);
                log.error("Failed to create problem. Transaction was rolled back", e);
            }
            log.error("Failed to create problem. Transaction was not initiated", e);
            throw e;
        }
    }

    public Problem getProblem(int problemCode) throws SQLException {
        try {
            Connection connection = problemRepository.openConnection();
            return problemRepository.getProblem(connection, problemCode);
        } catch (Exception e) {
            log.error("Failed to query problem from database", e);
            throw e;
        }
    }

    public void updateProblem(int problem_code, Problem problem) throws SQLException, LiLACParsingException {
        Connection connection = problemRepository.openConnectionForEdit();
        try {
            problem.setHtmlContent(LiLACRenderer.render(problem.getContent()));
            problem.setHtmlSolution(LiLACRenderer.render(problem.getSolution()));
            problem.setCode(problem_code);

            problemRepository.updateProblem(problem, connection);
            problemRepository.commitAndClose(connection);
            PROBLEM_COUNT++;
            log.info("Problem "+problem_code+" updated");
        } catch (Exception e) {
            if(connection != null) {
                problemRepository.rollbackAndClose(connection);
                log.error("Failed to update problem. Transaction was rolled back", e);
            }
            log.error("Failed to update problem. Transaction was not initiated", e);
            throw e;
        }
    }

    public List<Problem> searchProblems(String query, int branch, int difficulty, int status) throws SQLException {
        Connection connection = problemRepository.openConnection();

        boolean[] branchFlag = new boolean[7];
        boolean[] difficultyFlag = new boolean[10];
        boolean[] statusFlag = new boolean[4];

        // true when filter enabled
        for (int i = 0; i < branchFlag.length; i++)
            branchFlag[i] = (branch & 1 << i) != 0;
        for (int i = 0; i < difficultyFlag.length; i++)
            difficultyFlag[i] = (difficulty & 1 << i) != 0;
        for (int i = 0; i < statusFlag.length; i++)
            statusFlag[i] = (status & 1 << i) != 0;

        String querySql;
        StringBuilder flagSql = new StringBuilder(),
                branchFilter = new StringBuilder("("),
                difficultyFilter = new StringBuilder("("),
                statusFilter = new StringBuilder("(");

        // flag sql
        int idx = 0;
        for(boolean includeBranch : branchFlag) {
            if(includeBranch) branchFilter.append("branch=").append(idx).append(" OR ");
            idx++;
        }
        branchFilter.append("false)");

        idx = 0;
        for(boolean includeDifficulty : difficultyFlag) {
            if(includeDifficulty) difficultyFilter.append("difficulty=").append(idx).append(" OR ");
            idx++;
        }
        difficultyFilter.append("false)");

        idx = 0;
        for(boolean includeStatus : statusFlag) {
            if(includeStatus) statusFilter.append("status=").append(idx).append(" OR ");
            idx++;
        }
        statusFilter.append("false)");

        if(branch != 0) {
            flagSql.append(branchFilter)
                   .append(" AND ");
        }
        if(difficulty != 0) {
            flagSql.append(difficultyFilter)
                   .append(" AND ");
        }
        if(status != 0) {
            flagSql.append(statusFilter)
                    .append(" AND ");
        }
        flagSql.append("true");

        return problemRepository.searchProblems(connection, !query.equals(""), query, flagSql.toString());
    }
}
