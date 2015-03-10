package com.xtracker.backend.rest.resource;

import com.xtracker.backend.jpa.Point;
import com.xtracker.backend.jpa.Track;
import org.glassfish.jersey.message.internal.Token;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.List;


// /user/tracks/trackid

public class RestResource {
    @GET
    @Path("users/{user_id}")
    @Produces(MediaType.APPLICATION_XML)
    public List<Track> getTracksList(long user_id,Token access_token) {

    }

    @GET
    @Path("users/{user_id}/{track_id}")
    @Produces(MediaType.APPLICATION_XML)
    public Track getTrack(long track_id,Token access_token) {

    }


    @POST
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public void addTrack(List<Point> points,Timestamp time_start,Timestamp time_end,Token access_token) {

    }

    @POST
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public void editTrack(long track_id,List<Point> points,Timestamp time_start,Timestamp time_end,Token access_token){

    }

    @POST
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public void deleteTrack(long track_id,Token access_token){

    }

    @POST
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public void newUser(String email,Token access_token){

    }

    @POST
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void uploadPhoto(long track_id, image,Token access_token){

    }
}
