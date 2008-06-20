package org.sf.jlaunchpad.install;

import org.sf.jlaunchpad.LauncherException;
import org.sf.jlaunchpad.util.FileUtil;
import org.sf.jlaunchpad.util.StringUtil;
import org.sf.jlaunchpad.xml.ProxiesXmlHelper;
import org.sf.pomreader.ProjectInstaller;
import org.jdom.JDOMException;
import org.jdom.Element;

import java.io.*;

/**
 * The class perform initial (command line) installation of scriprlandia.
 *
 * @author Alexander Shvets
 * @version 1.0 07/14/2007
 */
public class CoreInstaller {
  public final static String LAUNCHER_PROPERTIES =
      System.getProperty("user.home") + File.separatorChar + ".jlaunchpad";

  protected LauncherProperties launcherProps = new LauncherProperties(LAUNCHER_PROPERTIES);

  private String installRoot;

  private ProjectInstaller installer;

  protected void load() throws IOException {
    launcherProps.load();
  }

  protected void save() throws IOException {
    launcherProps.save();
  }

  /**
   * Performs installation of initial components/projects.
   *
   * @param args the command line arguments
   * @throws LauncherException the exception
   */
  public void install(String[] args) throws LauncherException {
    System.out.println("Installing JLaunchPad...");

    installRoot = System.getProperty("jlaunchpad.install.root");

    if(installRoot == null) {
      installRoot = ".";
    }

    try {
//      load();
      //save();

      installer = new ProjectInstaller();

      install("bootstrap-mini");
      install("jdom");
      install("jlaunchpad-common");
      install("classworlds");
      install("pom-reader");
      install("jlaunchpad-launcher");

      String proxyHost = (String)launcherProps.get("proxyHost");

      if(proxyHost != null && proxyHost.trim().length() > 0) {
        System.setProperty("jruby.proxy.line", "http://" + launcherProps.get("proxyHost") + ":" + 
                                               launcherProps.get("proxyPort"));
      }

      copyConfigFiles("src/main/config");

      configureProxy();
    }
    catch (Exception e) {
      throw new LauncherException(e);
    }

    System.out.println("JLaunchPad is installed.");
  }

  private void configureProxy() throws JDOMException, IOException {
    String repositoryHome = System.getProperty("repository.home");
    String launcherHome = System.getProperty("jlaunchpad.home");

    File outSettings = new File(launcherHome + File.separatorChar + "settings.xml");

    ProxiesXmlHelper xmlHelper = new ProxiesXmlHelper();

    if(!outSettings.exists()) {
      File inSettings =  new File(installRoot,"src/main/config/settings.xml");

      xmlHelper.read(inSettings);
    }
    else {
      xmlHelper.read(outSettings);
    }

    Element localRepository = xmlHelper.getLocalRepositoryElement();

    // set up local repository value
    localRepository.setText(repositoryHome.replace(File.separatorChar, '/'));

    xmlHelper.process(launcherProps);

    xmlHelper.save(outSettings);
  }

  private void install(String name) throws Exception {
    System.out.println("Installing \"" + name + "\" project...");

    installer.install(installRoot + "/projects/" + name, false);
  }

  private void copyConfigFiles(String dir) throws IOException {
    File[] files = new File(installRoot, dir).listFiles();

    String launcherHome = System.getProperty("jlaunchpad.home");
    File launcherHomeFile = new File(launcherHome);

    if (!launcherHomeFile.exists()) {
      launcherHomeFile.mkdirs();
    }

    for (File fromFile : files) {
      if (fromFile.exists() && !fromFile.isHidden() && !fromFile.isDirectory()) {
        File toFile = new File(launcherHomeFile.toString(), fromFile.getName());

        if(!fromFile.getName().equals("proxies-segment.xml")) {
          System.out.println("Copying \"" + fromFile.getName() + "\" file to \"" + toFile.getParent() + "\" directory...");

          copy(fromFile, toFile);
        }
      }
    }
  }

  private void copy(File fromFile, File toFile) throws IOException {
    String extension = FileUtil.getExtension(fromFile);

    boolean isUnixFile = (extension != null && extension.equals("sh"));

    BufferedReader reader = null;
    BufferedWriter writer = null;

    try {
      reader = new BufferedReader(new FileReader(fromFile));
      writer = new BufferedWriter(new FileWriter(toFile));

      boolean done = false;

      while (!done) {
        String line = reader.readLine();

        if (line == null) {
          done = true;
        } else {
          writer.write(StringUtil.substituteProperties(line, "@", "@", System.getProperties()));

          if (isUnixFile) {
            writer.write("\n");
          } else {
            writer.newLine();
          }
        }
      }
    }
    finally {
      if (reader != null) {
        reader.close();
      }

      if (writer != null) {
        writer.close();
      }
    }

    if(!System.getProperty("os.name").toLowerCase().startsWith("windows") && isUnixFile) {
      chmod(toFile);
    }
  }

  private void chmod(File file) {
    Runtime runtime = Runtime.getRuntime();

    try {
      Process process = runtime.exec(new String[] { "chmod", "+x", file.getCanonicalPath() });

      int code = process.waitFor();

      if(code > 0) {
        System.out.println("Error code: " + code + ". \"chmod\" - operation is not permitted. Use 'su' rights.");
      }
    } catch (Exception e) {
       e.printStackTrace();
    }
  }

  /**
   * Launches the installer from the command line.
   *
   * @param args The application command-line arguments.
   * @throws LauncherException exception
   */
  public static void main(String[] args) throws Exception {
    CoreInstaller installer = new CoreInstaller();
    installer.load();

    installer.install(args);
  }

}
