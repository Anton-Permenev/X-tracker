package com.xtracker.backend.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


// /user/tracks/user_id/track_id
@Path("rest")
public class RestResource {

    @GET
    @Path("hello")
    public String GetHello() {
        return "hello!";

    }


//
//    @GET
//    @Path("{user_id}")
//    @Produces(MediaType.APPLICATION_XML)
//    public List<Track> getTracksList(long user_id,Token access_token) {
//
//    }
//
//    @POST
//    @Produces(MediaType.APPLICATION_XML)
//    @Consumes(MediaType.APPLICATION_XML)
//    public void newUser(String email,Token access_token){
//
//    }
//
//    @GET
//    @Path("{track_id}")
//    @Produces(MediaType.APPLICATION_XML)
//    public Track getTrack(long track_id,Token access_token) {
//
//    }
//
//
//    @POST
//    @Produces(MediaType.APPLICATION_XML)
//    @Consumes(MediaType.APPLICATION_XML)
//    public void addTrack(List<Point> points,Timestamp time_start,Timestamp time_end,Token access_token) {
//
//    }
//
//    @POST
//    @Produces(MediaType.APPLICATION_XML)
//    @Consumes(MediaType.APPLICATION_XML)
//    public void editTrack(long track_id,List<Point> points,Timestamp time_start,Timestamp time_end,Token access_token){
//
//    }
//
//    @POST
//    @Produces(MediaType.APPLICATION_XML)
//    @Consumes(MediaType.APPLICATION_XML)
//    public void deleteTrack(long track_id,Token access_token){
//
//    }



    
}
