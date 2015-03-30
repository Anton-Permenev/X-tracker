package com.xtracker.backend.resource.errors;

import java.io.Serializable;

public class ErrorMessage implements Serializable{
    private int status;
    private String message;

    public static String NO_EMAIL_OR_TOKEN = "No email or token provided.";
    public static String TOKEN_INVALID = "Given token is not valid.";
    public static String INTERNAL_SERVER_ERROR = "Internal server error.";

    public ErrorMessage(RestException e) {
        status = e.getStatus();
        message = e.getMessage();
    }

    public ErrorMessage(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorMessage() {
    }
}
