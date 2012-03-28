package org.exoplatform.setting.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Test;

public class WizardUtilityTest {

  @Test
  public void testFilterWithGateinSpecificity() {
    assertEquals("toto", WizardUtility.filterWithGateinSpecificity("toto_portal"));
    assertEquals(null, WizardUtility.filterWithGateinSpecificity(null));
    assertEquals("", WizardUtility.filterWithGateinSpecificity(""));
    assertEquals("toto_porta", WizardUtility.filterWithGateinSpecificity("toto_porta"));
    assertEquals("sdqsdqs", WizardUtility.filterWithGateinSpecificity("sdqsdqs"));
    assertEquals("", WizardUtility.filterWithGateinSpecificity("_portal"));
    assertEquals("_portal_sdqsd", WizardUtility.filterWithGateinSpecificity("_portal_sdqsd"));
  }

  @Test
  public void testFormatExoServerLogsPath() {
    assertEquals(0, WizardUtility.formatExoServerLogsPath(null).size());
    List<String> list = Collections.emptyList();
    assertEquals(0, WizardUtility.formatExoServerLogsPath(list).size());
  }

  @Test
  public void testConcatToRegexp() {
    List<String> list = new ArrayList<String>();
    List<String> emptyList = Collections.emptyList();
    list.add("toto");
    list.add("tata");
    list.add("tutu");

    assertEquals("", WizardUtility.concatToRegexp(emptyList));
    assertEquals("", WizardUtility.concatToRegexp(null));
    assertEquals(".*(toto|tata|tutu).*", WizardUtility.concatToRegexp(list));
  }

  @Test
  public void testMergeProperty() {
    System.setProperty("tutu.value", "tutu");
    System.setProperty("toto.value", "toto");
    System.setProperty("tata.value", "tata");

    assertEquals("blabla.blabla.tutu.balala.tata", WizardUtility.mergeProperty("blabla.blabla.${tutu.value}.balala.${tata.value}"));
    assertEquals("blabla.blabla.${titi.value}", WizardUtility.mergeProperty("blabla.blabla.${titi.value}"));
    assertEquals("", WizardUtility.mergeProperty(""));
    assertEquals(null, WizardUtility.mergeProperty(null));
  }
  
  @Test
  public void testIsConfigurationExists() throws ConfigurationException {
    
    File file = new File("target/test.properties");
    
    PropertiesConfiguration conf1 = new PropertiesConfiguration(file);
    conf1.setProperty("pp1", "ppValue1");
    conf1.save();

    assertTrue(WizardUtility.isConfigurationExists("target/test.properties"));
    assertFalse(WizardUtility.isConfigurationExists("target/testt.properties"));
    assertFalse(WizardUtility.isConfigurationExists(""));
    assertFalse(WizardUtility.isConfigurationExists(null));

    if (file.exists()) {
        assertTrue(file.delete());
    }
  }
}

