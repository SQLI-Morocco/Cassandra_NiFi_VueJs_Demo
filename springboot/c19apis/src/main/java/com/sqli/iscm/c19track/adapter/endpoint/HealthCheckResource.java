package com.sqli.iscm.c19track.adapter.endpoint;

import javax.json.Json;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class HealthCheckResource {
    @GET
    @Path("/healthcheck")
    @Produces(MediaType.APPLICATION_JSON)
    public String healthCheck() {
        return Json.createObjectBuilder().add("status", "success").build().toString();
    }
}
