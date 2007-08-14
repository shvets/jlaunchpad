package org.sf.jlaunchpad;

import org.codehaus.classworlds.ClassRealm;
import org.codehaus.classworlds.ClassWorld;
import org.codehaus.classworlds.DuplicateRealmException;
import org.codehaus.classworlds.NoSuchRealmException;
import org.sf.jlaunchpad.core.LauncherException;
import org.sf.jlaunchpad.core.LauncherCommandLineParser;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

public class ClassworldLauncher extends CoreLauncher {
  /* Interactive flag. */
  protected boolean isInteractive = false;

  /** The list of configured realms. */
  protected Map<String, ClassRealm> configuredRealms = new HashMap<String, ClassRealm>();

  /** The original classworld launcher. */
  private org.codehaus.classworlds.Launcher classworldLauncher = new org.codehaus.classworlds.Launcher();

  /**
   * Creates new classworld launcher.
   *
   * @param classWorld the classworld
   * @param parser the parser
   * @param args command line arguments
   */
  public ClassworldLauncher(LauncherCommandLineParser parser, String[] args, ClassWorld classWorld) {
    super(parser, args, classWorld);
  }

  /**
   * Gets main realm.
   *
   * @return the main realm
   * @throws NoSuchRealmException the exception
   */
  public ClassRealm getMainRealm() throws NoSuchRealmException {
    return classworldLauncher.getMainRealm();    
  }

  /**
   * Checks if interactive mode has been requested.
   *
   * @return true if interactive mode has been requested; false otherwise
   */
  public boolean isInteractive() {
    return isInteractive;
  }

  /**
   * Sets the interactive flag.
   *
   * @param interactive the interactive flag
   */
  public void setInteractive(boolean interactive) {
    isInteractive = interactive;
  }

