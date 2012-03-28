package org.exoplatform.setting.client.ui.view;

import java.util.HashMap;
import java.util.Map;

import org.exoplatform.setting.client.data.SetupWizardMode;
import org.exoplatform.setting.client.data.WizardInvalidFieldException;
import org.exoplatform.setting.client.ui.controller.SetupWizardController;
import org.exoplatform.setting.shared.WizardFieldVerifier;
import org.exoplatform.setting.shared.data.SetupScreen;
import org.exoplatform.setting.shared.data.SetupWizardData;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * View corresponding to <b>STEP 0 - Setup</b>
 * 
 * @author Clement
 *
 */
public class SuperUserWizardView extends WizardView {
  
  private TextBox userName;
  private TextBox password;
  private TextBox password2;
  private TextBox email;
  
  public SuperUserWizardView(SetupWizardController controller, SetupWizardMode mode) {
    super(controller, SetupScreen.SUPER_USER);
  }

  @Override
  protected String getWizardTitle() {
    return constants.superUser();
  }

  @Override
  protected String getWizardDescription() {
    return constants.superUserDescription();
  }

  @Override
  protected Widget buildStepToolbar() {

    Grid gridToolbar = new Grid(1, 3);
    gridToolbar.setWidth("100%");
    gridToolbar.getColumnFormatter().setWidth(0, "100%");
    gridToolbar.setWidget(0, 1, preparePreviousButton());
    gridToolbar.setWidget(0, 2, prepareNextButton());
    
    return gridToolbar;
  }

  @Override
  protected Widget buildStepContent() {
    
    userName = new TextBox();
    password = new PasswordTextBox();
    password2 = new PasswordTextBox();
    email = new TextBox();

    Grid advancedOptions = new Grid(4, 2);
    advancedOptions.setCellSpacing(6);
    advancedOptions.setHTML(0, 0, constants.userName());
    advancedOptions.setWidget(0, 1, userName);
    advancedOptions.setHTML(1, 0, constants.password());
    advancedOptions.setWidget(1, 1, password);
    advancedOptions.setHTML(2, 0, constants.confirmPassword());
    advancedOptions.setWidget(2, 1, password2);
    advancedOptions.setHTML(3, 0, constants.email());
    advancedOptions.setWidget(3, 1, email);
    
    preFillFields();
    
    return advancedOptions;
  }

  @Override
  public Map<SetupWizardData, String> verifyDatas() throws WizardInvalidFieldException {
    
    if(! WizardFieldVerifier.isValidTextField(userName.getText())) {
      throw new WizardInvalidFieldException(constants.invalidUserName());
    }
    
    if(! WizardFieldVerifier.isValidPassword(password.getText())) {
      throw new WizardInvalidFieldException(constants.invalidPassword());
    }
    
    if(! WizardFieldVerifier.isValidPassword2(password.getText(), password2.getText())) {
      throw new WizardInvalidFieldException(constants.differentPasswords());
    }

    if(! WizardFieldVerifier.isValidEmail(email.getText())) {
      throw new WizardInvalidFieldException(constants.invalidMail());
    }
    
    Map<SetupWizardData, String> datas = new HashMap<SetupWizardData, String>();
    datas.put(SetupWizardData.SU_USERNAME, userName.getText());
    datas.put(SetupWizardData.SU_PASSWORD, password.getText());
    datas.put(SetupWizardData.SU_EMAIL, email.getText());
    
    return datas;
  }
  
  @Override
  public void preFillFields() {
    userName.setText(controller.getPropertyValue(SetupWizardData.SU_USERNAME));
    password.setText(controller.getPropertyValue(SetupWizardData.SU_PASSWORD));
    password2.setText(controller.getPropertyValue(SetupWizardData.SU_PASSWORD));
    email.setText(controller.getPropertyValue(SetupWizardData.SU_EMAIL));
  }
  
}
