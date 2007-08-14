package org.sf.jlaunchpad.core;

import org.sf.jlaunchpad.util.ReflectionUtil;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 * The class represents simle implementation of the launcherbehavior.
 *
 * @author Alexander Shvets
 * @version 1.0 01/14/2007
 */
public class SimpleLauncher extends AbstractLauncher {

  /** The class loader. */
  private ClassLoader classLoader;

  /** The main method name. */
  protected String mainMethodName;

  /** The classpath list. */
  protected List<URL> classpath = new ArrayList<URL>();

  /**
   * Creates new launcher.
   *
   * @param args command line arguments
   */
  public SimpleLauncher(String[] args) {
    super(args);
  }
  
  /**
   * Creates new launcher.
   *
   * @param parser the parser
   * @param args command line arguments
   */
  public SimpleLauncher(LauncherCommandLineParser parser, String[] args) {
    super(parser, args);
  }

  /**
   * Gets the main method name.
   *
   * @return the method class name
   */
  public String getMainMethodName() {
    return mainMethodName;
  }

  /**
   * Sets the main method name.
   *
   * @param mainMethodName the method class name
   */
  public void setMainMethodName(String mainMethodName) {
    this.mainMethodName = mainMethodName;
  }

  /**
   * Gets class loader.
   *
   * @return class loader
   */
  public ClassLoader getClassLoader() {
    return classLoader;
  }

  /**
   * Adds classpath entry.
   *
   * @param url the URL
   * @throws LauncherException the launcher exception
   */
  public void addClasspathEntry(URL url) throws LauncherException {
    classpath.add(url);
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
      classpath.add(file.toURI().toURL());
    }
    catch (MalformedURLException e) {
      throw new LauncherException(e);
    }
  }

  /**
   * Configures the launcher.
   *
   * @param parentClassLoader classLoader
   * @throws LauncherException the exception
   */
  public void configure(ClassLoader parentClassLoader) throws LauncherException {
    URL[] urls = new URL[classpath.size()];

    classpath.toArray(urls);

    classLoader = new URLClassLoader(urls, parentClassLoader);
  }

  /**
   * Main launcher method.
   *
   * @throws LauncherException the exception
   */
  public void launch() throws LauncherException {
    try {
      Class clazz = classLoader.loadClass(mainClassName);

      ReflectionUtil.launchClass(clazz, args, "public static void main(String[] argv) method.");
    }
    catch(Exception e) {
      throw new LauncherException(e);
    }
  }

}
