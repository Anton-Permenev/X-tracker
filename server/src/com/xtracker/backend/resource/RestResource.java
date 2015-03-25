package com.xtracker.backend.resource;

import com.xtracker.backend.ejb.AuthBean;
import com.xtracker.backend.ejb.ORMBean;
import com.xtracker.backend.jpa.Track;
import com.xtracker.backend.resource.pojo.Keys;
import com.xtracker.backend.resource.utils.Secured;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


// /user/tracks/user_id/track_id
//"users/tracks/{user_id}/{track_id}"
@Path("rest")
public class RestResource {

    @EJB(beanName = "orm")
    ORMBean ormBean;

    @EJB(beanName = "auth")
    AuthBean authBean;

    @GET
    @Path("hello")
    @Produces("application/json")
    public String hello(){
        return new String("Helloww");
    }

    /**
     * Login or register a user with given email and Google OAuth access token. If given email does not exist in the database
     * (but the token is valid), a new user will be created and persisted.
     * @param email an email of a new or existing user complying with google account
     * @param token valid Google OAuth access token
     * @return a new private key used for accessing secured REST methods
     */


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("login")
    public Keys login(@QueryParam("email") String email, @QueryParam("access_token") String token)  {
        if (email == null || token == null)
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("no email or token provided").build());
        try {
            String properEmail = authBean.fetchEmail(token);
            String privateKey;
            String publicKey;
            if (properEmail.equals(email)) {
                privateKey = authBean.generatePrivateKey();
                publicKey = String.valueOf(ormBean.setPrivateKey(email, privateKey));
            } else {
                throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("this token is not valid").build());
            }
            Keys keys = new Keys();
            keys.setPrivateKey(privateKey);
            keys.setPublicKey(publicKey);
            return keys;
        } catch (Exception e) {
            throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("internal server error").build());
        }
    }

    @GET
    @Path("tracks")
    @Produces("application/json")
    @Secured
    public List<Track> getTracksList() {
        return null;
    }

    @GET
    @Path("tracks/{track_id}")
    @Produces("application/json")
    public Track getTrack(long track_id) {
        return null;
    }


    @POST
    @Path("tracks")
    @Produces("application/json")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public void addTrack(Track track) {
        System.out.println("track" + track.getTrackId());
    }

    @DELETE
    @Path("tracks/{track_id}")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public void deleteTrack(@PathParam("track_id")long track_id){

    }





}
