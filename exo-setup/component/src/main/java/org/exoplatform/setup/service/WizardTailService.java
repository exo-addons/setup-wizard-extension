package org.exoplatform.setup.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.exoplatform.setup.util.WizardUtility;

/**
 * Utility class to reproduce a tail with server logs.
 * Each tail fills a list with lines of log files. This list can be accessed by method getElements
 * <p>
 * is singleton
 * 
 * @author Clement
 *
 */
public class WizardTailService {

  private static Logger logger = Logger.getLogger(WizardTailService.class);
  
  private static final int MS_TAIL_SLEEP = 200;
  private static final int MS_MAX_TAIL_SLEEP = 40000;

  private LinkedList<String> currentElementsTailed;
  
  private boolean TAIL_FINISHED;
  
  /**
   * Singleton mode
   */
  private static WizardTailService wizardTail = null;
  public static WizardTailService getInstance() {
    if(wizardTail == null) {
      wizardTail = new WizardTailService();
    }
    return wizardTail;
  }
  
  /**
   * Retrieve all elements picked up into logs and clear it.
   * @return
   */
  public List<String> getElements() {
    List<String> list = null;
    if(currentElementsTailed != null) {
      list = new LinkedList<String>(currentElementsTailed);
      currentElementsTailed.clear();
    }
    return list;
  }

  /**
   * Launches many tails
   * @param list
   */
  public void launchesTails(List<String> list) {
    TAIL_FINISHED = false;
    if(list != null) {
      for(final String path : list) {
        Thread t = new Thread() {
          public void run() {
            tail(path);
          }
        };
        t.start();
      }
    }
  }
  
  /**
   * Launch a tail with server logs. When an important log is found, add it to the list
   * <p>
   * Important log is defined by keywords configured into properties file
   * @param in
   * @throws IOException
   */
  private void tail(String pathToFile) {
    
    int totalTailSleep = 0;
    
    try {
      FileInputStream in = new FileInputStream(pathToFile);
      
      if(currentElementsTailed == null) {
        currentElementsTailed = new LinkedList<String>();
      }
      
      if(in != null) {
        try {
          int maxBufSize = 8192;
          byte buf[] = new byte[maxBufSize];
          int len;
    
          
          String lastLine = "";
          String lineSeparator = System.getProperty("line.separator");
          // Infinite loop interrupted only by call Thread method: interrupt()
          while (!Thread.interrupted()) {
            // If there is data, we read it
            
            String firstLine = "";
            while (in.available() > 0) {
              
              // There is data, we reset tailSleep count
              totalTailSleep = 0;
              
              // Data reading from stream
              len = in.read(buf);
              if (len > 0) {
                
                String str = new String(buf, 0, len);
                
                // Separate by line
                String lines[] = str.split(lineSeparator, -1);
    
                if(lines != null && lines.length > 0) {
                  // If there is lastLine stored, we get it and add it to the current list
                  firstLine = lastLine + lines[0];
                  lines[0] = firstLine;
                  
                  // Case of last line is not plain
                  if(len >= maxBufSize && ! str.endsWith(lineSeparator)) {
                    // Stock last line
                    lastLine = lines[lines.length-1];
                    
                    // delete last element
                    lines = (String[])ArrayUtils.removeElement(lines, lastLine);
                  }
                  else {
                    lastLine = "";
                  }
                }
                
                // Try to match important log
                for(String line : lines) {
                  if(line.matches(WizardUtility.getTailMatchWordsRegexp(WizardUtility.getCurrentServerName()))) {
                    //System.out.println(line);
                    currentElementsTailed.add(line);
                  }
                  
                  // If we found the end of logs, we interrupt thread
                  if(line.matches(WizardUtility.getTailEndWordsRegexp(WizardUtility.getCurrentServerName()))) {
                    logger.debug("Thread interruption because end logs found");
                    Thread.currentThread().interrupt();
                    break;
                  }
                }
              }
            }
    
            // there is no data to read, we sleep to avoid actives sleeps
            try {
              lastLine = "";
              Thread.sleep(MS_TAIL_SLEEP);
              
              // Tail is automatically interrupted if sleep again after MS_MAX_TAIL_SLEEP ms
              totalTailSleep += MS_TAIL_SLEEP;
              if(totalTailSleep > MS_MAX_TAIL_SLEEP) {
                logger.debug("Thread interruption because time execution was exceeded");
                Thread.currentThread().interrupt();
              }
            } catch (InterruptedException ignored) {
              break;
            }
          }
        }
        finally {
          in.close();
        }
      }
    }
    catch(IOException ex) {
      logger.error("Problem with wizard tail stream reading", ex);
    }
  }
}
