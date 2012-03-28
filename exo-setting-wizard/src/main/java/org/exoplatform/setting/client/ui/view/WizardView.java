package org.exoplatform.setting.client.ui.view;

import java.util.Map;

import org.exoplatform.setting.client.data.WizardInvalidFieldException;
import org.exoplatform.setting.client.i18n.WizardConstants;
import org.exoplatform.setting.client.ui.controller.SetupWizardController;
import org.exoplatform.setting.client.ui.model.WizardModel;
import org.exoplatform.setting.shared.data.SetupScreen;
import org.exoplatform.setting.shared.data.SetupWizardData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
    
/**
 * View is parent of each views into setup wizard.
 * It contains some factory methods and defines some methods to redefine.
 * 
 * @author Clement
 *
 */
public abstract class WizardView extends HorizontalPanel {
  
  // View type
  private SetupScreen currentScreen;
  
  // Panel corresponding to the body content
  private ScrollPanel uiContent;

  // i18n constants
  protected WizardConstants constants = GWT.create(WizardConstants.class);
  
  // Contains principal controller
  protected SetupWizardController controller;
  
  /**
   * Initialization
   * @param gui
   * @param title
   * @param description
   * @param stepNumber
   */
  public WizardView(SetupWizardController controller, SetupScreen type) {
    
    this.controller = controller;
    this.currentScreen = type;
  }
  

  /*=======================================================================
   * Screen Factory
   *======================================================================*/
  
  /**
   * Build a view
   */
  public void build() {
    // Construct a dock panel
    DockPanel dock = new DockPanel();
    dock.setStyleName("cw-DockPanel");
    dock.add(buildHeader(), DockPanel.NORTH);
    dock.add(buildDescription(), DockPanel.NORTH);
    dock.add(buildContent(), DockPanel.CENTER);
    dock.add(buildStepBar(), DockPanel.SOUTH);
    dock.add(buildToolbar(), DockPanel.SOUTH);

    // Configure Main panel
    setSpacing(5);
    setSize("100%", "500px");
    setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
    setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
    
    DecoratorPanel decPanel = new DecoratorPanel();
    decPanel.setWidget(dock);
    
    // Add constructed view
    add(decPanel);
  }
  
  /**
   * Header creation
   * @param title
   * @return
   */
  protected Widget buildHeader() {
    Label uiTitle = new Label();
    uiTitle.setText(getWizardTitle());
    uiTitle.setStylePrimaryName("blockHeader");
    return uiTitle;
  }

  /**
   * Description block creation
   * @param description
   * @return
   */
  protected Widget buildDescription() {
    HTML desc = new HTML(getWizardDescription());
    desc.setHeight("30px");
    desc.setStylePrimaryName("blockDescription");
    return desc;
  }

  /**
   * Skeleton of toolbar creation
   * @return
   */
  protected Widget buildToolbar() {
    HorizontalPanel uiToolbar = new HorizontalPanel();
    uiToolbar.setStylePrimaryName("blockAction");
    uiToolbar.setWidth("100%");
    uiToolbar.setHorizontalAlignment(ALIGN_RIGHT);
    uiToolbar.setVerticalAlignment(ALIGN_MIDDLE);
    uiToolbar.setSpacing(10);
    uiToolbar.setHeight("60px");
    uiToolbar.add(buildStepToolbar());
    return uiToolbar;
  }

  /**
   * Skeleton of toolbar creation
   * @return
   */
  protected Widget buildStepBar() {
    HorizontalPanel uiToolbar = new HorizontalPanel();
    uiToolbar.setStylePrimaryName("blockSteps");
    uiToolbar.setWidth("100%");
    uiToolbar.setHorizontalAlignment(ALIGN_RIGHT);
    uiToolbar.setVerticalAlignment(ALIGN_MIDDLE);
    
    // Build stepBar
    int nbSteps = controller.getNbViews();

    Grid table = new Grid(1, nbSteps);
    // TODO : Implements progress bar
    table.setCellSpacing(3);
    /*Image currentImg = null;
    for(int i=0; i<nbSteps; i++) {
      if(i == screenType.getScreenNumber()) {
        currentImg = new Image("img/red_round_button.jpg");
      }
      else if(i < screenType.getScreenNumber()) {
        currentImg = new Image("img/green_round_tick.jpg");
      }
      else {
        currentImg = new Image("img/green_round_button.jpg");
      }
      table.setWidget(0, i, currentImg);
    }*/

    // Some debug informations displayed
    String tmp = "";
    if(controller.isQuickModeActivated()) {
      tmp = "Q";
    }
    if(controller.isDebugActivated()) {
      tmp += "D";
    }
    if(tmp.length() > 0) {
      tmp += " - " + controller.getLastBuildDate();
      tmp += "<br />" + this.getClass().toString();
    }
    HTML debugHtml = new HTML(tmp);
    debugHtml.setStylePrimaryName("debugInfos");
    uiToolbar.add(debugHtml);
    
    uiToolbar.add(table);
    return uiToolbar;
  }
  
