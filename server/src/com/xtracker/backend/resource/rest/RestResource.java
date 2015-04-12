package com.xtracker.backend.resource.rest;

import com.xtracker.backend.ejb.AuthBean;
import com.xtracker.backend.ejb.ORMBean;
import com.xtracker.backend.jpa.Point;
import com.xtracker.backend.jpa.Track;
import com.xtracker.backend.resource.errors.ErrorMessage;
import com.xtracker.backend.resource.errors.RestException;
import com.xtracker.backend.resource.pojo.Keys;
import com.xtracker.backend.resource.utils.Secured;
import org.hibernate.validator.constraints.Email;

import javax.ejb.EJB;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;


// /user/tracks/user_id/track_id
//"users/tracks/{user_id}/{track_id}"
@Path("/rest")
public class RestResource {

    String email = "";

    @EJB(beanName = "orm")
    ORMBean ormBean;

    @EJB(beanName = "auth")
    AuthBean authBean;

    @GET
    @Path("/hello")
    @Produces("application/json")
    @Secured
    public String hello() {
        return new String("Helloww");
    }

    /**
     * Login or register a user with given email and Google OAuth access token. If given email does not exist in the database
     * (but the token is valid), a new user will be created and persisted.
     *
     * @param email an email of a new or existing user complying with google account
     * @param token valid Google OAuth access token
     * @return a new private key used for accessing secured REST methods
     */

    @GET
    @Produces("application/json")
    @Path("login")
    public Keys login(@NotNull(message = "No email provided.") @Email(message = "Please provide valid email address.") @QueryParam("email") String email,
                      @NotNull(message = "No access token provided.") @QueryParam("access_token") String token) throws Exception {
        String properEmail = authBean.fetchEmail(token);
        String privateKey;
        String publicKey;
        if (properEmail != null && properEmail.equals(email)) {
            privateKey = authBean.generatePrivateKey();
            publicKey = String.valueOf(ormBean.setPrivateKey(email, privateKey));
        } else {
            throw new RestException(ErrorMessage.TOKEN_INVALID, Response.Status.UNAUTHORIZED);
        }
        Keys keys = new Keys();
        keys.setPrivateKey(privateKey);
        keys.setPublicKey(publicKey);
        return keys;
    }

    @GET
    @Path("tracks")
    @Produces("application/json")
    @Secured
    public List<Track> getTracksList(@QueryParam("user_id") long user_id) throws SQLException {
        List<Track> tracks = ormBean.getTracks(user_id);

        for (Track track : tracks) {
            track.setPoints(null);
            track.setJumps(null);
            track.setUser(null);
        }
        return tracks;
    }

    @GET
    @Path("tracks/{track_id}")
    @Produces("application/json")
    public Track getTrack(@PathParam("track_id") long track_id) throws SQLException {
        Track track = ormBean.getTrack(track_id);
        System.out.println(track.getTrackId());
        for (Point p : track.getPoints()){
            p.setTrack(null);
        }
        track.setUser(null);
        return track;
    }


    @POST
    @Path("tracks")
    @Produces("application/json")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public void addTrack(@NotNull Track track, @HeaderParam("user_id") String userId) throws SQLException {
        track.setUser(ormBean.getUser(Integer.valueOf(userId)));
        ormBean.addTrack(track);
    }

    @DELETE
    @Path("tracks/{track_id}")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public void deleteTrack(@PathParam("track_id") long track_id) {
        deleteTrack(track_id);
    }


}
