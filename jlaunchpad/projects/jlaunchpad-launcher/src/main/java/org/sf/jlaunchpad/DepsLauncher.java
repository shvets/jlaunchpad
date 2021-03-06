package org.sf.jlaunchpad;

import org.codehaus.classworlds.ClassRealm;
import org.codehaus.classworlds.ClassWorld;
import org.sf.pomreader.PomReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.List;

/**
 * This is the dependencies launcherclass. It extends classworlds' classloader functionality
 * with the dependencies resolution, specified in pom.xml instead of classworlds'
 * configuration file.
 *
 * @author Alexander Shvets
 * @version 2.0 02/19/2006
 */
public class DepsLauncher extends ClassworldLauncher {
  /**
   * Pom file name.
   */
  private List depsFileNames;

  /**
   * Classpath file name.
   */
  private String classpathFileName;

  /**
   * The Pom reader.
   */
  protected PomReader pomReader = new PomReader();

  /**
   * Creates new dependencies launcher.
   *
   * @param parser     the parser
   * @param args       command line arguments
   * @param classWorld the classworld
   * @throws LauncherException the exception
   */
  public DepsLauncher(LauncherCommandLineParser parser, String[] args, ClassWorld classWorld)
      throws LauncherException {
    super(parser, args, classWorld);

    try {
      pomReader.init();
    }
    catch (Exception e) {
      throw new LauncherException(e);
    }
  }

  /**
   * Gets the realm name.
   *
   * @return the realm name
   */
  protected String getRealmName() {
    return "pom-launcher";
  }

  /**
   * Sets the deps file name.
   *
   * @param depsFileNames the list of pom file names
   */
  public void addDepsFileName(List depsFileNames) {
    this.depsFileNames = depsFileNames;
  }

  /**
   * Sets the classpath file name.
   *
   * @param classpathFileName the classpath file name
   */
  public void setClasspathFileName(String classpathFileName) {
    this.classpathFileName = classpathFileName;
  }

  /**
   * Resolves dependencies for specified pom maven2 dependencies file.
   *
   * @param classpathFile the classpath file name
   * @throws Exception the exception
   */
  public void loadDependencies(File classpathFile) throws Exception {
    ClassRealm classRealm = getMainRealm();

    BufferedReader reader = new BufferedReader(new FileReader(classpathFile));

    boolean done1 = false;

    while (!done1) {
      String line = reader.readLine();

      if (line == null) {
        done1 = true;
      } else {
        boolean done2 = false;

        while (!done2) {
          int index1 = line.indexOf("${");

          if (index1 == -1) {
            done2 = true;
          } else {
            int index2 = line.substring(index1 + 1).indexOf("}");

            if (index2 == -1) {
              done2 = true;
            } else {
              String propertyName = line.substring(index1 + 2, index2 + 1);
              String property = System.getProperty(propertyName);

              if (property == null) {
                line = line.substring(0, index1) + "?" + propertyName + "?" + line.substring(index2 + 2);

                System.out.println("This property is not specified: " + propertyName + ".");
              } else {
                line = line.substring(0, index1) + property + line.substring(index2 + 2);
              }
            }
          }
        }

        classRealm.addConstituent(new File(line).toURI().toURL());
      }
    }
  }

  /**
   * Resolves dependencies for specified pom maven2 dependencies file.
   *
   * @param groupId    group ID
   * @param artifactId artifact ID
   * @param version    version
   * @throws Exception the exception
   */
  public void resolveDependencies(String groupId, String artifactId, String version)
      throws Exception {
    resolveDependencies(groupId, artifactId, version, null);
  }

  /**
   * Resolves dependencies for specified pom maven2 dependencies file.
   *
   * @param groupId    group ID
   * @param artifactId artifact ID
   * @param version    version
   * @param classifier the classifier
   * @throws Exception the exception
   */
  public void resolveDependencies(String groupId, String artifactId, String version, String classifier)
      throws Exception {
    ClassRealm classRealm = getMainRealm();

    List<URL> deps = pomReader.calculateDependencies(groupId, artifactId, version, classifier);

    for (URL dep : deps) {
      classRealm.addConstituent(dep);
    }
  }

  /**
   * Resolves dependencies for specified pom maven2 dependencies file.
   *
   * @param depsFileName the pom file ma,e
   * @throws Exception the exception
   */
  public void resolveDependencies(String depsFileName) throws Exception {
    resolveDependencies(depsFileName, false);
  }
  /**
   * Resolves dependencies for specified pom maven2 dependencies file.
   *
   * @param depsFileName the pom file ma,e
   * @param ignore do not download pom file
   * @throws Exception the exception
   */
  public void resolveDependencies(String depsFileName, boolean ignore) throws Exception {
    File depsFile = new File(depsFileName);

    if (!depsFile.exists()) {
      System.out.println("File " + depsFile + " does not exist.");
    }
    else {
      ClassRealm classRealm = getMainRealm();

      List<URL> deps = pomReader.calculateDependencies(depsFile, ignore);

      for (URL dep : deps) {
        classRealm.addConstituent(dep);
      }
    }
  }

  /**
   * Inits the launch.
   *
   * @throws LauncherException the exception
   */
  public void initLaunch() throws LauncherException {
    try {
      if (depsFileNames != null) {
        for (Object depsFileName : depsFileNames) {
          File depsFile = new File((String) depsFileName);

          if (depsFile.exists()) {
            resolveDependencies((String) depsFileName);
          }
        }
      }

      if (classpathFileName != null) {
        File classpathFile = new File(classpathFileName);

        if (classpathFile.exists()) {
          loadDependencies(classpathFile);
        }
      }
    }
    catch (Exception e) {
      throw new LauncherException(e);
    }
  }

}
