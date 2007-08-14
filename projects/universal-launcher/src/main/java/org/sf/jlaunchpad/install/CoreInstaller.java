package org.sf.jlaunchpad.install;


import org.sf.jlaunchpad.core.LauncherException;
import org.sf.jlaunchpad.util.StringUtil;
import org.sf.jlaunchpad.util.FileUtil;
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

      ProjectInstaller installer = new ProjectInstaller();

      System.out.println("Installing bootstrap-mini project...");
      installer.install("projects/bootstrap-mini", false);

      System.out.println("Installing \"universal-launcher-common\" project...");
      installer.install("projects/universal-launcher-common", false);

      System.out.println("Installing \"classworlds\" project...");
      installer.install("projects/classworlds", false);

      System.out.println("Installing \"pom-reader\" project...");
      installer.install("projects/pom-reader", false);      

      System.out.println("Installing \"universal-launcher\" project...");
      installer.install("projects/universal-launcher", false);

      File[] files = new File("projects/universal-launcher/src/main/config").listFiles();

      String launcherHome = System.getProperty("launcher.home");
      File launcherHomeFile = new File(launcherHome);

      if(!launcherHomeFile.exists()) {
        launcherHomeFile.mkdirs();
      }

      for(File fromFile : files) {
        String extension = FileUtil.getExtension(fromFile);

        boolean isUnixFile = (extension != null && extension.equals("sh"));

        if(fromFile.exists() && !fromFile.isHidden() && !fromFile.isDirectory()) {
          File toFile = new File(launcherHomeFile.toString(), fromFile.getName());

          System.out.println("Copying \"" + fromFile.getName() + "\" file to \"" + toFile.getParent() +  "\" directory...");

          BufferedReader reader = null;
          BufferedWriter writer = null;

          try {
            reader = new BufferedReader(new FileReader(fromFile));
            writer = new BufferedWriter (new FileWriter(toFile));

            boolean done = false;

            while(!done) {
              String line = reader.readLine();

              if(line == null) {
                done = true;
              }
              else {
                writer.write(StringUtil.substituteProperties(line, "@", "@"));

                if(isUnixFile) {
                  writer.write("\n");
                }
                else {
                  writer.newLine();
                }
              }
            }
          }
          finally {
            if(reader != null) {
              reader.close();
            }

            if(writer != null) {
              writer.close();
            }
          }
        }
      }
    }
    catch (Exception e) {
      throw new LauncherException(e);
    }

    System.out.println("JLaunchPad is installed.");
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
