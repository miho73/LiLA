package com.github.miho73.lila.objects.Exception;

public class LiLACParsingException extends Exception {
    public LiLACParsingException() {
    }

    public LiLACParsingException(String message) {
        super(message);
    }

    public LiLACParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public LiLACParsingException(Throwable cause) {
        super(cause);
    }

    public LiLACParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
