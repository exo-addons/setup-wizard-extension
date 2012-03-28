package org.exoplatform.setting.client.ui.model;

import java.util.List;

import org.exoplatform.setting.client.ui.controller.SetupWizardController;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class DatabaseJcrWizardModel extends WizardModel {

  private List<String> datasources;

  public DatabaseJcrWizardModel(SetupWizardController controller, int screenNumber) {
    super(controller, screenNumber);
  }

  @Override
  public void initDatas() {

    // Build callback method to get datasources
    AsyncCallback<List<String>> callbackDs = new AsyncCallback<List<String>>() {

      public void onFailure(Throwable arg0) {
        controller.displayError(arg0);
      }

      public void onSuccess(List<String> arg0) {
        datasources = arg0;
        controller.fireModelLoaded(screenNumber);
      }
    };
    controller.getDatasources(callbackDs);
  }

  public List<String> getDatasources() {
    return datasources;
  }

}
