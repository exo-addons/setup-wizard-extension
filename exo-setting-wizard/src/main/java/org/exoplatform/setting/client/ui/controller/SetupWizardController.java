package org.exoplatform.setting.client.ui.controller;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.exoplatform.setting.client.data.SetupWizardMode;
import org.exoplatform.setting.client.data.StartupInformationDto;
import org.exoplatform.setting.client.service.WizardService;
import org.exoplatform.setting.client.service.WizardServiceAsync;
import org.exoplatform.setting.client.ui.model.DatabaseIdmWizardModel;
import org.exoplatform.setting.client.ui.model.DatabaseJcrWizardModel;
import org.exoplatform.setting.client.ui.model.LdapConfigWizardModel;
import org.exoplatform.setting.client.ui.model.SystemInfoWizardModel;
import org.exoplatform.setting.client.ui.model.WizardModel;
import org.exoplatform.setting.client.ui.view.ApplyWizardView;
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
import org.exoplatform.setting.client.ui.view.WizardDialogBox;
import org.exoplatform.setting.client.ui.view.WizardView;
import org.exoplatform.setting.shared.WizardClientUtility;
import org.exoplatform.setting.shared.data.SetupScreen;
import org.exoplatform.setting.shared.data.SetupWizardData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class SetupWizardController {

  // Create a remote service proxy to talk to the server-side Wizard service.
  private final WizardServiceAsync wizardService = GWT.create(WizardService.class);
  
  // GUI elements
  private LinkedHashMap<SetupScreen, WizardView> views;
  private LinkedHashMap<SetupScreen, WizardModel> models;
  private LinkedList<Integer> loadedModels;
  private int nbModels = 0;
  private int nbViews = 0;
  private WizardDialogBox messageDialogBox;
  
  // Client Mode to show/hide some screens
  private SetupWizardMode setupWizardMode;
  private SetupScreen setupWizardIdmtype;
  
  // Screens datas
  private Map<SetupWizardData, String> setupWizardDatas;
  
  private static StartupInformationDto dtoStartupInformation;
  
  
  public void start() {
    
    // Call to server
    initStartupInformation();
    
    // Create the dialog box
    messageDialogBox = new WizardDialogBox();
    
    // Initialize setup mode
    setupWizardMode = SetupWizardMode.STANDARD;
    
    // Initialize datas
    setupWizardDatas = new LinkedHashMap<SetupWizardData, String>();

    // Models init
    models = new  LinkedHashMap<SetupScreen, WizardModel>();
    models.put(SetupScreen.SETUP_INFO, new SystemInfoWizardModel(this, 0));
    models.put(SetupScreen.JCR_DB, new DatabaseJcrWizardModel(this, 3));
    models.put(SetupScreen.IDM_DB, new DatabaseIdmWizardModel(this, 5));
    models.put(SetupScreen.IDM_LDAP, new LdapConfigWizardModel(this, 6));
    
    // Views init
    views = new  LinkedHashMap<SetupScreen, WizardView>();
    views.put(SetupScreen.SETUP_INFO, new SystemWizardView(this, SetupWizardMode.STANDARD));
    views.put(SetupScreen.SETUP_TYPE, new SetupTypeWizardView(this, SetupWizardMode.STANDARD));
    views.put(SetupScreen.SUPER_USER, new SuperUserWizardView(this, SetupWizardMode.ADVANCED));
    views.put(SetupScreen.JCR_DB, new JcrDBWizardView(this, SetupWizardMode.STANDARD));
    views.put(SetupScreen.IDM_SETUP, new IdmSetupWizardView(this, SetupWizardMode.STANDARD));
    views.put(SetupScreen.IDM_DB, new IdmDBWizardView(this, SetupWizardMode.STANDARD));
    views.put(SetupScreen.IDM_LDAP, new LdapWizardView(this, SetupWizardMode.STANDARD));
    views.put(SetupScreen.FILESYSTEM, new FileSetupWizardView(this, SetupWizardMode.STANDARD));
    views.put(SetupScreen.MAIL_SETTINGS, new MailWizardView(this, SetupWizardMode.STANDARD));
    //views.put(SetupWizardViewType.CHAT_SERVER, new ChatWizardView(this, SetupWizardMode.ADVANCED));
    views.put(SetupScreen.WEBSITES, new WebsiteWizardView(this, SetupWizardMode.ADVANCED));
    views.put(SetupScreen.SUMMARY, new SummaryWizardView(this, SetupWizardMode.STANDARD));
    views.put(SetupScreen.APPLY_SETTINGS, new ApplyWizardView(this, SetupWizardMode.STANDARD));
    views.put(SetupScreen.ERROR, new ErrorWizardView(this, SetupWizardMode.STANDARD));

    /*************
     * The client-side of GWT does not have access to the whole Java API
     * So we cannot use java.lang.reflect.Constructor into client side
     */
    // New instance of each active view need to be indexed into controller
    /*for(SetupScreen screen : SetupScreen.values()) {
      if(screen.isActive()) {
        try {
          java.lang.reflect.Constructor constructeur = screen.getScreenClass().getConstructor(new Class[] {SetupWizardController.class, SetupScreen.class});
          WizardView view = (WizardView) constructeur.newInstance (new Object [] {this, screen});
          views.put(screen, view);
        }
        catch(Exception e) {
          displayError(e);
        }
      }
    }*/
    
    nbModels = models.size();
    nbViews = views.size();
    
    executeModels();
  }



  /*=======================================================================
   * GUI methods
   *======================================================================*/

  /**
   * This method need to be called by all models. When all models are loaded
   * So, views are built and displayed.
   */
  public void fireModelLoaded(int screenNumber) {
    if(loadedModels == null) {
      loadedModels = new LinkedList<Integer>();
    }
    loadedModels.add(screenNumber);
    
    // All models are loaded, so we can display Setup Wizard
    if(loadedModels.size() == nbModels) {
      buildViews();
      displaySetupWizard();
    }
  }
  
  /**
   * Load all datas needed by views
   */
  private void executeModels() {
    if(models != null) {
      for(Map.Entry<SetupScreen, WizardModel> entry : models.entrySet()) {
        entry.getValue().initDatas();
      }
    }
  }
  
  /**
   * Build views and add in HTML
   */
  private void buildViews() {
    for(Map.Entry<SetupScreen, WizardView> entry : views.entrySet()) {
      entry.getValue().build();
      RootPanel.get("mainBlock").add(entry.getValue());
    }
  }
  
  public WizardModel getModel(SetupScreen screenType) {
    return models.get(screenType);
  }
  
  /**
   * First displaying of SetupWizard
   */
  private void displaySetupWizard() {
    // Hide loading
    RootPanel.get("loadingBlock").setVisible(false);
    
    // Display mainBlock
    RootPanel.get("mainBlock").setVisible(true);
    
    // Display mainBlock
    RootPanel.get("stepBlock").setVisible(true);
    
    // Display First Screen
    displayScreen(SetupScreen.SETUP_INFO);
  }
  
  /**
   * Display screen #index 
   * 
   * @param index
   */
  public void displayScreen(SetupScreen screen) {
    
    if(screen != null) {
      WizardView screenView = views.get(screen);
  
      if(! isQuickModeActivated()) {
        
        // If screen is not displayable, choose another one
        if(! WizardClientUtility.isScreenDisplayable(setupWizardMode, setupWizardIdmtype, screenView.getScreen())) {
          displayScreen(screen.getNextScreen());
        }
      }
      
      // Hide all screens
      for(Map.Entry<SetupScreen, WizardView> entry : views.entrySet()) {
        entry.getValue().hide();
      }
      
      screenView.executeOnDisplay();
      screenView.display();
    }
  }
  
  /**
   * Display an error in a dialog box
   * @param error
   */
  public void displayError(String error) {
    messageDialogBox.displayError(error);
  }
  
  /**
   * Display a message + stackTrace
   * <p>
   * If debug is not activated, displays an error message only
   * 
   * @param error
   * @param arg0
   */
  public void displayError(Throwable arg0) {
    String error = "";
    if(arg0 != null) {
      if(arg0.getMessage() != null) {
        error += arg0.getMessage() + "<br />";
      }
      else if(arg0.getLocalizedMessage() != null) {
        error += arg0.getLocalizedMessage() + "<br />";
      }
      if(arg0.getCause() != null) {
        error = "<pre>" + printStackTrace(arg0.getCause().getStackTrace()) + "</pre>";
      }
      else {
        error = "<pre>" + printStackTrace(arg0.getStackTrace()) + "</pre>";
      }
    }

    messageDialogBox.displayError(error);
  }
  
  private String printStackTrace(Object[] stackTrace) {
    String output = "";
    for (Object line : stackTrace) {
      output += "&nbsp;&nbsp;&nbsp;&nbsp;" + line + "<br/>";
    }
    return output;
  }
  
  /**
   * Display a message in a dialog box
   * @param message
   */
  public void displayMessage(String message) {
    messageDialogBox.displayMessage(message);
  }


  /*=======================================================================
   * Controller methods (client)
   *======================================================================*/
  
  /**
   * Stores datas
   * @param datas
   */
  public void storeDatas(Map<SetupWizardData, String> datas) {

    if(datas != null && datas.size() > 0) {
      // Add datas
      setupWizardDatas.putAll(datas);
    }
  }

  public SetupWizardMode getSetupWizardMode() {
    return setupWizardMode;
  }

  public void setSetupWizardMode(SetupWizardMode setupWizardMode) {
    this.setupWizardMode = setupWizardMode;
  }
  
  public Map<SetupWizardData, String> getSetupWizardDatas() {
    return this.setupWizardDatas;
  }
  
  public int getNbViews() {
    return nbViews;
  }
  
  public boolean isDebugActivated() {
    boolean isDebugActivated = false;
    if(dtoStartupInformation != null) {
      isDebugActivated = dtoStartupInformation.getIsDebugActivated().booleanValue();
    }
    return isDebugActivated;
  }
  
  public boolean isQuickModeActivated() {
    boolean isQuickModeActivated = false;
    if(dtoStartupInformation != null) {
      isQuickModeActivated = dtoStartupInformation.getIsQuickModeActivated().booleanValue();
    }
    return isQuickModeActivated;
  }
  
  public String getLastBuildDate() {
    String lastBuildDate = "";
    if(dtoStartupInformation != null) {
      lastBuildDate = dtoStartupInformation.getLastBuildDate();
    }
    return lastBuildDate;
  }

  public String getPropertyValue(SetupWizardData data) {
    String ppValue = "";
    if(dtoStartupInformation != null) {
      ppValue = dtoStartupInformation.getPropertiesValues().get(data);
    }
    return ppValue;
  }
  
  public boolean isStoreFilesChecked() {
    boolean isChecked = false;
    
    String ppValue = setupWizardDatas.get(SetupWizardData.STORE_FILES_IN_DB);
    if(ppValue != null) {
      isChecked = ppValue.equals("true");
    }
    
    return isChecked;
  }

  public SetupScreen getSetupWizardIdmMode() {
    return setupWizardIdmtype;
  }

  public void setSetupWizardIdmMode(SetupScreen setupWizardIdmtype) {
    this.setupWizardIdmtype = setupWizardIdmtype;
  }


  /*=======================================================================
   * Server methods (between client and server)
   *======================================================================*/
  
  /**
   * Call to server to get all properties displayed to user
   * @return
   */
  public Map<String, String> getSystemInfoProperties(AsyncCallback<Map<String, String>> callback) {
    wizardService.getSystemProperties(callback);
    
    return null;
  }
  
  /**
   * Call to server to get all datasources installed on server
   * @return
   */
  public Map<String, String> getDatasources(AsyncCallback<List<String>> callback) {
    wizardService.getDatasources(callback);
    
    return null;
  }
  
  /**
   * Call to server to get all properties displayed to user
   * @return
   */
  public String writeProperties(AsyncCallback<String> callback) {
    wizardService.writeProperties(setupWizardDatas, callback);
    
    return null;
  }
  
  /**
   * Call to server to get all properties displayed to user
   * @return
   */
  public String startPlatform(AsyncCallback<String> callback) {
    wizardService.startPlatform(callback);
    
    return null;
  }
  
  /***
   * Call to server to get exceptions launched by server, if exists
   * @return
   */
  public List<String> getImportantServerLogs(AsyncCallback<List<String>> callback) {
    wizardService.getImportantServerLogs(callback);
    
    return null;
  }

  /**
   * Call to the server to get some startup informations
   */
  private void initStartupInformation() {
    AsyncCallback<StartupInformationDto> callback = new AsyncCallback<StartupInformationDto>() {
      public void onFailure(Throwable arg0) {
        displayError(arg0.getMessage());
        displayScreen(SetupScreen.ERROR);
      }
      public void onSuccess(StartupInformationDto dto) {
        dtoStartupInformation = dto;
      }
    };
    wizardService.getStartupInformation(callback);
  }
 
}


