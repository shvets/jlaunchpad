package org.sf.jlaunchpad;

import org.apache.maven.bootstrap.model.Dependency;
import org.apache.maven.bootstrap.model.Model;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.classworlds.ClassWorld;
import org.sf.jlaunchpad.core.LauncherCommandLineParser;
import org.sf.jlaunchpad.core.LauncherException;
import org.sf.jlaunchpad.util.CommonUtil;
import org.sf.jlaunchpad.util.FileUtil;

import java.io.File;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * This is the main launcher class. It should be able to launch any Java program.
 *
 * @author Alexander Shvets
 * @version 2.0 02/19/2006
 */
public class JLaunchPadLauncher extends DepsLauncher {
  protected final static String IGNORE_EXTENSION = "ignore-extension";

  /**
   * The singleton object.
   */
  protected static Map<String, JLaunchPadLauncher> instances = new HashMap<String, JLaunchPadLauncher>();

  /**
   * Creates new launcher.
   *
   * @param parser     the parser
   * @param args       command line arguments
   * @param classWorld class world
   * @throws LauncherException the launcher exception
   */
  public JLaunchPadLauncher(LauncherCommandLineParser parser, String[] args, ClassWorld classWorld)
      throws LauncherException {
    super(parser, args, classWorld);
  }

  /**
   * Gets the singleton instance.
   *
   * @return the singleton instance
   */
  public static JLaunchPadLauncher getInstance() {
    return instances.get(IGNORE_EXTENSION);
  }

  /**
   * Configures the launcher.
   *
   * @param parentClassLoader parent class loader
   * @throws LauncherException the exception
   */
  public void configure(ClassLoader parentClassLoader) throws LauncherException {
    Map<String, Object> commandLine = parser.getCommandLine();

    List depsFileNames = parser.getStarterDepsFileNames();

    if (depsFileNames != null) {
      for (int i = 0; i < depsFileNames.size(); i++) {
        String depsFileName = (String) depsFileNames.get(i);
        if (new File(depsFileName).exists()) {
          addDepsFileName(depsFileNames);
        } else {
          System.out.println("File " + depsFileName + " does not exist.");
        }
      }
    }

    String classpathFileName = (String) commandLine.get("classpath.file.name");

    if (classpathFileName != null) {
      if (new File(classpathFileName).exists()) {
        setClasspathFileName(classpathFileName);
      } else {
        System.out.println("File " + classpathFileName + " does not exist.");
      }
    }

    String mainClassName = parser.getStarterClassName();

    if (mainClassName == null) {
      if (parser.isPomstarterMode()) {
        if (depsFileNames == null) {
          throw new LauncherException("deps.file.name property should be specified.");
        }
      } else {
        throw new LauncherException("main.class.name property should be specified.");
      }
    }

    setMainClassName(mainClassName);

    super.configure(parentClassLoader);

    File compilerJar = CommonUtil.getCompilerJar();

    if (compilerJar != null) {
      try {
        ClassRealm mainRealm = getMainRealm();
        mainRealm.addConstituent(compilerJar.toURI().toURL());
        //System.out.println("Using Java compiler: " + compilerJar);
      }
      catch (Exception e) {
        throw new LauncherException(e);
      }
    } else {
      System.out.println("Compiler jar file could not be found: " + compilerJar);
    }
  }

  /**
   * Inits the launch.
   *
   * @throws LauncherException the exception
   */
  public void initLaunch() throws LauncherException {
    super.initLaunch();

    String mainClassName = parser.getStarterClassName();

    if (mainClassName == null) {
      if (parser.isPomstarterMode()) {
        List<String> depsFileNames = parser.getStarterDepsFileNames();

        if (depsFileNames == null) {
          throw new LauncherException("deps.file.name property should be specified.");
        } else {
          try {
            List<String> mainClassNames = findMainClassNames(depsFileNames);

            if (mainClassNames.size() == 0) {
              System.out.println("Cannot find Main Class Name.");
            } else if (mainClassNames.size() == 1) {
              mainClassName = mainClassNames.get(0);
            } else {
              throw new LauncherException("More than one option for Main Class Name: " + mainClassNames + ".");
            }
          }
          catch (Exception e) {
            throw new LauncherException(e);
          }

        }
      } else {
        throw new LauncherException("main.class.name property should be specified.");
      }
    }

    setMainClassName(mainClassName);

    classworldLauncher.setAppMain(mainClassName, getRealmName());
  }

  /**
   * Finds main class name from pom file.
   *
   * @param pomFileName the pom file name
   * @return main class name
   * @throws Exception the exception
   */
  public List<String> findMainClassNames(String pomFileName) throws Exception {
    List<String> closestNames = new ArrayList<String>();

    File pom = new File(pomFileName);

    List<String> names = new ArrayList<String>();

    Model model = pomReader.readModel(pom, false);

    for (Object o : model.getAllDependencies()) {
      Dependency dependency = (Dependency) o;

      File file = pomReader.getArtifactFile(dependency);

      if (FileUtil.getExtension(file).equals("jar")) {
        final Manifest manifest = FileUtil.getManifest(new JarFile(file));
        if (manifest != null) {
          final Attributes mainAttributes = manifest.getMainAttributes();
          final String className = mainAttributes.getValue(Attributes.Name.MAIN_CLASS);

          if (className != null) {
            names.add(className);
          }
        }
      }
    }

    String groupId = model.getGroupId();
    String artifactId = model.getArtifactId();

    for (Iterator<String> j = names.iterator(); j.hasNext();) {
      String className = j.next();

      if (className.startsWith(groupId) || className.startsWith(groupId + "." + artifactId)) {
        j.remove();
        closestNames.add(className);
      }
    }

    closestNames.addAll(names);

    if (closestNames.size() == 0) {
      closestNames.add(groupId + "." + artifactId);
    }


    return closestNames;
  }

  /**
   * Finds main class name from pom file.
   *
   * @param pomFileNames the list of pom file name
   * @return main class name
   * @throws Exception the exception
   */
  public List<String> findMainClassNames(List<? extends String> pomFileNames) throws Exception {
    List<String> closestNames = new ArrayList<String>();

    for (String pfn : pomFileNames) {
      List<? extends String> names = findMainClassNames(pfn);

      closestNames.addAll(names);
    }

    return closestNames;
  }

  /**
   * Launches the launcher from the command line.
   *
   * @param args       The application command-line arguments.
   * @param classWorld class world
   * @throws LauncherException exception
   */
  public static void main(String[] args, ClassWorld classWorld) throws LauncherException {
    LauncherCommandLineParser parser = new LauncherCommandLineParser();

    JLaunchPadLauncher launcher = new JLaunchPadLauncher(parser, args, classWorld);

    try {
      launcher.configure(Thread.currentThread().getContextClassLoader());

      instances.put(IGNORE_EXTENSION, launcher);

      launcher.launch();
    }
    catch (LauncherException e) {
      System.out.println(e.getMessage());
    }
  }

}
