package org.exoplatform.setting.shared.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.exoplatform.setting.shared.data.SetupScreen;
import org.junit.Test;

public class SetupScreenTest {

  @Test
  public void testIsBefore() {
    assertTrue(SetupScreen.JCR_DB.isBefore(SetupScreen.IDM_DB));
    assertTrue(SetupScreen.SETUP_INFO.isBefore(SetupScreen.ERROR));
    assertFalse(SetupScreen.SETUP_INFO.isBefore(SetupScreen.SETUP_INFO));
    assertFalse(SetupScreen.WEBSITES.isBefore(SetupScreen.CHAT_SERVER));
    assertFalse(SetupScreen.WEBSITES.isBefore(null));
  }
}
