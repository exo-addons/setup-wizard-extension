package org.exoplatform.setup.util;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/***
 * Contains methods to access to properties configured into SetupWizard
 * 
 * @author Clement
 *
 */
public class WizardProperties {
  
  private static PropertiesConfiguration propertiesConfiguration;
  private static Logger logger = Logger.getLogger(WizardProperties.class);
  
  public static PropertiesConfiguration getConf() {
    if(propertiesConfiguration == null) {
      try {
        propertiesConfiguration = new PropertiesConfiguration("setup-wizard.properties");
      }
      catch (ConfigurationException e) {
        logger.error("Error during loading wizard properties: " + e.getMessage(), e);
      }
    }
    return propertiesConfiguration;
  }
  
  /**
   * Debug mode 
   * @return true or false
   */
  public static Boolean getDebug() {
    return getConf().getBoolean("exo.setupwizard.debug", false);
  }
  
  /**
   * QuickView mode 
   * @return true or false
   */
  public static Boolean getQuickMode() {
    return getConf().getBoolean("exo.setupwizard.quickmode", false);
  }
  
  /**
   * First screen number
   * @return
   */
  public static Integer getFirstScreenNumber() {
    return getConf().getInteger("exo.setupwizard.first.screen", 0);
  }
  
  /**
   * Get configuration of jndi name according to the server
   * @param serverName
   * @return
   */
  public static String getDatasourceJndiName(String serverName) {
    return getConf().getString(getDatasourceJndiPropertyName(serverName));
  }
  public static String getDatasourceJndiPropertyName(String serverName) {
    return "exo.setupwizard.datasource.jndi.name." + serverName;
  }
  
  /**
   * Path of file configuration.properties (depending on the server)
   * @param serverName
   * @return
   */
  public static String getExoConfigurationPropertiesPath(String serverName) {
    return getConf().getString("exo.setupwizard.configuration.properties.path." + serverName);
  }
  
  /**
   * Path of logs file (depending on the server)
   * @param serverName
   * @return
   */
  public static List<String> getExoLogsServerPath(String serverName) {
    return getConf().getList("exo.setupwizard.configuration.server.logs.path." + serverName);
  }
  
  /**
   * Get words which are matched by tail system
   * @return
   */
  public static List<String> getTailMatchWords(String serverName) {
    return getConf().getList("exo.setupwizard.tail.match.words." + serverName);
  }
  
  /**
   * Get words which end tail process
   * @return
   */
  public static List<String> getTailEndWords(String serverName) {
    return getConf().getList("exo.setupwizard.tail.end.words." + serverName);
  }
}
