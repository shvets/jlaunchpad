package org.sf.jlaunchpad.core;

import java.net.URL;

/**
 * The interface represents launher behavior.
 *
 * @author Alexander Shvets
 * @version 1.0 01/14/2007
 */
public interface Launcher {

  /**
   * Gets the main class name.
   *
   * @return the main class name
   */
  String getMainClassName();

  /**
   * Sets the main class name.
   *
   * @param mainClassName the main class name
   */
  void setMainClassName(String mainClassName);

  /**
   * Gets the exit code.
   *
   * @return the exit code
   */
  int getExitCode();

  /**
   * Adds classpath entry.
   *
   * @param url the URL
   * @throws LauncherException the launcher exception
   */
  void addClasspathEntry(URL url) throws LauncherException;

  /**
   * Adds classpath entry.
   *
   * @param fileName the file name
   * @throws LauncherException the launcher exception
   */
  void addClasspathEntry(String fileName) throws LauncherException;

  /**
   * Configures the launcher.
   *
   * @param parentClassLoader parent class loader
   * @throws LauncherException the exception
   */
  void configure(ClassLoader parentClassLoader) throws LauncherException;

  /**
   * Main launcher method.
   *
   * @throws LauncherException the exception
   */
  void launch() throws LauncherException;

  /**
   * Cets arguments.
   *
   * @return array of arguments
   */
  String[] getArgs();

  /**
   * Sets arguments.
   *
   * @param args array of arguments
   */
  void setArgs(String[] args);

}
