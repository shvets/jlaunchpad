package org.sf.jlaunchpad.core;

import java.util.*;

/**
 * The command line parser for the launcher.
 *
 * @author Alexander Shvets
 * @version 1.0 12/16/2006
 */
public class LauncherCommandLineParser {
  /** The collection of significant parameters. */
  protected Map<String, String> commandLine = new HashMap<String, String>();

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

          commandLine.put("deps.file.name", arg.substring(index + 1));
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
  public String getStarterDepsFileName() {
    return commandLine.get("deps.file.name");
  }

  /**
   * Gets the starter class name.
   *
   * @return starter class name
   */
  public String getStarterClassName() {
    return commandLine.get("main.class.name");
  }


  public boolean isWaitMode() {
    String s = commandLine.get("wait.mode");
    return s != null && s.equalsIgnoreCase("true");
  }

  /**
   * Gets the command line parameters.
   *
   * @return the command line parameters
   */
  public Map<String, String> getCommandLine() {
    return commandLine;
  }
  
}
