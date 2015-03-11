package com.xtracker.backend.rest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("track")
public class TrackResource {
    @GET
    public String getAllMessages() {
        String message = "Hellow";
        return message;
    }
}
