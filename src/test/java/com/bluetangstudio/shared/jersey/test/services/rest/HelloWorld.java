package com.bluetangstudio.shared.jersey.test.services.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/rest/helloworld")
public interface HelloWorld {
    @GET
    public String getHello();

}