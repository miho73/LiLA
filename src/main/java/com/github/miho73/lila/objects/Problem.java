package com.github.miho73.lila.objects;

public class Problem {
    int code, tag;
    String name, content, solution, answer;
    DIFFICULTY difficulty;
    BRANCH branch;
    PROBLEM_STATUS status;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public DIFFICULTY getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DIFFICULTY difficulty) {
        this.difficulty = difficulty;
    }

    public BRANCH getBranch() {
        return branch;
    }

    public void setBranch(BRANCH branch) {
        this.branch = branch;
    }

    public PROBLEM_STATUS getStatus() {
        return status;
    }

    public void setStatus(PROBLEM_STATUS status) {
        this.status = status;
    }

    public enum DIFFICULTY {
        NOT_SET,
        UNRATED,
        BRONZE,
        SILVER,
        GOLD,
        AMBER,
        CRYSTAL,
        EMERALD,
        SAPPHIRE,
        RUBY,
        DIAMOND
    }

    public enum BRANCH {
        ALGEBRA,
        NUMBER_THEORY,
        COMBINATORICS,
        GEOMETRY,
        PHYSICS,
        CHEMISTRY,
        BIOLOGY
    }

    public enum PROBLEM_STATUS {
        OPEN,
        CORRECTING,
        NOT_SUBMITTABLE,
        UNPUBLISHED
    }
}
