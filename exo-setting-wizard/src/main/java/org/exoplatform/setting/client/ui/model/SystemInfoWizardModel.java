package org.exoplatform.setting.client.ui.model;

import java.util.Map;

import org.exoplatform.setting.client.ui.controller.SetupWizardController;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class SystemInfoWizardModel extends WizardModel {

  private Map<String, String> systemInfoOptions;

  public SystemInfoWizardModel(SetupWizardController controller, int screenNumber) {
    super(controller, screenNumber);
  }

  @Override
  public void initDatas() {

    // Build callback method to get system properties
    AsyncCallback<Map<String, String>> callbackSystemProperties = new AsyncCallback<Map<String, String>>() {

      public void onFailure(Throwable arg0) {
        controller.displayError(arg0);
      }

      public void onSuccess(Map<String, String> arg0) {
        systemInfoOptions = arg0;
        controller.fireModelLoaded(screenNumber);
      }
    };
    controller.getSystemInfoProperties(callbackSystemProperties);
  }

  public Map<String, String> getSystemInfoOptions() {
    return systemInfoOptions;
  }

}
