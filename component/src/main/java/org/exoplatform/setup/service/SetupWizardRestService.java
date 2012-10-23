package org.exoplatform.setup.service;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.exoplatform.commons.utils.SecurityHelper;
import org.exoplatform.container.RootContainer;
import org.exoplatform.setup.data.*;
import org.exoplatform.setup.util.WizardProperties;
import org.exoplatform.setup.util.WizardUtility;
import org.jboss.resteasy.annotations.providers.jaxb.json.BadgerFish;

import javax.naming.*;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.security.PrivilegedAction;
import java.util.*;

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

  /**
   * This WS permits to get the properties of the user's machine in order to display it in the first screen.
   */
  @BadgerFish
  @GET
  @Path("/pp")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getSystemProperties() {
    logger.debug("Client calls server method: getSystemProperties");

    Map<String, String> map = new LinkedHashMap<String, String>();

    // Get server name
    map.put("server.name", WizardUtility.getCurrentServerName());
    map.put("server.home", WizardUtility.getCurrentServerHome());

    // Get some system properties
    map.put("exo.conf.dir.name", System.getProperty("exo.conf.dir.name"));
    map.put("exo.product.developing", System.getProperty("exo.product.developing"));
    map.put("exo.profiles", System.getProperty("exo.profiles"));
    map.put("file.encoding", System.getProperty("file.encoding"));
    map.put("gatein.data.dir", System.getProperty("gatein.data.dir"));
    map.put("java.home", System.getProperty("java.home"));
    map.put("java.runtime.name", System.getProperty("java.runtime.name"));
    map.put("java.runtime.version", System.getProperty("java.runtime.version"));
    map.put("java.specification.version", System.getProperty("java.specification.version"));
    map.put("java.version", System.getProperty("java.version"));
    map.put("os.arch", System.getProperty("os.arch"));
    map.put("os.name", System.getProperty("os.name"));
    map.put("os.version", System.getProperty("os.version"));
    map.put("user.country", System.getProperty("user.country"));
    map.put("user.dir", System.getProperty("user.dir"));
    map.put("user.home", System.getProperty("user.home"));
    map.put("user.language", System.getProperty("user.language"));
    map.put("user.name", System.getProperty("user.name"));

    return Response.ok(new SystemPropertiesDto(map), MediaType.APPLICATION_JSON).build();
  }

  /**
   *  This WS permits to get the Datasources defined in the server.
   */
  @BadgerFish
  @GET
  @Path("/ds")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getDatasources() {
    // Get JNDI Name according to the sever name
    String DATASOURCE_CONTEXT = WizardUtility.getDatasourceJndiName(WizardUtility.getCurrentServerName());
    String DATASOURCE_CONTEXT_PP_NAME = WizardUtility.getDatasourceJndiPropertyName(WizardUtility.getCurrentServerName());

    List<String> datasources = new ArrayList<String>();

    if(DATASOURCE_CONTEXT != null) {
        try {
            Context initialContext = new InitialContext();
            Context namingContext = (Context) initialContext.lookup(DATASOURCE_CONTEXT);
            if (namingContext != null) {
                NamingEnumeration<Binding> nenum = namingContext.listBindings("");
                while(nenum.hasMore()) {
                    Binding binding = (Binding) nenum.next();
                    Object ds = namingContext.lookup(binding.getName());
                    if(ds instanceof DataSource) {
                        datasources.add(DATASOURCE_CONTEXT + WizardUtility.filterWithGateinSpecificity(binding.getName()));
                    }
                  }
              }
              else {
                  logger.error("Failed to lookup datasource.");
              }
        }
        catch (NamingException ex) {
            logger.error("Cannot get connection, maybe datasource jndi name is not correct (" + DATASOURCE_CONTEXT + "). Try to change property (" + DATASOURCE_CONTEXT_PP_NAME + ")");
        }
    }

    return Response.ok(new DatasourcesDto(datasources), MediaType.APPLICATION_JSON).build();
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
          String ppName = SetupWizardData.getPropertyName(entry.getKey());
          String ppValue = entry.getValue().get(0);
          
         if(ppName != null && ppName.length() > 0 && ppValue != null && ppValue.length() > 0) {
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
            propertiesValues.put(data.getPropertyIndex(), pp);
          }
        }
      }
      dto.setPropertiesValues(propertiesValues);
    }
    catch(Exception e) {
      logger.error("Cannot get startup informations: " + e.getMessage(), e);
    }
    
    return Response.ok(dto).cacheControl(cacheControl).build();
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
