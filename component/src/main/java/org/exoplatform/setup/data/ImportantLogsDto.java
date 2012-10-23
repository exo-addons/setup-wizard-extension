package org.exoplatform.setup.data;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Clement
 *
 */
@XmlRootElement(name = "ImportantLogsDto")
public class ImportantLogsDto implements Serializable {
  private static final long serialVersionUID = 1L;
  
  String logPath;
  List<String> logs;
  
  @XmlElement
  public String getLogPath() {
    return logPath;
  }
  public void setLogPath(String logPath) {
    this.logPath = logPath;
  }
  
  @XmlElement
  public List<String> getLogs() {
    return logs;
  }
  public void setLogs(List<String> logs) {
    this.logs = logs;
  }
}
