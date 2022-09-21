package com.github.miho73.lila.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Judge {
    private String name, answer;
    private int quota;
    private JudgeMethod method;

    public void setMethod(int m) {
        this.method = switch (m) {
            case 0 -> JudgeMethod.SELF;
            case 1 -> JudgeMethod.EQUATION;
            default -> throw new IllegalStateException("Unexpected value for judge method: " + m);
        };
    }

    public int getMethodCode() {
        return switch (method) {
            case SELF -> 0;
            case EQUATION -> 1;
        };
    }

    public enum JudgeMethod {
        SELF,
        EQUATION
    }
}
