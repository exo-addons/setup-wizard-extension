package org.exoplatform.setting.client.ui.view;

import org.exoplatform.setting.client.data.SetupWizardMode;
import org.exoplatform.setting.client.ui.controller.SetupWizardController;
import org.exoplatform.setting.shared.data.SetupScreen;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

public class ErrorWizardView extends WizardView {

  public ErrorWizardView(SetupWizardController controller, SetupWizardMode mode) {
    super(controller, SetupScreen.ERROR);
  }

  @Override
  protected String getWizardTitle() {
    return constants.errorTitle();
  }

  @Override
  protected String getWizardDescription() {
    return constants.errorDesc();
  }

  @Override
  protected Widget buildStepToolbar() {
    Grid gridToolbar = new Grid();
    return gridToolbar;
  }

  @Override
  protected Widget buildStepContent() {
    Grid gridToolbar = new Grid();
    return gridToolbar;
  }

}
