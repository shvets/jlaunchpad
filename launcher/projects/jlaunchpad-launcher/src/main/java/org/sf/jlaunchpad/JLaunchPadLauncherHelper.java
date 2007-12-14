package org.sf.jlaunchpad;

/**
 * This is the class for holding convenient methods when working with
 * JLaunchPad CLASSPATH.
 *
 * @author Alexander Shvets
 * @version 2.0 09/09/2007
 */
public class JLaunchPadLauncherHelper {

  /**
   * Disables object creation.
   */
  private JLaunchPadLauncherHelper() {}

  /**
   * Resolves dependencies for specified artifact.
   *
   * @throws Exception the exception
   * @param groupId group ID
   * @param artifactId artifact ID
   * @param version version
   * @param classifier classifier
   */
  public static void resolveDependencies(String groupId, String artifactId, String version, String classifier)
         throws Exception {
    JLaunchPadLauncher.getInstance().resolveDependencies(groupId, artifactId, version, classifier);
  }

  /**
   * Resolves dependencies for specified artifact.
   *
   * @throws Exception the exception
   * @param groupId group ID
   * @param artifactId artifact ID
   * @param version version
   */
  public static void resolveDependencies(String groupId, String artifactId, String version)
         throws Exception {
    JLaunchPadLauncher.getInstance().resolveDependencies(groupId, artifactId, version);
  }

  /**
   * Resolves dependencies for specified dependencies file.
   *
   * @param depsFileName deps file name
   * @throws Exception the exception
   */
  public static void resolveDependencies(String depsFileName) throws Exception {
    JLaunchPadLauncher.getInstance().resolveDependencies(depsFileName);
  }
    
}
