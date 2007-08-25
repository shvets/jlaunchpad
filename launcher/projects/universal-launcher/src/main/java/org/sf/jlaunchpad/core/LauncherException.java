package org.sf.jlaunchpad.core;

/**
 * The class represents exception that happens during launch time.
 *
 * @author Alexander Shvets
 * @version 1.0 12/16/2006
 */
public class LauncherException extends Exception {

  /**
   * Default constructor.
   */
  public LauncherException() {
    super();
  }

  /**
   * Creates new launch exception.
   *
   * @param message the message
   */
  public LauncherException(String message) {
    super(message);
  }

  /**
   * Creates new launch exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public LauncherException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates new launch exception.
   *
   * @param cause the cause
   */
  public LauncherException(Throwable cause) {
    super(cause);
  }

}
