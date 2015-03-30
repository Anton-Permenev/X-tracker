package com.xtracker.backend.resource.errors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ParamsExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        String entity = "";
        for (ConstraintViolation<?> cv : e.getConstraintViolations()) {
            System.out.println(cv.getMessage());
            entity += cv.getMessage() + "\n";
        }
        int status = Response.Status.BAD_REQUEST.getStatusCode();
        return Response.status(status)
                .entity(new ErrorMessage(status, entity))
                .type("application/json")
                .build();
    }
}
