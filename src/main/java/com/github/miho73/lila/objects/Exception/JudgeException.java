package com.github.miho73.lila.objects.Exception;

import lombok.Getter;

@Getter
public class JudgeException extends Exception {
    private final int causeCode;

    /**
     * Causes:
     * 0: Length of user answer is different with database
     * 1: Some judge elements has wrong judge method
     * @param code cause of error
     */
    public JudgeException(int code) {
        super();
        this.causeCode = code;
    }
}
