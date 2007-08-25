package org.sf.jlaunchpad.util;

import java.io.*;

/**
 * This class contains convenient methods for serializing/deserializing objects.
 *
 * @author Alexander Shvets
 * @version 1.0 12/16/2006
 */
public class SerializableUtil {

  /**
   * Serializes the object.
   *
   * @param object the object to be serialized
   *
   * @return object in form ob array of bytes
   * @throws IOException exception
   */
  public static byte[] serialize(Object object) throws IOException {
    byte[] bytes = new byte[0];

    if(object != null) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = null;
      try {
        oos = new ObjectOutputStream(baos);

        oos.writeObject(object);

        bytes = baos.toByteArray();
      }
      finally {
        if(oos != null) {
          oos.close();
        }
      }
    }

    FileUtil.copyToFile(new ByteArrayInputStream(bytes), new File("c:/test.class"));

    return bytes;
  }

  /**
   * Deserializes the object.
   *
   * @param bytes object in form ob array of bytes
   * @return the deserialized object
   * @throws IOException exception
   * @throws ClassNotFoundException exception 
   */
  public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
    Object object = null;

    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    ObjectInputStream ois = null;
    try {
      ois = new ObjectInputStream(bais);

      object = ois.readObject();
    }
    finally {
      if(ois != null) {
        ois.close();
      }
    }

    return object;
  }

}
