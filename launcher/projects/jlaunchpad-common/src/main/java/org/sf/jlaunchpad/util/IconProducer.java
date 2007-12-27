// IconProducer.class

package org.sf.jlaunchpad.util;

import java.io.*;
//import java.net.URL;
import java.awt.Toolkit;
import java.awt.Image;
import javax.swing.ImageIcon;

public class IconProducer {
  private static Class clazz = Object.class;

  public static void setClass(Class c) {
    clazz = c;
  }

  public static ImageIcon getImageIcon(String iconFileName) {
    return getImageIcon(iconFileName, clazz);
  }

  public static ImageIcon getImageIcon(String iconFileName, Class clazz) {
    InputStream is;

    is = clazz.getResourceAsStream(iconFileName);

    if(is == null) {
      is = clazz.getClassLoader().getResourceAsStream(iconFileName);
    }

    if(is != null) {
      Toolkit tk = Toolkit.getDefaultToolkit();

      try {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int c = is.read();
        while (c != -1) {
          os.write(c);
          c = is.read();
        }

        is.close();
        os.close();

        Image image = tk.createImage(os.toByteArray());

        if(image != null) {
          return new ImageIcon(image);
        }
      }
      catch(IOException e) {
        e.printStackTrace();
      }
    }

    return null;
  }

}

