package com.xtracker.backend.resource;

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
import java.util.HashMap;
import java.util.Map;


@Provider
@Secured
public class SecurityFilter implements ContainerRequestFilter {

    @EJB
    AuthBean authBean;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String method = containerRequestContext.getMethod();
        Map<String, String> params = method.equals("POST") ? getPostParams(containerRequestContext) : getGetParams(containerRequestContext);

        String publicKey = params.get("user_id");
        String hmac = params.get("hmac");

        if (publicKey != null && hmac != null) {
            params.remove("hmac");
            params.remove("user_id");
            String data = publicKey;
            for (Map.Entry<String, String> param : params.entrySet()) {
                data += ";" + param.getValue();
            }
            try {
                if (!validate(publicKey, hmac, data)) {
                   returnAuthError(containerRequestContext, "authentication error");
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

    private Map<String, String> getGetParams(ContainerRequestContext context) {
        MultivaluedMap<String, String> params = context.getUriInfo().getQueryParameters();
        Map<String, String> map = new HashMap<>();
        for (String key : params.keySet()) {
            map.put(key, params.getFirst(key));
        }
        return map;
    }

    private Map<String, String> getPostParams(ContainerRequestContext context) throws IOException {
        String entity = IOUtils.toString(context.getEntityStream(), "UTF-8");
        String[] params = entity.split("&");
        Map<String, String> map = new HashMap<>();
        for (String param : params) {
            String[] keyValuePair = param.split("=");
            map.put(keyValuePair[0], keyValuePair[1]);
        }
        return map;
    }

    private boolean validate(String publicKey, String hmac, String data) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, SQLException, NumberFormatException {
        String properHmac = authBean.generateHmac(Long.parseLong(publicKey), data);
        return properHmac.equals(hmac);
    }

}
