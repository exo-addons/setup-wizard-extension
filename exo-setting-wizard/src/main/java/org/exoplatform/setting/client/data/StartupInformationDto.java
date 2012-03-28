package org.exoplatform.setting.client.data;

import java.io.Serializable;
import java.util.Map;

import org.exoplatform.setting.shared.data.SetupWizardData;

public class StartupInformationDto implements Serializable {

  private static final long serialVersionUID = 1L;
  
  Integer firstScreenNumber;
  Boolean isDebugActivated;
  Boolean isQuickModeActivated;
  String lastBuildDate;
  
  Map<SetupWizardData, String> propertiesValues;
  
  
  public Integer getFirstScreenNumber() {
    return firstScreenNumber;
  }
  public void setFirstScreenNumber(Integer firstScreenNumber) {
    this.firstScreenNumber = firstScreenNumber;
  }
  public Boolean getIsDebugActivated() {
    return isDebugActivated;
  }
  public void setIsDebugActivated(Boolean isDebugActivated) {
    this.isDebugActivated = isDebugActivated;
  }
  public Boolean getIsQuickModeActivated() {
    return isQuickModeActivated;
  }
  public void setIsQuickModeActivated(Boolean isQuickModeActivated) {
    this.isQuickModeActivated = isQuickModeActivated;
  }
  public String getLastBuildDate() {
    return lastBuildDate;
  }
  public void setLastBuildDate(String lastBuildDate) {
    this.lastBuildDate = lastBuildDate;
  }
  public Map<SetupWizardData, String> getPropertiesValues() {
    return propertiesValues;
  }
  public void setPropertiesValues(Map<SetupWizardData, String> test) {
    this.propertiesValues = test;
  }
}
