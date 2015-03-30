package com.xtracker.backend.resource.errors;

import javax.ws.rs.core.Response;

public class RestException extends Exception{
    private String message;
    private int status;

    public RestException(String message, Response.Status status) {
        super(message);
        this.status = status.getStatusCode();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
