package org.exoplatform.setup.util;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.exoplatform.container.monitor.jvm.J2EEServerInfo;

/**
 * This class contains only static and testable methods.
 * <p>
 * Provides some utility methods
 * 
 * @author Clement
 *
 */
public class WizardUtility {
  
  /**
   * J2EEServerInfo is a class packaged into platform
   */
  private static J2EEServerInfo j2eeServerInfo;
  private static J2EEServerInfo getJ2EEServerInfo() {
    if(j2eeServerInfo == null) {
      j2eeServerInfo = new J2EEServerInfo();
    }
    return j2eeServerInfo;
  }

  /*=======================================================================
   * Non testable methods
   *======================================================================*/
  
  /**
   * 
   * @return current server name
   */
  public static String getCurrentServerName() {
    return getJ2EEServerInfo().getServerName();
  }
  
  /**
   * 
   * @return current server home path
   */
  public static String getCurrentServerHome() {
    return getJ2EEServerInfo().getServerHome();
  }
  
  /**
   * Returns jndi name according to the server type 
   * 
   * @param serverType tomcat, jboss, ...
   * @return
   */
  public static String getDatasourceJndiName(String serverName) {
    return WizardProperties.getDatasourceJndiName(serverName);
  }
  
  public static String getDatasourceJndiPropertyName(String serverName) {
    return WizardProperties.getDatasourceJndiPropertyName(serverName);
  }
  
  /**
   * 
   * @return Path of Exo configuration properties file
   */
  public static String getExoConfigurationPropertiesPath(String serverName) {
    String path = getCurrentServerHome() + "/";
    path += WizardProperties.getExoConfigurationPropertiesPath(serverName);
    return path;
  }
  
  /**
   * Get words which are matched by tail system
   * @return
   */
  public static String getTailMatchWordsRegexp(String serverName) {
    List<String> words = WizardProperties.getTailMatchWords(serverName);
    return concatToRegexp(words);
  }
  
  /**
   * Get words which end tail process
   * @return
   */
  public static String getTailEndWordsRegexp(String serverName) {
    List<String> words = WizardProperties.getTailEndWords(serverName);
    return concatToRegexp(words);
  }

  
  /*=======================================================================
   * Testable methods
   *======================================================================*/
  
  /**
   * GateIn add a suffix to the datasource, so we need to delete this suffix
   * before write it to the properties file.
   * 
   * @param strToFilter
   * @return
   */
  public static String filterWithGateinSpecificity(String strToFilter) {
    String finalString = strToFilter;
    if(strToFilter != null && strToFilter.length() > 0) {
      finalString = strToFilter.replaceAll("_portal$", "");
    }
    return finalString;
  }
  
  /**
   * Returns path where Exo logs are written
   * <p>
   * If string ${date} is found, replaces it with current date
   * @param serverName
   * @return
   */
  public static List<String> formatExoServerLogsPath(List<String> logsPaths) {
    List<String> list = new ArrayList<String>();
    
    String path = getCurrentServerHome() + "/";
    if(logsPaths != null) {
      for(String logsPath : logsPaths) {
        list.add(path + logsPath.replaceAll("\\$\\{date\\}", DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CANADA_FRENCH).format(new Date())));
      }
    }
    return list;
  }
  
  /**
   * With List [elt1, elt2, elt3]
   * returns "elt1|elt2|elt3"
   * 
   * @param words
   * @return
   */
  static String concatToRegexp(List<String> words) {
    String regexp = "";
    if(words != null && words.size() > 0) {
      regexp = ".*(";
      for(ListIterator<String> it = words.listIterator(); it.hasNext();) {
        regexp += it.next();
        if(it.hasNext()) {
          regexp += "|";
        }
      }
      regexp += ").*";
    }
    
    return regexp;
  }
  
  /**
   * Try to merge properties variables into property value
   * <p>
   * by example: toto.tata.${tutu.value} returns toto.tata.tutu (si ${tutu.value} = tutu into system properties)
   * @param propertyValue
   * @return
   */
  public static String mergeProperty(String propertyValue) {
    String property = propertyValue;
    
    if(propertyValue != null && propertyValue.length() > 0) {
      // Regexp to find all properties to replace by system properties
      Pattern p = Pattern.compile("\\$\\{([^\\}]*)\\}");
      Matcher m = p.matcher(propertyValue);
      while(m.find()) {
        String pp = m.group(1);
        String ppValue = System.getProperty(pp);
        if(ppValue != null) {
          property = property.replace("${" + pp + "}", ppValue);
        }
      }
    }
    
    return property;
  }
}
