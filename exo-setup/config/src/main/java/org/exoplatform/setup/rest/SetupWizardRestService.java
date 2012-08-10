package org.exoplatform.setup.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.setup.data.SetupWizardData;
import org.exoplatform.setup.data.StartupInformationDto;
import org.exoplatform.setup.data.WizardPropertiesException;
import org.exoplatform.setup.util.WizardProperties;
import org.exoplatform.setup.util.WizardUtility;

@Path(SetupWizardRestService.WS_ROOT_PATH)
public class SetupWizardRestService implements ResourceContainer {
  
  private static Logger logger = Logger.getLogger(SetupWizardRestService.class);

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
  
  @GET
  @Path("/si")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getStartupInfos() {
    
    if(logger.isDebugEnabled()) {
      logger.debug("getStartupInformation is called");
    }

    CacheControl cacheControl = new CacheControl();
    cacheControl.setNoCache(true);
    cacheControl.setNoStore(true);

    StartupInformationDto dto = new StartupInformationDto();
    
    try {
      dto.setIsDebugActivated(WizardProperties.getDebug());
      dto.setIsQuickModeActivated(WizardProperties.getQuickMode());
      dto.setFirstScreenNumber(WizardProperties.getFirstScreenNumber());
      
      // Exo Conf, load properties
      PropertiesConfiguration exoConf = getExoConfiguration();
      Map<String, String> propertiesValues = new HashMap<String, String>();
      
      if(exoConf != null) {
        for(SetupWizardData data : SetupWizardData.values()) {
          Object ppObj = exoConf.getProperty(data.getPropertyName());
          if(ppObj != null) {
            String pp = ppObj.toString();
            pp = WizardUtility.mergeProperty(pp);
            propertiesValues.put(data.getPropertyName(), pp);
          }
        }
      }
      dto.setPropertiesValues(propertiesValues);
    }
    catch(Exception e) {
      logger.error("Cannot get startup informations: " + e.getMessage(), e);
    }
    
    return Response.ok(dto, MediaType.APPLICATION_JSON).cacheControl(cacheControl).build();
  }
  
  /**
   * Try to load exo configuration file
   * @return PropertiesConfiguration or null if not found
   */
  private PropertiesConfiguration getExoConfiguration() throws WizardPropertiesException {
    PropertiesConfiguration conf = null;
    
    // Get configuration path
    String path = WizardUtility.getExoConfigurationPropertiesPath(WizardUtility.getCurrentServerName());

    try {
      conf = new PropertiesConfiguration(path);
    }
    catch (Exception e) {
      logger.error("Cannot get Properties configuration", e);
      
      // Launches an exception to the client to not display next screen
      throw new WizardPropertiesException("Cannot get Properties configuration: " + e.getMessage());
    }
    
    return conf;
  }

}
