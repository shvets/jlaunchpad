package org.sf.jlaunchpad.install;

import org.sf.jlaunchpad.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * The class represent launcher properties located in "user.home".
 *
 * @author Alexander Shvets
 * @version 1.0 01/14/2007
 */
public class LauncherProperties extends Properties {

  /**
   * Maven2 settings.xml file location.
   */
  public static final String SETTINGS_XML =
      System.getProperty("user.home") + File.separatorChar + ".m2" + File.separatorChar + "settings.xml";

  public final static String LAUNCHER_PROPERTIES =
      System.getProperty("user.home") + File.separatorChar + ".jlaunchpad";

  protected String fileName;

  public LauncherProperties() {
    this(LAUNCHER_PROPERTIES);
  }

  public LauncherProperties(String fileName) {
    this.fileName = fileName;
  }

  /**
   * Loads the properties file.
   *
   * @throws IOException I/O exception
   */
  public void load() throws IOException {
    String root = "/";

    if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
      root = System.getProperty("user.dir").substring(0, 1) + ":\\";
    }

    File launcherPropsFile = new File(fileName);

    if (launcherPropsFile.exists()) {
      super.load(new FileInputStream(launcherPropsFile));

      String proxySet = (String) get("proxySet");

      if (proxySet == null || proxySet.equalsIgnoreCase("false")) {
        remove("proxySet");
        remove("proxyHost");
        remove("proxyPort");
        remove("proxyUser");
        remove("proxyAuth");
        remove("proxyPassword");
      }

      String httpProxySet = (String) get("http.proxySet");

      if (httpProxySet == null || httpProxySet.equalsIgnoreCase("false")) {
        remove("http.proxySet");
        remove("http.proxyHost");
        remove("http.proxyPort");
        remove("http.proxyUser");
        remove("http.proxyAuth");
        remove("http.proxyPassword");
      }
    } else {
      //put("java.specification.version.level", "1.5");
      put("java.home.internal", root + "Java" + File.separatorChar + "jdk1.6.0");
    }

    if (get("repository.home") == null) {
      String repositoryHome;

      String settingsXml =
          System.getProperty("user.home") + File.separatorChar + ".m2" + File.separatorChar + "settings.xml";

      File outSettings = new File(settingsXml);

      if (!outSettings.exists()) {
        repositoryHome = root + "maven-repository";
      } else {
        String settings = new String(FileUtil.getStreamAsBytes(new FileInputStream(SETTINGS_XML)));
        int index1 = settings.indexOf("<localRepository>");
        int index2 = settings.indexOf("</localRepository>");

        repositoryHome = settings.substring(index1 + "<localRepository>".length(), index2);
      }

      put("repository.home", repositoryHome);
    }

    if (get("jlaunchpad.home") == null) {
      put("jlaunchpad.home", System.getProperty("user.home") + File.separatorChar + "jlaunchpad");
    }
  }

  /**
   * Saves the properties file.
   *
   * @throws IOException I/O exception
   */
  public void save() throws IOException {
    File launcherPropsFile = new File(fileName);

    store(new FileOutputStream(launcherPropsFile), "Launcher properties");
  }

}
