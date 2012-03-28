package org.exoplatform.setting.server.service;

import java.io.File;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.exoplatform.commons.utils.SecurityHelper;
import org.exoplatform.container.RootContainer;
import org.exoplatform.setting.client.data.StartupInformationDto;
import org.exoplatform.setting.client.data.WizardPlatformException;
import org.exoplatform.setting.client.data.WizardPropertiesException;
import org.exoplatform.setting.client.data.WizardTailException;
import org.exoplatform.setting.client.service.WizardService;
import org.exoplatform.setting.server.WizardProperties;
import org.exoplatform.setting.server.WizardUtility;
import org.exoplatform.setting.server.util.WizardTailService;
import org.exoplatform.setting.shared.data.SetupWizardData;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class WizardServiceImpl extends RemoteServiceServlet implements WizardService {
  
  private static Logger logger = Logger.getLogger(WizardServiceImpl.class);

  /**
   * Return a map with interesting system properties
   */
  public Map<String, String> getSystemProperties() {
    
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
    
    return map;
  }

  /**
   * Return all datasources installed into user system
   * <p>
   * If datasource context is not configured, we return an empty list
   */
  public List<String> getDatasources() {
    
    logger.debug("Client calls server method: getDatasources");
    
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
    
    return datasources;
  }

  /**
   * Save datas into configuration file
   * <p>
   * This method also updates system properties
   */
  public String writeProperties(Map<SetupWizardData, String> datas) throws WizardPropertiesException {
    
    logger.debug("Client calls server method: writeProperties");

    if(datas != null && datas.size() > 0) {
      PropertiesConfiguration exoConf = getExoConfiguration();
      Properties systemProperties = System.getProperties();

      try {
        // Make a copy of exo configuration (.old)
        String exoConfOldPath = exoConf.getPath() + ".old";
        if(! WizardUtility.isConfigurationExists(exoConfOldPath)) {
          exoConf.save(exoConfOldPath);
        }
  
        // Fetch all properties stores by user
        for(Map.Entry<SetupWizardData, String> entry : datas.entrySet()) {
          SetupWizardData data = entry.getKey();
          String ppValue = entry.getValue();
          
         if(data.getPropertyName() != null && ppValue != null) {
            if(exoConf.containsKey(data.getPropertyName())) {
              // If property exists we update it
              exoConf.setProperty(data.getPropertyName(), ppValue);
            }
            else {
              // Else we add to file
              exoConf.addProperty(data.getPropertyName(), ppValue);
            }
            // Update System Properties
            systemProperties.setProperty(data.getPropertyName(), ppValue);
          }
        }
        exoConf.save();
      }
      catch (ConfigurationException e) {
        logger.error("Cannot save Properties configuration", e);
        
        // Launches an exception to the client to not display next screen
        throw new WizardPropertiesException("Cannot save Properties configuration: " + e.getMessage());
      }
    }
    
    return null;
  }
  
  /**
   * This method start platform
   */
  public String startPlatform() throws WizardTailException, WizardPlatformException {
    
    logger.debug("Client calls server method: startPlatform");
    
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
    }
    catch(Exception e) {
      logger.error("Cannot start platform", e);
      // Launches an exception to the client to not display next screen
      throw new WizardPlatformException("Cannot start platform: " + e.getMessage());
    }
    
    // Launches tail(s)
    List<String> logsServerPath = WizardProperties.getExoLogsServerPath(WizardUtility.getCurrentServerName());
    WizardTailService.getInstance().launchesTails(WizardUtility.formatExoServerLogsPath(logsServerPath));
    
    return null;
  }
  
  /**
   * Get all startup information
   * @return
   */
  public StartupInformationDto getStartupInformation() throws WizardPropertiesException {
    
    logger.debug("Client calls server method: getStartupInformation");

    StartupInformationDto dto = new StartupInformationDto();
    
    dto.setIsDebugActivated(WizardProperties.getDebug());
    dto.setIsQuickModeActivated(WizardProperties.getQuickMode());
    dto.setFirstScreenNumber(WizardProperties.getFirstScreenNumber());
    
    // Get last build date
    File setupPropertiesFile = WizardProperties.getConf().getFile();
    Date lastBuildDate = new Date(setupPropertiesFile.lastModified());
    dto.setLastBuildDate(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(lastBuildDate));
    
    // Exo Conf, load properties
    PropertiesConfiguration exoConf = getExoConfiguration();
    Map<SetupWizardData, String> propertiesValues = new HashMap<SetupWizardData, String>();
    
    if(exoConf != null) {
      for(SetupWizardData data : SetupWizardData.values()) {
        Object ppObj = exoConf.getProperty(data.getPropertyName());
        if(ppObj != null) {
          String pp = ppObj.toString();
          pp = WizardUtility.mergeProperty(pp);
          propertiesValues.put(data, pp);
        }
      }
    }
    dto.setPropertiesValues(propertiesValues);
    
    return dto;
  }

  /**
   * Get some messages from logs server
   */
  @Override
  public List<String> getImportantServerLogs() {
    return WizardTailService.getInstance().getElements();
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