  /**
   * Skeleton of content creation
   * @return
   */
  protected Widget buildContent() {
    uiContent = new ScrollPanel();
    uiContent.setWidget(buildStepContent());
    uiContent.setStylePrimaryName("blockContent");
    uiContent.setSize("600px", "300px");
    if(controller.isDebugActivated()) {
      preFillFields();
    }
    return uiContent;
  }
  
  /**
   * This method display a screen after his initialization
   */
  public void display() {
    this.setVisible(true);
  }
  
  public void hide() {
    this.setVisible(false);
  }
  
  public void scrollContentToBottom() {
    uiContent.scrollToBottom();
  }
  

  /*=======================================================================
   * GUI abstract methods (need to be redefined)
   *======================================================================*/

  /**
   * Redefine Wizard title
   * @return
   */
  protected abstract String getWizardTitle();

  /**
   * Redefine Wizard description
   * @return
   */
  protected abstract String getWizardDescription();
  
  /**
   * Toolbar creation is to redefine
   * @return
   */
  protected abstract Widget buildStepToolbar();
  
  /**
   * Step content creation is to redefine
   * @return
   */
  protected abstract Widget buildStepContent();

  
  /*=======================================================================
   * GUI methods (can be redefined)
   *======================================================================*/
  
  /**
   * Store data after screen validation
   * @return
   */
  public Map<SetupWizardData, String> verifyDatas() throws WizardInvalidFieldException {
    return null;
  }
  
  /**
   * When screen is displayed, this method is executed
   */
  public void executeOnDisplay() {}
  
  /**
   * If debug is on, screen can redefine this method to preFill some fields
   */
  public void preFillFields() {}
  
  
  /*=======================================================================
   * Button factory
   *======================================================================*/

  /**
   * Constructs a standard previous button
   * <p>
   * If current step is the first step, so we return null
   * @return
   */
  protected Button preparePreviousButton() {
    return preparePreviousButton(constants.previous());
  }
  
  /**
   * Constructs a standard previous button with text string
   * <p>
   * If current step is the first step, so return null
   * @param text
   * @return build button
   */
  protected Button preparePreviousButton(String text) {
    return prepareButton(text, currentScreen.getPreviousScreen());
  }

  /**
   * Constructs a standard next button
   * @return build button
   */
  protected Button prepareNextButton() {
    return prepareNextButton(constants.next());
  }
  
  /**
   * Constructs a next button with text string
   * <p>
   * If debug is activated, this button is focused
   * @param text
   * @return build button
   */
  protected Button prepareNextButton(String text) {
    Button nextButton = prepareButton(text, currentScreen.getNextScreen());
    if(controller.isDebugActivated()) {
      nextButton.setFocus(true);
    }
    return nextButton;
  }
  
  /**
   * Constructs a button with text string and with step target
   * <p>
   * Verify datas only on click "Next" button
   * <p>
   * store datas if
   * @param text
   * @param toStep
   * @return build button
   */
  protected Button prepareButton(String text, final SetupScreen screen) {
    Button button = new Button();
    button.setText(text);
    button.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        if(currentScreen.isBefore(screen)) {
          try {
            if(! controller.isQuickModeActivated()) {
              // Ask to verify datas
              Map<SetupWizardData, String> datas = verifyDatas();
              // Ask to controller to store these datas
              controller.storeDatas(datas);
            }
            controller.displayScreen(screen);
          }
          catch (WizardInvalidFieldException e) {
            controller.displayError(e.getMessage());
          }
        }
        else {
          controller.displayScreen(screen);
        }
      }
    });
    return button;
  }

  
  /*=======================================================================
   * API methods
   *======================================================================*/
  
  /**
   * Get the current model loaded
   * @return
   */
  protected WizardModel getModel() {
    return controller.getModel(currentScreen);
  }

  public SetupScreen getScreen() {
    return currentScreen;
  }
}
