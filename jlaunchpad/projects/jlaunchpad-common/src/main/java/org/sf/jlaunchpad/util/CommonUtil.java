package org.sf.jlaunchpad.util;

import java.io.File;
import java.io.IOException;

import java.util.jar.Manifest;
import java.util.jar.Attributes;

/**
 * This is the class for holding common utilities.
 *
 * @author Alexander Shvets
 * @version 1.0 02/19/2006
 */
public class CommonUtil {

  /**
   * Checks if the Java is working in AWT or Swing mode.
   *
   * @return true if the Java is working in AWT or Swing mode; false otherwise
   */
  public static boolean isGuiMode() {
    Thread currentThread = Thread.currentThread();

    ThreadGroup root = currentThread.getThreadGroup();

    boolean ok = false;

    while (!ok) {
      ThreadGroup parent = root.getParent();

      if (parent == null) {
        ok = true;
      } else {
        root = parent;
      }
    }

    Thread[] list = new Thread[root.activeCount()];

    root.enumerate(list, true);

    boolean isGuiMode = false;

    for (int i = 0; i < list.length && !isGuiMode; i++) {
      Thread thread = list[i];

      if (thread != null) {
        String threadName = thread.getName();

        if (threadName.startsWith("AWT-")) {
          isGuiMode = true;
        }
      }
    }

    return isGuiMode;
  }

  /**
   * Prints threads list.
   */
  public static void printThreads() {
    Thread currentThread = Thread.currentThread();

    ThreadGroup root = currentThread.getThreadGroup();

    boolean ok = false;

    while (!ok) {
      ThreadGroup parent = root.getParent();

      if (parent == null) {
        ok = true;
      } else {
        root = parent;
      }
    }

    Thread[] list = new Thread[root.activeCount()];

    root.enumerate(list, true);

    boolean isGuiMode = false;

    for (int i = 0; i < list.length && !isGuiMode; i++) {
      Thread thread = list[i];

      if (thread != null) {
        System.out.println("Thread: " + thread.getName());
      }
    }
  }

  /**
   * Gets the compiler jar file.
   *
   * @return the compiler jar file
   */
  public static File getCompilerJar() {
    File compilerJar = null;

    File toolsJar = new File(System.getProperty("java.home"), "../lib/tools.jar");

    if (toolsJar.exists()) {
      compilerJar = toolsJar;
    }
    else {
      toolsJar = new File(System.getProperty("java.home.internal"), "/lib/tools.jar");

      if (toolsJar.exists()) {
        compilerJar = toolsJar;
      }
      else {
        String javaCompilerVersion = System.getProperty("java.compiler.version");

        if(javaCompilerVersion != null) {
          toolsJar = new File(System.getProperty("repository.home"),
            "/com/sun/tools/javac/" + javaCompilerVersion + "/javac-" + javaCompilerVersion + ".jar");
          if (toolsJar.exists()) {
            compilerJar = toolsJar;
          }
        }
      }
    }

/*    String javaVersion = System.getProperty("java.version");

    if(javaVersion.startsWith("1.5")) {
      String javacInternalVersion = "7.0-b07";

      compilerJar = new File(System.getProperty("repository.home"),
            "/com/sun/tools/javac/" + javacInternalVersion + "/javac-" + javacInternalVersion + ".jar");
    }
  */
    return compilerJar;
  }

  /**
   * Sets up the java specification version.
   * @return java specification version
   * @throws IOException I/O Exception
   */
  public static String getJavaSpecificationVersion() throws IOException {
    String specificationVersion = null;

    String rtJarName = System.getProperty("java.home") + "../jre/lib/rt.jar";

    if (!new File(rtJarName).exists()) {
      rtJarName = System.getProperty("java.home") + "/jre/lib/rt.jar";

/*      if (!new File(rtJarName).exists()) {  
        rtJarName = System.getProperty("java.home.internal") + "/jre/lib/rt.jar";
      }
*/
    }

    if (new File(rtJarName).exists()) {  
      final Manifest manifest = FileUtil.getManifest(rtJarName);

      final Attributes mainAttributes = manifest.getMainAttributes();

      specificationVersion = mainAttributes.getValue("Specification-Version");
    }

    return specificationVersion;
  }

  /**
   *  1.1: 00 2D (= 45)
   *  1.2: 00 2E (= 46)
   *  1.3: 00 2F (= 47)
   *  1.4: 00 30 (= 48)
   *  1.5: 00 31 (= 49)
   *  1.6: 00 32 (= 50)
   *
   * @param majorVersion the major version
   * @return java specification version
   */
  public String calculateJavaSpecificationVersion(int majorVersion) {
    String javaSpecificationVersion = "1.1";

    if(majorVersion == 45) {
      javaSpecificationVersion = "1.1";
    }
    else if(majorVersion == 46) {
      javaSpecificationVersion = "1.2";
    }
    else if(majorVersion == 47) {
      javaSpecificationVersion = "1.3";
    }
    else if(majorVersion == 48) {
      javaSpecificationVersion = "1.4";
    }
    else if(majorVersion == 49) {
      javaSpecificationVersion = "1.5";
    }
    else if(majorVersion == 50) {
      javaSpecificationVersion = "1.6";
    }

    return javaSpecificationVersion;
  }

}
