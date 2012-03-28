package org.exoplatform.setting.client.ui.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.exoplatform.setting.client.data.SetupWizardMode;
import org.exoplatform.setting.client.data.WizardInvalidFieldException;
import org.exoplatform.setting.client.ui.controller.SetupWizardController;
import org.exoplatform.setting.client.ui.model.DatabaseIdmWizardModel;
import org.exoplatform.setting.shared.WizardClientUtility;
import org.exoplatform.setting.shared.data.SetupScreen;
import org.exoplatform.setting.shared.data.SetupWizardData;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class IdmDBWizardView extends WizardView {

  RadioButton chooseDsRadio;
  RadioButton setDsRadio;
  ListBox dsList;
  TextBox newDsText;

  private DatabaseIdmWizardModel model;
  
  
  public IdmDBWizardView(SetupWizardController controller, SetupWizardMode mode) {
    super(controller, SetupScreen.IDM_DB);
    
    model = (DatabaseIdmWizardModel) getModel();
  }

  @Override
  protected String getWizardTitle() {
    return constants.idmDbSetup();
  }

  @Override
  protected String getWizardDescription() {
    return constants.idmDbSetupDesc();
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

    chooseDsRadio = new RadioButton("idmDbSetup", constants.chooseDs());
    setDsRadio = new RadioButton("idmDbSetup", constants.setYourDs());
    dsList = new ListBox();
    newDsText = new TextBox();
    chooseDsRadio.setValue(true);
    dsList.setWidth("250px");
    newDsText.setWidth("243px");
    
    // Get datasources in model
    List<String> dss = model.getDatasources();
    if(dss != null && dss.size() > 0) {
      for(String ds : dss) {
        dsList.addItem(ds);
      }
    }
    else {
      dsList.addItem(constants.noDs());
      dsList.setEnabled(false);
      chooseDsRadio.setEnabled(false);
      setDsRadio.setValue(true);
    }
    
    // Pre select an item
    int preselectedItem = WizardClientUtility.getPreselectedItemIndex(dss, "idm");
    if(preselectedItem >= 0) {
      dsList.setItemSelected(preselectedItem, true);
    }

    Grid table = new Grid(2, 2);
    table.setCellSpacing(6);
    table.setWidget(0, 0, chooseDsRadio);
    table.setWidget(0, 1, dsList);
    table.setWidget(1, 0, setDsRadio);
    table.setWidget(1, 1, newDsText);
    
    return table;
  }

  @Override
  public Map<SetupWizardData, String> verifyDatas() throws WizardInvalidFieldException {
    
    String datasource = "";
    if(setDsRadio.getValue().equals(true)) {
      if(newDsText.getText() == null || newDsText.getText().equals("")) {
        throw new WizardInvalidFieldException(constants.indicateYourDs());
      }
      else {
        datasource = newDsText.getText();
      }
    }
    else {
      datasource = dsList.getItemText(dsList.getSelectedIndex());
    }

    Map<SetupWizardData, String> datas = new HashMap<SetupWizardData, String>();
    datas.put(SetupWizardData.IDM_DATA_SOURCE, datasource);
    
    return datas;
  }
  
  @Override
  public void preFillFields() {
    newDsText.setText("MyIdmDS");
  }
}
