package org.exoplatform.setup.data;

import java.io.Serializable;
import java.util.Map;

public class StartupInformationDto implements Serializable {

  private static final long serialVersionUID = 1L;
  
  Integer firstScreenNumber;
  Boolean isDebugActivated;
  Boolean isQuickModeActivated;

  Map<String, String> propertiesValues;
  
  
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
  public Map<String, String> getPropertiesValues() {
    return propertiesValues;
  }
  public void setPropertiesValues(Map<String, String> map) {
    this.propertiesValues = map;
  }
}
