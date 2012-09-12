package org.exoplatform.setup.data;

/**
 * For each property configured by user, there is an enum here.
 * 
 * @author Clement
 *
 */
public enum SetupWizardData {
  
  // Super User
  SU_USERNAME ("su_username", "exo.super.user"),
  SU_PASSWORD ("su_password", "exo.super.user.password"),
  SU_EMAIL    ("su_email", "exo.super.user.email"),
  
  JCR_DATA_SOURCE   ("jcr_data_source", "gatein.jcr.datasource.name"),
  STORE_FILES_IN_DB ("store_files_in_db", "gatein.jcr.store.files.db"),
  
  IDM_DATA_SOURCE ("idm_data_source", "gatein.idm.datasource.name"),

  LDAP_SERVER_TYPE  ("ldap_server_type", "ldap.server.type"),
  LDAP_PROVIDER_URL ("ldap_provider_url", "ldap.provider.url"),
  LDAP_BASE_DN      ("ldap_base_dn", "ldap.base.dn"),
  LDAP_ROOT_DN      ("ldap_root_dn", "ldap.root.dn"),
  LDAP_PASSWORD     ("ldap_password", "ldap.password"),
  
  FS_LOGS        ("fs_logs", "filesystem.logs"),
  FS_INDEX       ("fs_index", "gatein.jcr.index.data.dir"),
  FS_DATA_VALUES ("fs_data_values", "gatein.jcr.data.dir"),
  
  SMTP_HOST               ("smtp_host", "gatein.email.smtp.host"),
  SMTP_PORT               ("smtp_port", "gatein.email.smtp.port"),
  SMTP_SECURED_CONNECTION ("smtp_secured_connection", "gatein.email.smtp.auth"),
  SMTP_USERNAME           ("smtp_username", "gatein.email.smtp.username"),
  SMTP_PASSWORD           ("smtp_password", "gatein.email.smtp.password"),
  SMTP_EMAIL              ("smtp_email", "gatein.email.smtp.from"),
  
  WS_BLANK   ("ws_blank", "exo.website.blank"),
  WS_SAMPLES ("ws_samples", "exo.website.samples");

  private String propertyIndex;
  private String propertyName;
  
  private SetupWizardData(String propertyIndex, String propertyName) {
    this.propertyName = propertyName;
    this.propertyIndex = propertyIndex;
  }
  
  public String getPropertyIndex() {
    return this.propertyIndex;
  }
  
  public String getPropertyName() {
    return this.propertyName;
  }
  
  /**
   * This method returns the name corresponding to the index of data
   * @param index
   * @return property name
   */
  public static String getPropertyName(String index) {
    String name = "";
    if(index != null && index.length() > 0) {
      for(SetupWizardData data : SetupWizardData.values()) {
        if(index.equals(data.getPropertyIndex())) {
          name = data.getPropertyName();
          break;
        }
      }
    }
    return name;
  }
}