  /**
   * Interacts with the user.
   *
   * @param args command line parameters
   */
  protected void interact(final String[] args) {
    BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      System.out.print("Enter your command line parameters: ");

      String commandLine = null;
      try {
        commandLine = keyboard.readLine();
      }
      catch (IOException e) {
        e.printStackTrace();
      }

      if (commandLine == null) {
        break;
      }

      commandLine = commandLine.trim();

      if (commandLine.equalsIgnoreCase("quit") ||
        commandLine.equalsIgnoreCase("q")) {
        break;
      }

      final StringTokenizer st = new StringTokenizer(commandLine);

      final List<String> newArgsList = new ArrayList<String>();

      newArgsList.addAll(Arrays.asList(args));

      while (st.hasMoreTokens()) {
        newArgsList.add(st.nextToken());
      }

      String[] newArgs = new String[newArgsList.size()];
      newArgsList.toArray(newArgs);

      try {
        this.args = newArgs;
        launch();
      }
      catch (Throwable t) {
        // supress all exceptions to not to break the iteration
        t.printStackTrace();
      }
    }
  }

  /**
   * Adds discovered libraries (folders or jar files) to the class realm.
   *
   * @param libPaths the list of library paths
   * @throws Exception the exception
   */
  protected void addLibraries(List<String> libPaths) throws Exception {
    ClassRealm classRealm = getMainRealm();

    for (String libPath : libPaths) {
      File libPathFile = new File(libPath);

      if (libPathFile.isDirectory()) {
        File[] files = libPathFile.listFiles();

        for (File file : files) {
          classRealm.addConstituent(file.toURI().toURL());
        }
      }
    }
  }

  /**
   * Gets the realm name.
   *
   * @return the realm name
   */
  protected String getRealmName() {
    return "classworlds-launcher";
  }

  /**
   * Configures the launcher.
   *
   * @param parentClassLoader parent class loader
   * @throws LauncherException the exception
   */
  public void configure(ClassLoader parentClassLoader) throws LauncherException {
    classworldLauncher.setSystemClassLoader(parentClassLoader);

    classworldLauncher.setWorld(classWorld);

    String realmName = getRealmName();

    classworldLauncher.setAppMain(mainClassName, realmName);

    try {
      ClassRealm curRealm = classWorld.newRealm(realmName, parentClassLoader);

      configuredRealms.put(realmName, curRealm);
    }
    catch (DuplicateRealmException e) {
      throw new LauncherException(e);
    }

    // Associate child realms to their parents.
    associateRealms();
  }

  /**
   * Main launcher method.
   *
   * @throws LauncherException the exception
   */
  public void launch() throws LauncherException {
    boolean isExceptionThrown = false;

    try {
      final List<String> libPaths = new ArrayList<String>();
      Map<String, String> systemParams = new HashMap<String, String>();

      String[] newArgs = processSpecialParameters(args, libPaths, systemParams);

      for (String key : systemParams.keySet()) {
        System.setProperty(key, systemParams.get(key));
      }

      addLibraries(libPaths);

      if (isInteractive()) {
        interact(newArgs);
      } else {
        classworldLauncher.launch(newArgs);
      }

      exitCode = classworldLauncher.getExitCode();
    }
    catch (InvocationTargetException e) {
      e.printStackTrace();
      isExceptionThrown = true;

      printConstituents();

      // Decode ITE (if we can)
      Throwable t = e.getTargetException();

      if (t instanceof Exception) {
        throw new LauncherException(t);
      }
      if (t instanceof Error) {
        throw (Error) t;
      }

      throw new LauncherException(e);
    }
    catch (Exception e) {
      e.printStackTrace();
      isExceptionThrown = true;

      printConstituents();

      throw new LauncherException(e);
    }
    catch (Throwable e) {
      isExceptionThrown = true;
    }
    finally {
      if (!isExceptionThrown) {

        if (isWaitMode() && exitCode == 0) {
          try {
            Thread.currentThread().join();
          }
          catch (InterruptedException e) {
            //noinspection ThrowFromFinallyBlock
            throw new LauncherException(e);
          }
        }
      }
    }
  }

  /**
   * Checks for "wait" mode.
   *
   * @return true if "wait" mode; false otherwise
   */
  public boolean isWaitMode() {
    return parser.isWaitMode();
  }

  /**
   * Prints current classpath content.
   */
  public void printConstituents() {
    try {
      System.out.println(this);
      System.out.println(classworldLauncher.getWorld());
      System.out.println(classworldLauncher.getMainRealmName());

      ClassRealm realm = classworldLauncher.getMainRealm();

      URL[] constituents = realm.getConstituents();

      System.out.println("---------------------------------------------------");

      for (int i = 0; i < constituents.length; i++) {
        System.out.println("constituent[" + i + "]: " + constituents[i]);
      }

      System.out.println("---------------------------------------------------");

    }
    catch (NoSuchRealmException e) {
      System.out.println("No such realm: " + e.getMessage());
    }
  }

  /**
   * Processes "-lib" parameter pairs and adds libraries to the class realm.
   * Processes "-D", system parameters.
   *
   * @param args         original arguments
   * @param libPaths     the list of library paths
   * @param systemParams the collection of system parameters
   * @return new arguments without "-lib" parameters
   */
  public static String[] processSpecialParameters(String[] args, List<String> libPaths,
                                                  Map<String, String> systemParams) {
    List<String> newArgsList = new ArrayList<String>();

    processLibParameters(args, newArgsList, libPaths);

    List<String> newArgsList2 = new ArrayList<String>();

    for (int i = 0; i < newArgsList.size(); i++) {
      String arg = newArgsList.get(i);

      if (arg.startsWith("-D")) {
        String key = arg.substring(2);

        String value;

        if (i + 1 < newArgsList.size()) {
          value = newArgsList.get(i + 1);
        } else {
          value = "";
        }

        //System.setProperty(key, value);

        systemParams.put(key, value);

        newArgsList2.add(arg);
      } else if (!arg.startsWith("-X")) {
        newArgsList2.add(arg);
      }
    }

    String[] newArgs = new String[newArgsList2.size()];

    newArgsList2.toArray(newArgs);

    return newArgs;
  }

  /**
   * Prepares new command line without "-lib" parameter pairs.
   *
   * @param args     original arguments
   * @param newArgs  the list of new arguments
   * @param libPaths the list of library paths
   */
  public static void processLibParameters(final String[] args, List<String> newArgs,
                                          final List<String> libPaths) {
    for (int i = 0; i < args.length; i++) {
      final String arg = args[i];

      if (arg.equals("-lib")) {
        if (i < args.length - 1) {
          ++i;

          final String libPath = args[i];

          if (!libPaths.contains(libPath)) {
            libPaths.add(libPath);
          }
        }
      } else {
        newArgs.add(arg);
      }
    }
  }

  /**
   * Associate parent realms with their children.
   */
  protected void associateRealms() {
    List<String> sortRealmNames = new ArrayList<String>(configuredRealms.keySet());

    // sort by name
    Comparator<String> comparator = new Comparator<String>() {
      public int compare(String s1, String s2) {
        return s1.compareTo(s2);
      }
    };

    Collections.sort(sortRealmNames, comparator);

    // So now we have something like the following for defined
    // realms:
    //
    // root
    // root.maven
    // root.maven.plugin
    //
    // Now if the name of a realm is a superset of an existing realm
    // the we want to make child/parent associations.

    for (String realmName : sortRealmNames) {
      int j = realmName.lastIndexOf('.');

      if (j > 0) {
        String parentRealmName = realmName.substring(0, j);

        ClassRealm parentRealm = configuredRealms.get(parentRealmName);

        if (parentRealm != null) {
          ClassRealm realm = configuredRealms.get(realmName);

          realm.setParent(parentRealm);
        }
      }
    }
  }

}
