package org.sf.jlaunchpad;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This class reads and populate all required system properties form
 * propreties file to system properties.
 *
 * @author Alexander Shvets
 * @version 1.0 07/21/2007
 */
public class LauncherProperties extends Properties {

  /**
   * Creates new launcher helper.
   * @param fileName file name
   * @throws IOException I/O Exception 
   */
  public LauncherProperties(String fileName) throws IOException {
    File propertiesFile = new File(fileName);

    if(propertiesFile.exists()) {
      load(new FileInputStream(propertiesFile));
    }

    setupProperties();
  }

  /**
   * Sets up system properties, required for running launcher.
   */
  protected void setupProperties() {
    for (Object o : keySet()) {
      String key = (String) o;

      if (!key.equals("java.home")) {
        System.setProperty(key, (String) get(key));
      }
    }
  }

}
