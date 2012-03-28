package org.exoplatform.setting.client.ui.view;

import java.util.List;

import org.exoplatform.setting.client.data.SetupWizardMode;
import org.exoplatform.setting.client.ui.controller.SetupWizardController;
import org.exoplatform.setting.shared.data.SetupScreen;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

/**
 * View Apply Setting
 * 
 * @author Clement
 *
 */
public class ApplyWizardView extends WizardView {
  
  private static final int REFRESH_INTERVAL = 500; // ms
  
  private Grid serverLogsResume;
  private Button finishButton;
  private Timer refreshTimer;
  
  public ApplyWizardView(SetupWizardController controller, SetupWizardMode mode) {
    super(controller, SetupScreen.APPLY_SETTINGS);
  }

  @Override
  protected String getWizardTitle() {
    return constants.applySettings();
  }

  @Override
  protected String getWizardDescription() {
    return constants.applySettingsDescription();
  }

  @Override
  protected Widget buildStepToolbar() {
    
    FlowPanel panel = new FlowPanel();
    finishButton = prepareButton(constants.finish(), SetupScreen.SETUP_INFO);
    panel.add(finishButton);
    
    return panel;
  }

  @Override
  protected Widget buildStepContent() {
    
    serverLogsResume = new Grid(1, 1);
    serverLogsResume.setCellSpacing(6);
    serverLogsResume.setStylePrimaryName("table_logs");
    
    Grid advancedOptions = new Grid(1, 1);
    advancedOptions.setCellSpacing(6);
    advancedOptions.setWidget(0, 0, serverLogsResume);
    
    return advancedOptions;
  }

  @Override
  public void executeOnDisplay() {
    
    if(!controller.isQuickModeActivated()) {
      finishButton.setEnabled(false);
  
      // Start a timer to refresh screen regularly
      refreshTimer = new Timer() {
        @Override
        public void run() {
          refreshScreen();
        }
      };
      refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
      
      // Build callback method to start platform
      AsyncCallback<String> callback = new AsyncCallback<String>() {
  
        public void onFailure(Throwable arg0) {
          controller.displayError(arg0);
          //refreshTimer.cancel();
          finishButton.setEnabled(true);
        }
  
        public void onSuccess(String arg0) {
          finishButton.setEnabled(true);
          controller.displayMessage("Server is started !");
          //refreshTimer.cancel();
        }
      };
      controller.startPlatform(callback);
    }
  }
  
  private void refreshScreen() {
    // Server call to get exceptions
    AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {

      public void onFailure(Throwable arg0) {
        controller.displayError(arg0);
      }

      public void onSuccess(List<String> arg0) {
        if(arg0 != null && arg0.size() > 0) {
          for(String element : arg0) {
            //serverLogsResume.setText(serverLogsResume.getText() + "\n" + element);
            serverLogsResume.setHTML(serverLogsResume.getRowCount()-1, 0, element);
            serverLogsResume.resize(serverLogsResume.getRowCount()+1, serverLogsResume.getColumnCount());
          }
          // Scroll textArea
          scrollContentToBottom();
        }
      }
    };
    controller.getImportantServerLogs(callback);
  }
  
}
