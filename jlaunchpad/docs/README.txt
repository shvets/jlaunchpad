    JLaunchPad tutorial

    Description 

JLaunchPad (launcher) is the set of Java classes and shell scripts for simplifying installation/launching
of Java applications. Once the launcher is installed, it can be reused for starting different
Java applications.

For your application you have to specify required dependencies on other Java libraries. When 
application is getting executed first time, all dependencies will be downloaded and installed 
automatically into your local repository . For all consequent executions of the application 
download process is not required and the only one responsibility of the launcher is to build 
correct "classpath" and launch the application.


    Installation

In order to install launcher you should have preinstalled Java Virtual Machine. In "config.bat/config.sh"
modify "JAVA_HOME" variable to point to preinstalled Java. Now you should run this command:

>installer.bat

or 

>installer.sh

The program will ask to set up few values/locations:

- Java Home (JAVA_HOME);
- JLaunchPad Home (JLAUNCHPAD_HOME);
- Repository Home (REPOSITORY_HOME).

Java Home variable specifies the location of JDK that will be used for launching Java application. 

Launcher Home is the location, where launcher will be installed.

Repository Home (local repository) is used for storing all java libraries, required by the launcher 
and launching program. 


    Architecture


1. On the dedicated server we have Central Repository (sponsored, non-profit, for everybody
in the community/ IT industry) of java components represented in binary format. It could 
be a separate java library, some convenient tool or even GUI program.

2. Each component is provided with the group name, artifact name and the version. Classifier
also could be used for specifying Java version of the component (e.g. jdk15, jdk16 etc.).

3. Each component has binary artifact and could also contain (optional) sources, javadocs or 
other artifacts.

4. Each component describes dependencies to other components in the form of Dependencies File.
As the result, we have Dependencies Tree (or Transitive Dependencies).

5. Launcher program connects to the remote Central Repositories and downloads required 
components to the client's computer. Then the launcher builds correct CLASSPATH and then 
starts up the programs. 

6. All downloaded components are stored in the Local Repository - it is the mirror of 
Central Repositories and it contains only required components with their dependencies.

7. If somebody wants to introduce new program, s(he) describes it in the form of dependencies,
then s(he) writes the code. As the result, it is required to distribute new code only - 
all dependencies will be downloaded later and only "on-demand" - when it is really required.

8. "Smart" start-up program reads Dependencies File, installs all the required dependencies
and then starts the original program.

This is real separation of new code from related dependencies. If your application 
is, say, dependent on "jdom" library, your distribution does not have to include this file. 
Instead, you provide dependencies for the project and they will be downloaded automatically.

    Implementation

For the implementation the following projects were reused:

- classworlds project        http://dist.codehaus.org/classworlds
- bootstrap-mini project     http://svn.apache.org/repos/asf/maven/components/tags/maven-2.0.8/bootstrap
- Java App Launcher          https://java-app-launcher.dev.java.net

    Examples

    See "samples" folder. 
