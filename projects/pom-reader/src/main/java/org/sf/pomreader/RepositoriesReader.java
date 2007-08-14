package org.sf.pomreader;

import org.apache.maven.bootstrap.util.AbstractReader;
import org.apache.maven.bootstrap.model.Repository;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

import javax.xml.parsers.ParserConfigurationException;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

/**
 * This class reads "repositories.xml" file to get information about used repositories.
 *
 * @author Alexander Shvets
 * @version 1.0 12/16/2006
 */
public class RepositoriesReader extends AbstractReader {
  /** helper fields. */
  private boolean insideRepository;
  private Repository currentRepository;
  private StringBuffer bodyText = new StringBuffer();

  /** The list of repositories. */
  private List<Repository> repositories = new ArrayList<Repository>();

  /**
   * Parses the file.
   *
   * @param file the file
   * @throws ParserConfigurationException the exception
   * @throws SAXException the exception
   * @throws IOException the exception
   */
  public void parseFile(File file) throws ParserConfigurationException, SAXException, IOException {
    repositories.clear();

    super.parse(file);
  }

  /**
   * Gets the list of repositories.
   *
   * @return the list of repositories
   */
  public List<Repository> getRepositories() {
    return repositories;
  }

  /* Helper methods for SAX parser. */

  public void startElement(String uri, String localName, String rawName, Attributes attributes) {
    if (rawName.equals("repository")) {
      insideRepository = true;

      currentRepository = new Repository();
      
      currentRepository.setSnapshots(false);
    }
  }

  public void endElement(String uri, String localName, String rawName) throws SAXException {
    if (rawName.equals("repository")) {
      insideRepository = false;

      repositories.add(currentRepository);
    }
    else if (insideRepository) {
      if (rawName.equals("id")) {
        currentRepository.setId(getBodyText());
      }
      else if (rawName.equals("url")) {
        currentRepository.setBasedir(replaceNames(getBodyText()));
      }
      else if (rawName.equals("layout")) {
        currentRepository.setLayout(getBodyText());
      }
    }

    bodyText.delete(0, bodyText.length());
  }

  private String replaceNames(String text) {
    StringBuffer newText = new StringBuffer();

    String s = text;

    boolean ok = false;
    while(!ok) {
      int index1 = s.indexOf("${");

      if(index1 == -1) {
        newText.append(s);

        ok = true;
      }
      else {
        int index2 = s.indexOf("}");

        if(index2 == -1) {
          newText.append(s);

          ok = true;
        }
        else {
          String propertyName = s.substring(index1+2, index2);

          String propertyValue = System.getProperty(propertyName);

          if(propertyValue != null) {
            newText.append(s.substring(0, index1));            
            newText.append(propertyValue.replace('\\', '/'));
          }

          s = s.substring(index2+1);
        }
      }
    }

    return newText.toString();
  }

  public void characters(char buffer[], int start, int length) {
    bodyText.append(buffer, start, length);
  }

  private String getBodyText() {
    return bodyText.toString().trim();
  }

}
