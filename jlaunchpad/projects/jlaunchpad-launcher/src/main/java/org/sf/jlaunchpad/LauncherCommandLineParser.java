package org.sf.jlaunchpad;

import java.util.*;

/**
 * The command line parser for the launcher.
 *
 * @author Alexander Shvets
 * @version 1.0 12/16/2006
 */
public class LauncherCommandLineParser {
  /** The collection of significant parameters. */
  protected Map<String, Object> commandLine = new HashMap<String, Object>();

  /**
   * Parses the command line.
   *
   * @param args the array of arguments
   * @return the modified command line
   */
  public String[] parse(String[] args) {
    List<String> newArgsList = new ArrayList<String>();

    for (String arg : args) {
      if (arg.startsWith("-")) {
        if (arg.equalsIgnoreCase("-i") || arg.equalsIgnoreCase("-is.interactive")) {
          commandLine.put("is.interactive", "");
          //newArgsList.add("-i");          
        }
        else if (arg.toLowerCase().startsWith("-deps.file.name=")) {
          int index = arg.indexOf("=");

          List<String> depsFileName = (List<String>) commandLine.get("deps.file.name");
          if(depsFileName == null) {
            depsFileName = new ArrayList<String>();
            commandLine.put("deps.file.name", depsFileName);
          }

          depsFileName.add(arg.substring(index + 1));
        }
          else if (arg.toLowerCase().startsWith("-classpath.file.name=")) {
          int index = arg.indexOf("=");

          commandLine.put("classpath.file.name", arg.substring(index + 1));
        }
        else if (arg.toLowerCase().startsWith("-main.class.name=")) {
          int index = arg.indexOf("=");

          commandLine.put("main.class.name", arg.substring(index + 1));
        }
        else if(arg.equalsIgnoreCase("-wait")) {
          commandLine.put("wait.mode", "true");
        }
        else if(arg.equalsIgnoreCase("-pomstarter")) {
          commandLine.put("pomstarter.mode", "true");
        }
        else {
          newArgsList.add(arg);
        }
      }
      else {
        newArgsList.add(arg);
      }
    }

    String[] newArgs = new String[newArgsList.size()];

    newArgsList.toArray(newArgs);

    return newArgs;
  }

  /**
   * Checks if interactive mode has been requested.
   *
   * @return true if interactive mode has been requested; false otherwise
   */
  public boolean isInteractive() {
    return commandLine.get("is.interactive") != null;
  }

  /**
   * Gets the starter dependencies file name.
   *
   * @return starter dependencies file name
   */
  public List<String> getStarterDepsFileNames() {
    return (List<String>)commandLine.get("deps.file.name");
  }

  /**
   * Gets the starter class name.
   *
   * @return starter class name
   */
  public String getStarterClassName() {
    return (String)commandLine.get("main.class.name");
  }

  /**
   * Checks if wait mode.
   *
   * @return true if wait mode; false otherwise
   */
  public boolean isWaitMode() {
    String s = (String)commandLine.get("wait.mode");
    return s != null && s.equalsIgnoreCase("true");
  }

  /**
   * Checks if pomstarter mode.
   *
   * @return true if wait mode; false otherwise
   */
  public boolean isPomstarterMode() {
    String s = (String)commandLine.get("pomstarter.mode");
    return s != null && s.equalsIgnoreCase("true");
  }

  /**
   * Gets the command line parameters.
   *
   * @return the command line parameters
   */
  public Map<String, Object> getCommandLine() {
    return commandLine;
  }
  
}
