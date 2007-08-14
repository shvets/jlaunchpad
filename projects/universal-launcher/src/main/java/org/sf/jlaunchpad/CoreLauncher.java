package org.sf.jlaunchpad;

import org.sf.jlaunchpad.util.ReflectionUtil;
import org.sf.jlaunchpad.core.LauncherException;
import org.sf.jlaunchpad.core.AbstractLauncher;
import org.sf.jlaunchpad.core.LauncherCommandLineParser;
import org.codehaus.classworlds.ClassWorld;
import org.codehaus.classworlds.NoSuchRealmException;
import org.codehaus.classworlds.ClassRealm;

import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
import java.lang.reflect.Method;

/**
 * The class represents core implementation of the launcher behavior
 *  with the support of classworlds.
 *
 * @author Alexander Shvets
 * @version 1.0 01/14/2007
 */
public class CoreLauncher extends AbstractLauncher {
  /** The main realm name. */
  protected final String MAIN_REALM_NAME = "launcher.core";

  /** The ClassWorld class name. */
  protected final String CLASSWORLD_CLASS_NAME = "org.codehaus.classworlds.ClassWorld";

  /** The classworld.*/
  protected ClassWorld classWorld;

  /**
   * Creates new core launcher.
   *
   * @param parser the parser
   * @param args command line arguments
   * @param classWorld the classworld 
   */
  public CoreLauncher(LauncherCommandLineParser parser, String[] args, ClassWorld classWorld) {
    super(parser, args);

    this.classWorld = classWorld;
  }

  /**
   * Creates new core launcher.
   *
   * @param classWorld the classworld
   * @param args command line arguments
   */
  public CoreLauncher(String[] args, ClassWorld classWorld) {
    super(args);

    this.classWorld = classWorld;
  }

  /**
   * Gets main realm.
   *
   * @return the main realm
   * @throws NoSuchRealmException the exception
   */
  public ClassRealm getMainRealm() throws NoSuchRealmException {
    return classWorld.getRealm(MAIN_REALM_NAME);
  }

  /**
   * Adds classpath entry.
   *
   * @param url the URL
   * @throws LauncherException the launcher exception
   */
  public void addClasspathEntry(URL url) throws LauncherException {
    try {
      getMainRealm().addConstituent(url);
    }
    catch (NoSuchRealmException e) {
      throw new LauncherException(e);
    }
  }

  /**
   * Adds classpath entry.
   *
   * @param fileName the file name
   * @throws LauncherException the launcher exception
   */
  public void addClasspathEntry(String fileName) throws LauncherException {
    File file = new File(fileName);

    if(!file.exists()) {
      System.out.println("File " + file + " does not exist.");
    }

    try {
      addClasspathEntry(file.toURI().toURL());
    }
    catch (MalformedURLException e) {
      throw new LauncherException(e);
    }
  }

  /**
   * Configures the launcher.
   *
   * @param parentClassLoader parent class loader
   * @throws LauncherException the exception
   */
  public void configure(ClassLoader parentClassLoader) throws LauncherException {}

  /**
   * Main launcher method.
   *
   * @throws LauncherException the exception
   */
  public void launch() throws LauncherException {
    Throwable throwable = null;
    try {
      ClassRealm mainRealm = getMainRealm();

      Class clazz = mainRealm.loadClass(mainClassName);

      Class classworldClass = mainRealm.loadClass(CLASSWORLD_CLASS_NAME);

      Method method = ReflectionUtil.getMainMethodWithTwoParameters(clazz, classworldClass);

      try {
        ReflectionUtil.launchClass(method, new Object[] { args, classWorld },
          "public static void main(String[] argv, ClassWorld classWorld) method.");
      }
      catch(Throwable t) {
        throwable = t;
        ReflectionUtil.launchClass(clazz, args,
          "public static void main(String[] argv) method.");
      }
    }
    catch(Exception e) {
      if(throwable != null) {
        throw new LauncherException(throwable);
      }

      throw new LauncherException(e);
    }
  }
   
}
