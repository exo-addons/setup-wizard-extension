package org.exoplatform.setting.client.data;

public class WizardInvalidFieldException extends WizardException {
  private static final long serialVersionUID = 1L;
  
  public WizardInvalidFieldException() {
    super();
  }
  
  public WizardInvalidFieldException(String message) {
    super(message);
  }
}
