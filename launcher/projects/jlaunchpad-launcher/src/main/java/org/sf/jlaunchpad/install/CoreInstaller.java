package org.sf.jlaunchpad.install;


import org.sf.jlaunchpad.core.LauncherException;
import org.sf.jlaunchpad.util.FileUtil;
import org.sf.jlaunchpad.util.StringUtil;
import org.sf.pomreader.ProjectInstaller;

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

    try {
      load();

      installer = new ProjectInstaller();

      install("bootstrap-mini");
      install("jlaunchpad-common");
      install("classworlds");
      install("pom-reader");
      install("jlaunchpad-launcher");

      copyConfigFiles("src/main/config");
    }
    catch (Exception e) {
      throw new LauncherException(e);
    }

    System.out.println("JLaunchPad is installed.");
  }

  private void install(String name) throws Exception {
    System.out.println("Installing \"" + name + "\" project...");

    installer.install("projects/" + name, false);
  }

  private void copyConfigFiles(String dir) throws IOException {
    File[] files = new File(dir).listFiles();

    String launcherHome = System.getProperty("launcher.home");
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
          writer.write(StringUtil.substituteProperties(line, "@", "@"));

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
  }

  /**
   * Launches the installer from the command line.
   *
   * @param args The application command-line arguments.
   * @throws LauncherException exception
   */
  public static void main(String[] args) throws LauncherException {
    CoreInstaller installer = new CoreInstaller();

    installer.install(args);
  }

}
