package org.exoplatform.setup.rest;

import java.util.Collections;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.exoplatform.services.rest.resource.ResourceContainer;

@Path(SetupWizardRestService.WS_ROOT_PATH)
public class SetupWizardRestService implements ResourceContainer {

  protected final static String WS_ROOT_PATH = "/setuprest";
  
  @GET
  @Path("/pp")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getSystemProperties() {
    return Response.ok(Collections.emptyList(), MediaType.APPLICATION_JSON).build();
  }
  
  @GET
  @Path("/ds")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getDatasources() {
    return Response.ok(Collections.emptyList(), MediaType.APPLICATION_JSON).build();
  }
  
  @GET
  @Path("/wp")
  @Produces(MediaType.APPLICATION_JSON)
  public Response writeProperties() {
    return Response.ok(Collections.emptyList(), MediaType.APPLICATION_JSON).build();
  }
  
  @GET
  @Path("/sp")
  @Produces(MediaType.APPLICATION_JSON)
  public Response startPlatform() {
    return Response.ok(Collections.emptyList(), MediaType.APPLICATION_JSON).build();
  }
  
  @GET
  @Path("/il")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getImportantServerLogs() {
    return Response.ok(Collections.emptyList(), MediaType.APPLICATION_JSON).build();
  }

}
