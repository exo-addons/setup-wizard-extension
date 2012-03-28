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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class FileSetupWizardView extends WizardView {

  TextBox logs;
  TextBox index;
  TextBox dataValues;
  
  public FileSetupWizardView(SetupWizardController controller, SetupWizardMode mode) {
    super(controller, SetupScreen.FILESYSTEM);
  }

  @Override
  protected String getWizardTitle() {
    return constants.fileSystemSetup();
  }

  @Override
  protected String getWizardDescription() {
    return constants.fileSystemSetupDesc();
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

    logs = new TextBox();
    index = new TextBox();
    dataValues = new TextBox();

    Grid table = new Grid(3, 2);
    table.setCellSpacing(6);
    table.setHTML(0, 0, constants.logs());
    table.setWidget(0, 1, logs);
    table.setHTML(1, 0, constants.index());
    table.setWidget(1, 1, index);
    table.setHTML(2, 0, constants.dataValues());
    table.setWidget(2, 1, dataValues);
    
    // For this screen we prefill fields all the time
    preFillFields();
    
    return table;
  }

  @Override
  public Map<SetupWizardData, String> verifyDatas() throws WizardInvalidFieldException {
    
    if(! WizardFieldVerifier.isValidTextField(logs.getText())) {
      throw new WizardInvalidFieldException(constants.invalidLogsDirectory());
    }

    if(! WizardFieldVerifier.isValidTextField(index.getText())) {
      throw new WizardInvalidFieldException(constants.invalidIndexDirectory());
    }

    if(dataValues.isEnabled()) {
      if(! WizardFieldVerifier.isValidTextField(dataValues.getText())) {
        throw new WizardInvalidFieldException(constants.invalidDataValuesDirectory());
      }
    }
    
    Map<SetupWizardData, String> datas = new HashMap<SetupWizardData, String>();
    datas.put(SetupWizardData.FS_LOGS, logs.getText());
    datas.put(SetupWizardData.FS_INDEX, index.getText());
    
    if(dataValues.isEnabled()) {
      datas.put(SetupWizardData.FS_DATA_VALUES, dataValues.getText());
    }
    
    return datas;
  }

  @Override
  public void executeOnDisplay() {
    
    // If StoreFiles is unchecked we disable dataValues
    if(! controller.isStoreFilesChecked()) {
      dataValues.setEnabled(false);
    }
    else {
      dataValues.setEnabled(true);
    }
  }
  
  @Override
  public void preFillFields() {
    // Get exo conf with startup infos DTO
    logs.setText(controller.getPropertyValue(SetupWizardData.FS_LOGS));
    index.setText(controller.getPropertyValue(SetupWizardData.FS_INDEX));
    dataValues.setText(controller.getPropertyValue(SetupWizardData.FS_DATA_VALUES));
  }
}
