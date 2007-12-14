import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.sf.pomreader.PomReader;

import java.net.URL;
import java.util.List;

public class PomReaderTest extends TestCase {
  protected void setUp() throws Exception {
    super.setUp();

    System.setProperty("repository.home", "c:/maven-repository");
    System.setProperty("launcher.home", "c:/launcher");
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

  public void testCreatePom() {
    try {
      PomReader pomReader = new PomReader();

      pomReader.init();
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  public void testCaluclateDependencies() {
    try {
      PomReader pomReader = new PomReader();
      pomReader.init();

      List<URL> deps = pomReader.calculateDependencies("com.google.gdata", "gdata-youtube", "1.0", null);

      System.out.println("deps: " + deps);

    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  public static void main(String[] args) {
    TestSuite suite = new TestSuite();

    suite.addTestSuite(PomReaderTest.class);

    TestRunner.run(suite);
  }

}
