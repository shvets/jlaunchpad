package org.sf.jlaunchpad;

import org.codehaus.classworlds.ClassWorld;
import org.codehaus.classworlds.ClassRealm;
import org.apache.maven.bootstrap.model.Model;
import org.apache.maven.bootstrap.model.Dependency;
import org.sf.jlaunchpad.util.FileUtil;
import org.sf.jlaunchpad.util.CommonUtil;
import org.sf.jlaunchpad.core.LauncherCommandLineParser;
import org.sf.jlaunchpad.core.LauncherException;

import java.util.*;
import java.util.jar.Manifest;
import java.util.jar.JarFile;
import java.util.jar.Attributes;
import java.io.File;

/**
 * This is the main launcher class. It should be able to launch any Java program.
 *
 * @author Alexander Shvets
 * @version 2.0 02/19/2006
 */
public class UniversalLauncher extends DepsLauncher {

  /**
   * Creates new launcher.
   *
   * @param parser the parser
   * @param args command line arguments
   * @param classWorld class world
   * @throws LauncherException the launcher exception
   */
  protected UniversalLauncher(LauncherCommandLineParser parser, String[] args, ClassWorld classWorld)
         throws LauncherException {
    super(parser, args, classWorld);
  }

  /**
   * Configures the launcher.
   *
   * @param parentClassLoader parent class loader
   * @throws LauncherException the exception
   */
  public void configure(ClassLoader parentClassLoader) throws LauncherException {
    Map<String, String> commandLine = parser.getCommandLine();

    String mainClassName = parser.getCommandLine().get("main.class.name");

    if(mainClassName == null) {
      throw new LauncherException("main.class.name property should be specified.");
    }

    setMainClassName(mainClassName);

    String depsFileName = commandLine.get("deps.file.name");

    if(depsFileName != null) {
      if(new File(depsFileName).exists()) {
        setDepsFileName(depsFileName);
      }
      else {
        System.out.println("File " + depsFileName + " does not exist.");
      }
    }

    String classpathFileName = commandLine.get("classpath.file.name");

    if(classpathFileName != null) {
      if(new File(classpathFileName).exists()) {
        setClasspathFileName(classpathFileName);
      }
      else {
        System.out.println("File " + classpathFileName + " does not exist.");
      }
    }

    /*String launcherPropertiesFileName = commandLine.get("launcher.properties");

    if(launcherPropertiesFileName == null) {
      launcherPropertiesFileName = System.getProperty("user.home") + "/.jlaunchpad";
    }

    if(new File(launcherPropertiesFileName).exists()) {
      try {
        LauncherProperties launcherProperties = new LauncherProperties(launcherPropertiesFileName);
      }
      catch (IOException e) {
        throw new LauncherException(e);
      }
    }
    else {
      System.out.println("File " + launcherPropertiesFileName + " does not exist.");
    }
      */

    super.configure(parentClassLoader);

    File compilerJar = CommonUtil.getCompilerJar();

    if(compilerJar != null) {
      try {
        ClassRealm mainRealm = getMainRealm();
        mainRealm.addConstituent(compilerJar.toURI().toURL());
        //System.out.println("Using Java compiler: " + compilerJar);
      }
      catch (Exception e) {
        throw new LauncherException(e);
      }
    }
    else {
      System.out.println("Compiler jar file could not be found: " + compilerJar);
    }
  }

  /**
   * Finds main class name from pom file.
   *
   * @param pomFileName the pom file name
   * @return main class name
   * @throws Exception the exception
   */
  public List<String> findMainClassNames(String pomFileName) throws Exception {
    List<String> names = new ArrayList<String>();

    File pom = new File(pomFileName);

    Model model = pomReader.readModel(pom, false);

    for (Object o : model.getAllDependencies()) {
      Dependency dependency = (Dependency) o;

      File file = pomReader.getArtifactFile(dependency);

      if (!FileUtil.getExtension(file).equals("pom")) {
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

    List<String> closestNames = new ArrayList<String>();

    for (Iterator<String> i = names.iterator(); i.hasNext();) {
      String className = i.next();

      if(className.startsWith(groupId) || className.startsWith(groupId + "." + artifactId)) {
        i.remove();
        closestNames.add(className);
      }
    }

    closestNames.addAll(names);

    if(closestNames.size() == 0) {
      closestNames.add(groupId + "." + artifactId);
    }

    return closestNames;
  }

  /**
   * Launches the launcher from the command line.
   *
   * @param args The application command-line arguments.
   * @param classWorld class world
   * @throws LauncherException exception
   */
  public static void main(String[] args, ClassWorld classWorld) throws LauncherException {
    LauncherCommandLineParser parser = new LauncherCommandLineParser();

    UniversalLauncher launcher = new UniversalLauncher(parser, args, classWorld);

    try {
      launcher.configure(Thread.currentThread().getContextClassLoader());

      launcher.launch();
    }
    catch(LauncherException e) {
      System.out.println(e.getMessage());
    }
  }

}
