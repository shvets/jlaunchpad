package org.sf.jlaunchpad.core;

/**
 * The class represents abstract implementation of the launcher behavior.
 *
 * @author Alexander Shvets
 * @version 1.0 01/14/2007
 */
public abstract class AbstractLauncher implements Launcher {

  /** The main class name. */
  protected String mainClassName;

  /** The parser. */
  protected LauncherCommandLineParser parser;

  /** The array of arguments. */
  protected String[] args;

  /** The exit code. */
  protected int exitCode = 0;

  /**
   * Creates new launcher.
   *
   * @param parser the parser
   * @param args command line arguments
   */
  public AbstractLauncher(LauncherCommandLineParser parser, String[] args) {
    this.parser = parser;

    if(parser != null) {
      this.args = parser.parse(args);
    }
    else {
      this.args = args;       
    }
  }

  /**
   * Creates new launcher.
   *
   * @param args command line arguments
   */
  public AbstractLauncher(String[] args) {
    this(null, args);
  }

  /**
   * Gets the main class name.
   *
   * @return the main class name
   */
  public String getMainClassName() {
    return mainClassName;
  }

  /**
   * Sets the main class name.
   *
   * @param mainClassName the main class name
   */
  public void setMainClassName(String mainClassName) {
    this.mainClassName = mainClassName;
  }

  /**
   * Gets the exit code.
   *
   * @return the exit code
   */
  public int getExitCode() {
    return exitCode;
  }

  /**
   * Cets arguments.
   *
   * @return array of arguments
   */
  public String[] getArgs() {
    return args;
  }

  /**
   * Sets arguments.
   *
   * @param args array of arguments
   */
  public void setArgs(String[] args) {
    this.args = args;
  }
  
}
