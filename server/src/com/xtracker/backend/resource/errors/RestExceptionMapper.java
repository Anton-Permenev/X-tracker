package com.xtracker.backend.resource.errors;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RestExceptionMapper implements ExceptionMapper<RestException> {

    @Override
    public Response toResponse(RestException e) {
        return Response.status(e.getStatus())
                .entity(new ErrorMessage(e))
                .type("application/json")
                .build();
    }
}
