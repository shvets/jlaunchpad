import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.codehaus.classworlds.ClassWorld;
import org.sf.jlaunchpad.JLaunchPadLauncher;
import org.sf.jlaunchpad.DepsLauncher;
import org.sf.jlaunchpad.core.LauncherCommandLineParser;
import org.sf.jlaunchpad.core.SimpleLauncher;

public class UniversalLauncherTest extends TestCase {

  protected void setUp() throws Exception {
    super.setUp();

    System.setProperty("repository.home", "c:/maven-repository");
    System.setProperty("launcher.home", "c:/launcher");
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

  public void testCreateUniversalLauncher() {
    String[] args = new String[]{};

    try {
      ClassWorld classWorld = new ClassWorld();

      // LauncherCommandLineParser parser = new LauncherCommandLineParser();

/*      List<String> depsFileNames = new ArrayList<String>();

      depsFileNames.add("C:/maven-repository/org/sf/scriptlandia/beanshell-starter/1.0.0/beanshell-starter-1.0.0.pom ");
      parser.getCommandLine().put("main.class.name", "bsh.Interpreter");
      parser.getCommandLine().put("deps.file.name", depsFileNames);
*/
      // String[] newArgs = parser.parse(args);

      // JLaunchPadLauncher.main(args, classWorld);

      //JLaunchPadLauncher launcher = JLaunchPadLauncher.getInstance();

      // launcher.configure(Thread.currentThread().getContextClassLoader());

      //ScriptlandiaHelper.resolveDependencies("com.google.gdata", "gdata-youtube", "1.0", launcher);


      LauncherCommandLineParser parser = new LauncherCommandLineParser();

      JLaunchPadLauncher launcher = new JLaunchPadLauncher(parser, args, classWorld);

      launcher.configure(Thread.currentThread().getContextClassLoader());

      launcher.launch();
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  public void testCreateJLaunchPad() {
    String repositoryHome = System.getProperty("repository.home");

    String[] args = new String[] {};

    ClassWorld classWorld = new ClassWorld();

    LauncherCommandLineParser parser = new LauncherCommandLineParser();

    try {
      DepsLauncher launcher = new DepsLauncher(parser, args, classWorld);

      launcher.configure(Thread.currentThread().getContextClassLoader());

      launcher.addClasspathEntry(repositoryHome + "/org/sf/jlaunchpad/jlaunchpad-common/1.0.1/jlaunchpad-common-1.0.1.jar");
      launcher.addClasspathEntry(repositoryHome + "/org/apache/maven/bootstrap/bootstrap-mini/2.0.8/bootstrap-mini-2.0.8.jar");
      launcher.addClasspathEntry(repositoryHome + "/org/sf/jlaunchpad/pom-reader/1.0.1/pom-reader-1.0.1.jar");
      launcher.addClasspathEntry(repositoryHome + "/org/sf/jlaunchpad/jlaunchpad-launcher/1.0.1/jlaunchpad-launcher-1.0.1.jar");

      // 1. download dependency

      // 2. have it on CLASSPATH
      launcher.resolveDependencies("bsh", "bsh", "2.0b5");
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  public void testInstallDependency() {
    String[] args = new String[]{
        "-basedir",
        "projects/scriptlandia-startup",
        "-build.required",
        "true"
    };

    try {
      SimpleLauncher launcher = new SimpleLauncher(args);

      launcher.setMainClassName("org.sf.pomreader.ProjectInstaller");

      String repositoryHome = System.getProperty("repository.home");

      launcher.addClasspathEntry(repositoryHome + "/org/sf/jlaunchpad/jlaunchpad-common/1.0.1/jlaunchpad-common-1.0.1.jar");
      launcher.addClasspathEntry(repositoryHome + "/org/apache/maven/bootstrap/bootstrap-mini/2.0.8/bootstrap-mini-2.0.8.jar");
      launcher.addClasspathEntry(repositoryHome + "/org/sf/jlaunchpad/pom-reader/1.0.1/pom-reader-1.0.1.jar");
      launcher.addClasspathEntry(repositoryHome + "/org/sf/jlaunchpad/jlaunchpad-launcher/1.0.1/jlaunchpad-launcher-1.0.1.jar");

      launcher.configure(Thread.currentThread().getContextClassLoader());
      launcher.launch();
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  public static void main(String[] args) {
    TestSuite suite = new TestSuite();

    suite.addTestSuite(UniversalLauncherTest.class);

    TestRunner.run(suite);
  }

}