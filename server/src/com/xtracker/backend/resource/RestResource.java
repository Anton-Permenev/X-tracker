package com.xtracker.backend.resource;

import com.xtracker.backend.ejb.AuthBean;
import com.xtracker.backend.ejb.ORMBean;
import com.xtracker.backend.resource.utils.Secured;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;


// /user/tracks/user_id/track_id
@Path("rest")
public class RestResource {

    @EJB(beanName = "orm")
    ORMBean ormBean;

    @EJB(beanName = "auth")
    AuthBean authBean;

    /**
     * Login or register a user with given email and Google OAuth access token. If given email does not exist in the database
     * (but the token is valid), a new user will be created and persisted.
     * @param email an email of a new or existing user complying with google account
     * @param token valid Google OAuth access token
     * @return a new private key used for accessing secured REST methods
     */
    @GET
    @Path("login")
    public String login(@QueryParam("email") String email, @QueryParam("access_token") String token)  {
        if (email == null || token == null)
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("no email or token provided").build());
        try {
            String properEmail = authBean.fetchEmail(token);
            String privateKey = "";
            if (properEmail.equals(email)) {
                privateKey = authBean.generatePrivateKey();
                ormBean.setPrivateKey(email, privateKey);
            } else {
                throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("this token is not valid").build());
            }
            return privateKey;
        } catch (Exception e) {
            throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("internal server error").build());
        }
    }

    @GET
    @Path("setTrack")
    @Secured
    public String setTrack() {
        return "track";
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
