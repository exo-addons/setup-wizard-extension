package org.exoplatform.setting.shared.data;

import org.exoplatform.setting.client.data.SetupWizardMode;
import org.exoplatform.setting.client.ui.view.ApplyWizardView;
import org.exoplatform.setting.client.ui.view.ChatWizardView;
import org.exoplatform.setting.client.ui.view.ErrorWizardView;
import org.exoplatform.setting.client.ui.view.FileSetupWizardView;
import org.exoplatform.setting.client.ui.view.IdmDBWizardView;
import org.exoplatform.setting.client.ui.view.IdmSetupWizardView;
import org.exoplatform.setting.client.ui.view.JcrDBWizardView;
import org.exoplatform.setting.client.ui.view.LdapWizardView;
import org.exoplatform.setting.client.ui.view.MailWizardView;
import org.exoplatform.setting.client.ui.view.SetupTypeWizardView;
import org.exoplatform.setting.client.ui.view.SummaryWizardView;
import org.exoplatform.setting.client.ui.view.SuperUserWizardView;
import org.exoplatform.setting.client.ui.view.SystemWizardView;
import org.exoplatform.setting.client.ui.view.WebsiteWizardView;

/**
 * Configuration of screens
 * 
 * @author Clement
 *
 */
public enum SetupScreen {
  SETUP_INFO      (true,  null,           SetupWizardMode.STANDARD, SystemWizardView.class),
  SETUP_TYPE      (true,  SETUP_INFO,     SetupWizardMode.STANDARD, SetupTypeWizardView.class),
  SUPER_USER      (true,  SETUP_TYPE,     SetupWizardMode.ADVANCED, SuperUserWizardView.class),
  JCR_DB          (true,  SUPER_USER,     SetupWizardMode.STANDARD, JcrDBWizardView.class),
  IDM_SETUP       (true,  JCR_DB,         SetupWizardMode.STANDARD, IdmSetupWizardView.class),
  IDM_DB          (true,  IDM_SETUP,      SetupWizardMode.STANDARD, IdmDBWizardView.class),
  IDM_LDAP        (true,  IDM_DB,         SetupWizardMode.STANDARD, LdapWizardView.class),
  FILESYSTEM      (true,  IDM_LDAP,       SetupWizardMode.STANDARD, FileSetupWizardView.class),
  MAIL_SETTINGS   (true,  FILESYSTEM,     SetupWizardMode.STANDARD, MailWizardView.class),
  CHAT_SERVER     (false, MAIL_SETTINGS,  SetupWizardMode.ADVANCED, ChatWizardView.class),
  WEBSITES        (true,  MAIL_SETTINGS,  SetupWizardMode.ADVANCED, WebsiteWizardView.class),
  SUMMARY         (true,  WEBSITES,       SetupWizardMode.STANDARD, SummaryWizardView.class),
  APPLY_SETTINGS  (true,  SUMMARY,        SetupWizardMode.STANDARD, ApplyWizardView.class),
  ERROR           (true,  APPLY_SETTINGS, SetupWizardMode.STANDARD, ErrorWizardView.class);

  private SetupScreen previousScreen;
  private SetupScreen nextScreen;
  private boolean isActive;
  private SetupWizardMode mode;
  private Class<?> screenClass;
  
  private SetupScreen(boolean isActive, SetupScreen previousScreen, SetupWizardMode mode, Class<?> screenClass) {
    this.isActive = isActive;
    this.mode = mode;
    this.screenClass = screenClass;
    this.previousScreen = previousScreen;
    if(previousScreen != null) {
      this.previousScreen.setNextScreen(this);
    }
  }
  
  public void setNextScreen(SetupScreen nextScreen){ this.nextScreen = nextScreen; }
  public SetupScreen getPreviousScreen() { return this.previousScreen; }
  public SetupScreen getNextScreen() { return this.nextScreen; }
  public boolean hasNextScreen() { return this.nextScreen != null; }
  public boolean isActive() { return isActive; }
  public SetupWizardMode getMode() { return mode; }
  public void setMode(SetupWizardMode mode) { this.mode = mode; }
  public Class<?> getScreenClass() { return screenClass; }
  
  /**
   * Is screen in parameter is located before current screen
   * <p>
   * If screen is null return false by default
   * 
   * @param screen
   * @return
   */
  public boolean isBefore(SetupScreen screen) {
    boolean isBefore = false;
    
    if(screen != null) {
      SetupScreen tmpScreen = this;
      while(tmpScreen.hasNextScreen()) {
        tmpScreen = tmpScreen.getNextScreen();
        if(tmpScreen.equals(screen)) {
          isBefore = true;
          break;
        }
      }
    }
    
    return isBefore;
  }
}
