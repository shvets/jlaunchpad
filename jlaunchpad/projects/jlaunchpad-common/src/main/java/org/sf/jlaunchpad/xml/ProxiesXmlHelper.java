package org.sf.jlaunchpad.xml;

import org.jdom.Element;
import org.jdom.Text;
import org.jdom.JDOMException;

import java.io.IOException;
import java.util.Properties;

public class ProxiesXmlHelper extends XmlHelper {

  public void process(Properties properties) throws IOException, JDOMException {
    Element localRepository = getLocalRepositoryElement();

    Element proxies = getProxiesElement();

    if(proxies != null) {
      proxies.detach();
    }

    String proxyHost = (String)properties.get("proxyHost");

    if(proxyHost != null && proxyHost.trim().length() > 0) {
      proxies = new Element("proxies");

      Element root = document.getRootElement();

      int index = root.indexOf(localRepository);

      root.addContent(index+1, new Text("\n  "));
      root.addContent(index+2, new Text("\n  "));
      root.addContent(index+3, proxies);

      addProxy(proxies, properties);
    }
  }

  public Element getLocalRepositoryElement() {
    Element root = document.getRootElement();

    return getElementByPath(root, new String[] { "localRepository" });
  }

  protected Element getProxiesElement() {
    Element root = document.getRootElement();

    return getElementByPath(root, new String[] { "proxies" });
  }

  public String getLocalRepository() {
    Element localRepositoryElement = getLocalRepositoryElement();

    if(localRepositoryElement != null) {
      return localRepositoryElement.getText();
    }

    return "";
  }

  private void addProxy(Element proxies, Properties properties) {
    Element proxy = new Element("proxy");

    proxies.addContent("\n    ");
    proxies.addContent(proxy);
    proxies.addContent("\n  ");

    Element active = new Element("active");
    active.setText("true");

    Element protocol = new Element("protocol");
    protocol.setText("http");

    Element port = new Element("port");
    Element host = new Element("host");
    port.setText((String)properties.get("proxyPort"));
    host.setText((String)properties.get("proxyHost"));

    Element username = new Element("username");
    Element password = new Element("password");

    if (properties.get("proxyAuth") != null && ((String)properties.get("proxyAuth")).equalsIgnoreCase("true")) {
      username.setText((String)properties.get("proxyUser"));
      password.setText((String)properties.get("proxyPassword"));
    }
    else {
      username.setText("");
      password.setText("");
    }

    Element id = new Element("id");

    proxy.addContent("\n      ");
    proxy.addContent(active);
    proxy.addContent("\n      ");
    proxy.addContent(protocol);
    proxy.addContent("\n      ");
    proxy.addContent(username);
    proxy.addContent("\n      ");
    proxy.addContent(password);
    proxy.addContent("\n      ");
    proxy.addContent(port);
    proxy.addContent("\n      ");
    proxy.addContent(host);
    proxy.addContent("\n      ");
    proxy.addContent(id);
    proxy.addContent("\n    ");
  }

}
