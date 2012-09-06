package org.exoplatform.setup.rest;

import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.exoplatform.commons.utils.SecurityHelper;
import org.exoplatform.container.RootContainer;
import org.exoplatform.setup.data.ImportantLogsDto;
import org.exoplatform.setup.data.SetupWizardData;
import org.exoplatform.setup.data.StartupInformationDto;
import org.exoplatform.setup.data.WizardPropertiesException;
import org.exoplatform.setup.service.WizardTailService;
import org.exoplatform.setup.util.WizardProperties;
import org.exoplatform.setup.util.WizardUtility;
import org.jboss.resteasy.annotations.providers.jaxb.json.BadgerFish;

/**
 * This class contains all WS used by Setup Wizard application
 * 
 * @author Clement
 *
 */
@Path(SetupWizardRestService.WS_ROOT_PATH)
public class SetupWizardRestService {
  
  private static Logger logger = Logger.getLogger(SetupWizardRestService.class);

  protected final static String WS_ROOT_PATH = "/service";
  
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
  
  /**
   * This WS permit to write properties configured by user, into the exo configuration file.
   * @param queryParams
   * @return
   */
  @BadgerFish
  @POST
  @Path("/wp")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes("application/x-www-form-urlencoded")
  public Response writeProperties(MultivaluedMap<String, String> queryParams) {
    
    boolean isOk = false;
    
    if(logger.isDebugEnabled()) {
      logger.debug("writeProperties is called");
    }
    
    CacheControl cacheControl = new CacheControl();
    cacheControl.setNoCache(true);
    cacheControl.setNoStore(true);

    if(queryParams != null && queryParams.size() > 0) {
      try {
        PropertiesConfiguration exoConf = getExoConfiguration();
        Properties systemProperties = System.getProperties();
  
        // Fetch all properties stores by user
        for(Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
          String ppName = entry.getKey();
          String ppValue = entry.getValue().get(0);
          
         if(ppName != null && ppValue != null) {
            if(exoConf.containsKey(ppName)) {
              // If property exists we update it
              exoConf.setProperty(ppName, ppValue);
            }
            else {
              // Else we add to file
              exoConf.addProperty(ppName, ppValue);
            }
            // Update System Properties
            systemProperties.setProperty(ppName, ppValue);
          }
        }
        exoConf.save();
        isOk = true;
      }
      catch (ConfigurationException e) {
        logger.error("Cannot save Properties configuration", e);
      }
      catch(WizardPropertiesException e) {
        logger.error("Cannot get exo configuration", e);
      }
      catch(Exception e) {
        logger.error("Problem with WS writeProperties", e);
      }
    }
    
    return Response.ok(isOk ? "ok" : "nok").cacheControl(cacheControl).build();
  }
  
  /**
   * This WS launches the portal container. If launch is well done, the string "ok" is returned. 
   * Else "nok" is returned.
   * @return
   */
  @BadgerFish
  @GET
  @Path("/sp")
  @Produces(MediaType.APPLICATION_JSON)
  public Response startPlatform() {

    if(logger.isDebugEnabled()) {
      logger.debug("startPlatform is called");
    }
    
    CacheControl cacheControl = new CacheControl();
    cacheControl.setNoCache(true);
    cacheControl.setNoStore(true);
    
    boolean isOk = false;
    
    try {
      // Start the server into a Thread
      Thread t = new Thread() {
        public void run() {
          final RootContainer rootContainer = RootContainer.getInstance();
          SecurityHelper.doPrivilegedAction(new PrivilegedAction<Void>() {
            public Void run() {
               rootContainer.createPortalContainers();
               return null;
            }
          });
        }
      };
      t.start();
      isOk = true;
    }
    catch(Exception e) {
      logger.error("Cannot start platform", e);
    }
    
    if(isOk) {
      // Launches tail(s)
      List<String> logsServerPath = WizardProperties.getExoLogsServerPath(WizardUtility.getCurrentServerName());
      WizardTailService.getInstance().launchesTails(WizardUtility.formatExoServerLogsPath(logsServerPath));
    }
    
    return Response.ok().entity(isOk ? "ok" : "nok").cacheControl(cacheControl).build();
  }
  
  /**
   * During Portal starting, server logs are written into files. this WS permit to get some of these logs.
   * Like errors or exceptions.
   * @return
   */
  @BadgerFish
  @GET
  @Path("/il")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getImportantServerLogs() {
    
    if(logger.isDebugEnabled()) {
      logger.debug("getImportantServerLogs is called");
    }

    CacheControl cacheControl = new CacheControl();
    cacheControl.setNoCache(true);
    cacheControl.setNoStore(true);
    
    ImportantLogsDto dto = new ImportantLogsDto();
    dto.setLogs(WizardTailService.getInstance().getElements());
    
    return Response.ok().entity(dto).cacheControl(cacheControl).build();
  }
  
  /**
   * WS used to get all usefull information before setup display
   * <p>
   * like all existing exo properties, or debug information
   * @return
   */
  @BadgerFish
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
    
    return Response.ok().entity(dto).cacheControl(cacheControl).build();
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
