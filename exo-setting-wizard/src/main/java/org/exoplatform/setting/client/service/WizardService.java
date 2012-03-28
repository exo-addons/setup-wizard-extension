package org.exoplatform.setting.client.service;

import java.util.List;
import java.util.Map;

import org.exoplatform.setting.client.data.StartupInformationDto;
import org.exoplatform.setting.client.data.WizardPlatformException;
import org.exoplatform.setting.client.data.WizardPropertiesException;
import org.exoplatform.setting.client.data.WizardTailException;
import org.exoplatform.setting.shared.data.SetupWizardData;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("wizardsrv")
public interface WizardService extends RemoteService {
  
  Map<String, String> getSystemProperties();
  List<String> getDatasources();
  String writeProperties(Map<SetupWizardData, String> datas) throws WizardPropertiesException;
  String startPlatform() throws WizardTailException, WizardPlatformException;
  StartupInformationDto getStartupInformation() throws WizardPropertiesException;
  List<String> getImportantServerLogs();
}
