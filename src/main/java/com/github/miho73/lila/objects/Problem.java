package com.github.miho73.lila.objects;

public class Problem {
    int code, tag;
    String name;
    String content;
    String solution;
    String answer;
    String htmlContent;

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getHtmlSolution() {
        return htmlSolution;
    }

    public void setHtmlSolution(String htmlSolution) {
        this.htmlSolution = htmlSolution;
    }

    String htmlSolution;
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
    public int getDifficultyCode() {
        return switch (difficulty) {
            case NOT_SET    -> 0;
            case UNRATED    -> 1;
            case BRONZE     -> 2;
            case SILVER     -> 3;
            case GOLD       -> 4;
            case AMBER      -> 5;
            case CRYSTAL    -> 6;
            case EMERALD    -> 7;
            case SAPPHIRE   -> 8;
            case RUBY       -> 9;
            case DIAMOND    -> 10;
        };
    }

    public void setDifficulty(DIFFICULTY difficulty) {
        this.difficulty = difficulty;
    }
    public void setDifficulty(int difficulty) {
        this.difficulty = switch (difficulty) {
            case 0 -> DIFFICULTY.NOT_SET;
            case 1 -> DIFFICULTY.UNRATED;
            case 2 -> DIFFICULTY.BRONZE;
            case 3 -> DIFFICULTY.SILVER;
            case 4 -> DIFFICULTY.GOLD;
            case 5 -> DIFFICULTY.AMBER;
            case 6 -> DIFFICULTY.CRYSTAL;
            case 7 -> DIFFICULTY.EMERALD;
            case 8 -> DIFFICULTY.SAPPHIRE;
            case 9 -> DIFFICULTY.RUBY;
            case 10 -> DIFFICULTY.DIAMOND;
            default -> throw new IllegalStateException("Unexpected value: " + difficulty);
        };
    }

    public BRANCH getBranch() {
        return branch;
    }
    public int getBranchCode() {
        return switch (branch) {
            case GENERAL        -> 0;
            case ALGEBRA        -> 1;
            case COMBINATORICS  -> 2;
            case GEOMETRY       -> 3;
            case NUMBER_THEORY  -> 4;
            case PHYSICS        -> 5;
            case CHEMISTRY      -> 6;
            case BIOLOGY        -> 7;
        };
    }

    public void setBranch(BRANCH branch) {
        this.branch = branch;
    }
    public void setBranch(int branch) {
        this.branch = switch (branch) {
            case 0 -> BRANCH.GENERAL;
            case 1 -> BRANCH.ALGEBRA;
            case 2 -> BRANCH.COMBINATORICS;
            case 3 -> BRANCH.GEOMETRY;
            case 4 -> BRANCH.NUMBER_THEORY;
            case 5 -> BRANCH.PHYSICS;
            case 6 -> BRANCH.CHEMISTRY;
            case 7 -> BRANCH.BIOLOGY;
            default -> throw new IllegalStateException("Unexpected value: " + branch);
        };
    }

    public PROBLEM_STATUS getStatus() {
        return status;
    }
    public int getStatusCode() {
        return switch (status) {
            case OPEN               -> 0;
            case CORRECTING         -> 1;
            case NOT_SUBMITTABLE    -> 2;
            case UNPUBLISHED        -> 3;
        };
    }

    public void setStatus(PROBLEM_STATUS status) {
        this.status = status;
    }

    public void setStatus(int status) {
        this.status = switch (status) {
            case 0 -> PROBLEM_STATUS.OPEN;
            case 1 -> PROBLEM_STATUS.CORRECTING;
            case 2 -> PROBLEM_STATUS.NOT_SUBMITTABLE;
            case 3 -> PROBLEM_STATUS.UNPUBLISHED;
            default -> throw new IllegalStateException("Unexpected value: " + status);
        };
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
        GENERAL,
        ALGEBRA,
        COMBINATORICS,
        GEOMETRY,
        NUMBER_THEORY,
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
