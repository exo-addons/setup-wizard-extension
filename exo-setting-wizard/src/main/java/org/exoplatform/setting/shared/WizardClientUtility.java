package org.exoplatform.setting.shared;

import java.util.List;

import org.exoplatform.setting.client.data.SetupWizardMode;
import org.exoplatform.setting.shared.data.SetupScreen;

/**
 * Utility class only for client side
 * @author Clement
 *
 */
public class WizardClientUtility {

  /**
   * With an url, this method build a new url with a new parameter locale
   * 
   * @param url
   * @param locale
   * @return
   */
  public static String buildLocaleUrl(String url, String queryString, String oldLocale, String newLocale) {
    String newUrl = url;
    
    if(oldLocale == null || oldLocale.length() == 0) {
      if(queryString == null || queryString.length() == 0) {
        newUrl += "?";
      }
      else if(! queryString.contains("&") && ! queryString.equals("?")) {
        newUrl += "&";
      }
      newUrl += "locale=" + newLocale;
    }
    else {
      newUrl = newUrl.replaceAll("locale=" + oldLocale, "locale=" + newLocale);
    }
    
    return newUrl;
  }
  
  /**
   * Choose an index according to the keyToTest
   * <p>
   * If keyToTest matches a name in the list, corresponding index is chosen
   * <p>
   * If there is more than one match, first is chosen
   * <p>
   * If there isn't index corresponding, -1 is returned
   * 
   * @param names
   * @param keyToTest
   * @return index
   */
  public static int getPreselectedItemIndex(List<String> names, String keyToTest) {
    
    int indexChosen = -1;
    
    if(names != null && keyToTest != null && keyToTest.length() > 0) {
      keyToTest = keyToTest.toLowerCase();
      for(int i=0; i<names.size(); i++) {
        if(names.get(i).toLowerCase().contains(keyToTest)) {
          indexChosen = i;
          break;
        }
      }
    }
    
    return indexChosen;
  }
  
  /**
   * Return if Screen is displayable according to paramaters
   * 
   * @param setupMode SetupWizard can be in mode STANDARD or ADVANCED
   * @param userMode Screen type chosen by user
   * @param idmSetup type of idm chosen by user
   * @param idmViewType type of IDM of the view
   * @return
   */
  public static boolean isScreenDisplayable(SetupWizardMode setupMode, SetupScreen idmSetup, SetupScreen idmScreen) {
    
    boolean isDisplayable = true;
    
    if(setupMode != null && idmSetup != null && idmSetup != null && idmScreen != null) {
      if(SetupWizardMode.STANDARD.equals(setupMode) && SetupWizardMode.ADVANCED.equals(idmScreen.getMode())) {
        isDisplayable = false;
      }
      else if((SetupScreen.IDM_DB.equals(idmScreen) || SetupScreen.IDM_LDAP.equals(idmScreen)) && ! idmSetup.equals(idmScreen)) {
        isDisplayable = false;
      }
    }
    
    return isDisplayable;
  }
}
