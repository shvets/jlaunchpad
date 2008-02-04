package org.sf.jlaunchpad.xml;

import org.jdom.Element;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.*;
import java.util.List;


public class XmlHelper {

  protected SAXBuilder saxBuilder = new SAXBuilder();

  protected Document document;

  public void read(File inputFile) throws IOException, JDOMException {
    document = saxBuilder.build(inputFile);
  }

  public void save(File outputFile) throws IOException {
    FileOutputStream out = new FileOutputStream(outputFile);

    XMLOutputter outputter = new XMLOutputter();
    outputter.output(document, out);
  }

  public Element getFragment(File file) throws IOException, JDOMException {
    InputStream in = new FileInputStream(file);

    Document document = saxBuilder.build(in);

    Element root = document.getRootElement();

    root.detach();

    return root;
  }

  public Element getElementByPath(Element parent, String[] path) {
    Element current = parent;

    for (String aPath : path) {
      Element e = getElementByName(current, aPath);

      current = e;

      if (e == null) {
        break;
      }
    }

    return current;
  }

  public Element getElementByName(String name) {
    return getElementByName(document.getRootElement(), name);
  }

  public Element getElementByName(Element parent, String name) {
    List children = parent.getChildren();

    for (Object o : children) {
      if (o instanceof Element) {
        Element element = (Element) o;

        if (element.getName().equals(name)) {
          return element;
        }
      }
    }

    return null;
  }

  public void printChildren(Element parent) {
    List children = parent.getChildren();

    for (Object aChildren : children) {
      Element element = (Element) aChildren;

      System.out.println(element.getName());
    }
  }

}

