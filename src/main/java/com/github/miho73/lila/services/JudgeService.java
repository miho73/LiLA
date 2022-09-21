package com.github.miho73.lila.services;

import com.github.miho73.lila.Repositories.JudgeRepository;
import com.github.miho73.lila.Repositories.ProblemRepository;
import com.github.miho73.lila.objects.Exception.JudgeException;
import com.github.miho73.lila.objects.Judge;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service("JudgeService")
public class JudgeService {

    int JUDGE_COUNT;

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    JudgeRepository judgeRepository;

    @PostConstruct
    public void initJudgeService() throws SQLException {
        Connection connection = judgeRepository.openConnection();
        JUDGE_COUNT = judgeRepository.getJudgeCount(connection);
        judgeRepository.close(connection);
    }

    public JSONObject judgeSubmit(int problemCode, JSONArray userAnswer) throws SQLException, JudgeException {
        Connection connection = problemRepository.openConnection();
        Connection judgeConnection = judgeRepository.openConnectionForEdit();

        try {
            List<Judge> answer = problemRepository.getAnswers(problemCode, connection);

            // 1. answer form check
            if (answer.size() != userAnswer.length()) {
                throw new JudgeException(0);
            }

            boolean wasFormatError = false;
            for (int i = 0; i < answer.size(); i++) {
                if (((JSONObject) userAnswer.get(i)).getInt("m") != answer.get(i).getMethodCode()) {
                    wasFormatError = true;
                    break;
                }
            }
            if (wasFormatError) {
                throw new JudgeException(1);
            }

            // 2. judge user answer
            int score = 0;
            int fullScore = 100;
            JSONObject judgeResult = new JSONObject();
            JSONArray judgedElements = new JSONArray();

            for (int i = 0; i < answer.size(); i++) {
                Object userAns = ((JSONObject) userAnswer.get(i)).get("a");
                Judge ans = answer.get(i);

                JSONObject element = new JSONObject();
                element.put("quota", ans.getQuota());
                element.put("method", ans.getMethodCode());
                element.put("answer", ans.getAnswer());
                element.put("yours", userAns.toString());

                switch (ans.getMethod()) {
                    case SELF -> {
                        if ((boolean) userAns) {
                            score += ans.getQuota();
                            element.put("correct", true);
                            element.put("your-score", ans.getQuota());
                        } else {
                            element.put("correct", false);
                            element.put("your-score", 0);
                        }
                    }
                    case EQUATION -> {
                        if (ans.getAnswer().equals(userAns)) {
                            score += ans.getQuota();
                            element.put("correct", true);
                            element.put("your-score", ans.getQuota());
                        } else {
                            element.put("correct", false);
                            element.put("your-score", 0);
                        }
                    }
                }
                judgedElements.put(element);
            }

            judgeResult.put("problem_code", problemCode);
            judgeResult.put("judge", judgedElements);
            judgeResult.put("full-score", fullScore);
            judgeResult.put("your-score", fullScore * score / 100);

            judgeRepository.reportJudge(judgeConnection, judgeResult, userAnswer, judgedElements);

            judgeRepository.commit(judgeConnection);
            return judgeResult;
        } catch (SQLException e) {
            log.error("Failed to judge submission. Transaction rolled back");
            judgeRepository.rollback(judgeConnection);
            throw e;
        } finally {
            problemRepository.close(connection);
            judgeRepository.close(judgeConnection);
        }
    }
}
