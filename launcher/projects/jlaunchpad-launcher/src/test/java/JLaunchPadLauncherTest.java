import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.codehaus.classworlds.ClassWorld;
import org.sf.jlaunchpad.DepsLauncher;
import org.sf.jlaunchpad.JLaunchPadLauncher;
import org.sf.jlaunchpad.install.GuiInstaller;
import org.sf.jlaunchpad.core.LauncherCommandLineParser;

import java.util.ArrayList;
import java.util.List;

public class JLaunchPadLauncherTest extends TestCase {

  protected void setUp() throws Exception {
    super.setUp();

    System.setProperty("repository.home", "c:/maven-repository");
    System.setProperty("launcher.home", "c:/launcher");
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

  public void testCreateJLaunchPad() {
    String[] args = new String[]{};

    ClassWorld classWorld = new ClassWorld();

    LauncherCommandLineParser parser = new LauncherCommandLineParser();

    try {
      DepsLauncher launcher = new DepsLauncher(parser, args, classWorld);

      launcher.configure(Thread.currentThread().getContextClassLoader());

      // 1. download dependency
      launcher.resolveDependencies("bsh", "bsh", "2.0b5");
      // 2. have it on CLASSPATH
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  public void testCreateJLaunchPadLauncher() {
    String[] args = new String[]{};

    try {
      ClassWorld classWorld = new ClassWorld();

      List<String> depsFileNames = new ArrayList<String>();

      depsFileNames.add("projects/jlaunchpad-launcher/pom.xml ");
      LauncherCommandLineParser parser = new LauncherCommandLineParser();

      JLaunchPadLauncher launcher = new JLaunchPadLauncher(parser, args, classWorld);
      parser.getCommandLine().put("main.class.name", "org.sf.pomreader.ProjectInstaller");
      parser.getCommandLine().put("deps.file.name", depsFileNames);

      launcher.configure(Thread.currentThread().getContextClassLoader());

      launcher.launch();
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  public void testInstallProject() {
    String[] args = new String[]{
        "-basedir", "projects/jlaunchpad-launcher/src/test/resources/test-data1",
        "-build.required", "false"
    };

    List<String> depsFileNames = new ArrayList<String>();

    depsFileNames.add("projects/jlaunchpad-launcher/pom.xml ");

    LauncherCommandLineParser parser = new LauncherCommandLineParser();
    parser.getCommandLine().put("main.class.name", "org.sf.pomreader.ProjectInstaller");
    parser.getCommandLine().put("deps.file.name", depsFileNames);

    try {
      ClassWorld classWorld = new ClassWorld();

      JLaunchPadLauncher launcher = new JLaunchPadLauncher(parser, args, classWorld);

      launcher.configure(Thread.currentThread().getContextClassLoader());

      launcher.launch();
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  public void testGuiInstaller() {
    String[] args = new String[]{};

    try {
      GuiInstaller.main(args);

      Thread.currentThread().join();
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  public static void main(String[] args) {
    TestSuite suite = new TestSuite();

    suite.addTestSuite(JLaunchPadLauncherTest.class);

    TestRunner.run(suite);
  }

}