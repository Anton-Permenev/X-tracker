package com.xtracker.backend.resource.rest;

import com.xtracker.backend.ejb.AuthBean;
import com.xtracker.backend.resource.utils.Secured;
import org.apache.commons.io.IOUtils;

import javax.ejb.EJB;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@Provider
@Secured
public class SecurityFilter implements ContainerRequestFilter {

    private int dff;

    @EJB
    AuthBean authBean;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String method = containerRequestContext.getMethod();
        String publicKey = "";
        String hmac = "";

        if (method.equals("GET")) {
            MultivaluedMap<String, String> params = containerRequestContext.getUriInfo().getQueryParameters();
            publicKey = params.getFirst("user_id");
            hmac = params.getFirst("hmac");
        } else {
            publicKey = containerRequestContext.getHeaderString("user_id");
            hmac = containerRequestContext.getHeaderString("hmac");
        }

        if (publicKey != null && hmac != null) {
            String data = IOUtils.toString(containerRequestContext.getEntityStream(), "UTF-8");
            if (data == null)
                data = "";
            try {
                if (!validate(publicKey, hmac, data)) {
                    returnAuthError(containerRequestContext, "authorization error");
                }
            } catch (NoSuchAlgorithmException | NumberFormatException | InvalidKeyException e) {
                returnAuthError(containerRequestContext, "server error");
            } catch (SQLException e) {
                returnAuthError(containerRequestContext, "no such user");
            }
        } else {
            returnAuthError(containerRequestContext, "no public key or hmac");
        }
    }

    private void returnAuthError(ContainerRequestContext context, String message) {
        context.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(message).build());

    }


    private boolean validate(String publicKey, String hmac, String data) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, SQLException, NumberFormatException {
        String properHmac = authBean.generateHmac(Long.parseLong(publicKey), data);
        return properHmac.equals(hmac);
    }

}
