package com.github.miho73.lila.services;

import com.github.miho73.lila.Repositories.ProblemRepository;
import com.github.miho73.lila.objects.Exception.LiLACParsingException;
import com.github.miho73.lila.objects.Problem;
import com.github.miho73.lila.utils.LiLACRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.SQLException;

@Service("ProblemService")
public class ProblemService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired ProblemRepository problemRepository;

    public int PROBLEM_COUNT;

    @PostConstruct
    public void initProblemSystem() {
        Connection connection = null;
        try {
            connection = problemRepository.openConnection();
            PROBLEM_COUNT = problemRepository.getProblemCount(connection);
            logger.info("Problem count set to "+PROBLEM_COUNT);
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
            logger.info("Problem "+PROBLEM_COUNT+" created");
        } catch (Exception e) {
            if(connection != null) {
                problemRepository.rollbackAndClose(connection);
                logger.error("Failed to create problem. Transaction was rolled back", e);
            }
            logger.error("Failed to create problem. Transaction was not initiated", e);
            throw e;
        }
    }

    public Problem getProblem(int problemCode) throws SQLException {
        try {
            Connection connection = problemRepository.openConnection();
            return problemRepository.getProblem(connection, problemCode);
        } catch (Exception e) {
            logger.error("Failed to query problem from database", e);
            throw e;
        }
    }
}
