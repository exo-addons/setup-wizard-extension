package org.exoplatform.setting.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.exoplatform.setting.client.data.SetupWizardMode;
import org.exoplatform.setting.shared.data.SetupScreen;
import org.junit.Test;

public class WizardClientUtilityTest {

  @Test
  public void testBuildLocaleUrl() {
    String url = "http://toto:8080/";
    assertEquals("http://toto:8080/?locale=fr", WizardClientUtility.buildLocaleUrl(url, null, null, "fr"));
    
    url = "http://toto:8080/?titi=2";
    assertEquals("http://toto:8080/?titi=2&locale=fr", WizardClientUtility.buildLocaleUrl(url, "?titi=2", null, "fr"));
    
    url = "http://toto:8080/?titi=2&locale=en";
    assertEquals("http://toto:8080/?titi=2&locale=fr", WizardClientUtility.buildLocaleUrl(url, "?titi=2&locale=en", "en", "fr"));
    
    url = "http://toto:8080/?titi=2&locale=en&tutu=3";
    assertEquals("http://toto:8080/?titi=2&locale=fr&tutu=3", WizardClientUtility.buildLocaleUrl(url, "?titi=2&locale=fr&tutu=3", "en", "fr"));
    
    url = "http://toto:8080/?locale=en";
    assertEquals("http://toto:8080/?locale=fr", WizardClientUtility.buildLocaleUrl(url, "?locale=en", "en", "fr"));
    
    url = "http://toto:8080/?local=en";
    assertEquals("http://toto:8080/?local=en&locale=fr", WizardClientUtility.buildLocaleUrl(url, "?local=en", null, "fr"));
    
    url = "http://toto:8080/?titi=2&tutu=3&";
    assertEquals("http://toto:8080/?titi=2&tutu=3&locale=fr", WizardClientUtility.buildLocaleUrl(url, "?titi=2&tutu=3&", null, "fr"));
    
    url = "http://toto:8080/?";
    assertEquals("http://toto:8080/?locale=fr", WizardClientUtility.buildLocaleUrl(url, "?", null, "fr"));
  }

  @Test
  public void testGetPreselectedItemIndex() {
    List<String> names = new ArrayList<String>();
    List<String> emptyNames = Collections.emptyList();
    String keyToTest = "toto";

    names.add("titi");
    names.add("tata");
    names.add("tutu");
    names.add("toto");
    names.add("tototo");
    names.add("totu");
    
    assertEquals(-1, WizardClientUtility.getPreselectedItemIndex(emptyNames, ""));
    assertEquals(-1, WizardClientUtility.getPreselectedItemIndex(emptyNames, null));
    assertEquals(-1, WizardClientUtility.getPreselectedItemIndex(null, ""));
    assertEquals(-1, WizardClientUtility.getPreselectedItemIndex(null, null));
    
    names.remove(3);
    
    assertEquals(3, WizardClientUtility.getPreselectedItemIndex(names, keyToTest));
    
    names.remove(3);
    
    assertEquals(-1, WizardClientUtility.getPreselectedItemIndex(names, keyToTest));
    
    keyToTest = "titi";
    
    assertEquals(0, WizardClientUtility.getPreselectedItemIndex(names, keyToTest));

  }

  @Test
  public void testIsScreenDisplayable() {
    
    for(SetupScreen screen : SetupScreen.values()) {
      screen.setMode(SetupWizardMode.STANDARD);
    }
    assertTrue(WizardClientUtility.isScreenDisplayable(SetupWizardMode.STANDARD, SetupScreen.IDM_DB, SetupScreen.APPLY_SETTINGS));
    assertTrue(WizardClientUtility.isScreenDisplayable(SetupWizardMode.ADVANCED, SetupScreen.IDM_DB, SetupScreen.APPLY_SETTINGS));
    assertTrue(WizardClientUtility.isScreenDisplayable(SetupWizardMode.ADVANCED, SetupScreen.IDM_DB, SetupScreen.APPLY_SETTINGS));
    assertTrue(WizardClientUtility.isScreenDisplayable(SetupWizardMode.ADVANCED, SetupScreen.IDM_DB, SetupScreen.JCR_DB));
    assertTrue(WizardClientUtility.isScreenDisplayable(SetupWizardMode.ADVANCED, SetupScreen.IDM_DB, SetupScreen.SETUP_INFO));
    assertTrue(WizardClientUtility.isScreenDisplayable(SetupWizardMode.ADVANCED, SetupScreen.IDM_DB, SetupScreen.WEBSITES));
    assertTrue(WizardClientUtility.isScreenDisplayable(SetupWizardMode.ADVANCED, SetupScreen.IDM_DB, SetupScreen.SUPER_USER));
    assertTrue(WizardClientUtility.isScreenDisplayable(SetupWizardMode.ADVANCED, SetupScreen.IDM_DB, SetupScreen.IDM_DB));
    assertTrue(WizardClientUtility.isScreenDisplayable(SetupWizardMode.ADVANCED, SetupScreen.IDM_LDAP, SetupScreen.IDM_LDAP));
    assertFalse(WizardClientUtility.isScreenDisplayable(SetupWizardMode.ADVANCED, SetupScreen.IDM_LDAP, SetupScreen.IDM_DB));
    assertFalse(WizardClientUtility.isScreenDisplayable(SetupWizardMode.ADVANCED, SetupScreen.IDM_DB, SetupScreen.IDM_LDAP));
    
    SetupScreen.APPLY_SETTINGS.setMode(SetupWizardMode.ADVANCED);
    assertFalse(WizardClientUtility.isScreenDisplayable(SetupWizardMode.STANDARD, SetupScreen.IDM_DB, SetupScreen.APPLY_SETTINGS));
    assertTrue(WizardClientUtility.isScreenDisplayable(SetupWizardMode.ADVANCED, SetupScreen.IDM_DB, SetupScreen.APPLY_SETTINGS));
  }
}
