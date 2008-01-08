// TestUtils.java
// http://blog.dhananjaynene.com/archives/8

package org.sf.jlaunchpad.util;

import java.lang.reflect.*;
import java.io.*;
import java.net.*;

public class TestUtils

{

   public static void addURL(Class<?> clazz)

   {

       try

       {



           URLClassLoader sysloader = (URLClassLoader) ClassLoader

                   .getSystemClassLoader();

           Class<URLClassLoader> sysclass = URLClassLoader.class;



           String path = sysloader.getResource(

                   clazz.getCanonicalName().replace('.', '/') + ".class")

                   .getPath();

           int lastSlash = path.lastIndexOf('/');

           path = path.substring(lastSlash+1);

           URL url = new URL("file://" + path);



           Method method = sysclass.getDeclaredMethod("addURL", URL.class);

           method.setAccessible(true);

           method.invoke(sysloader, new Object[] { url });

       }

       catch (Throwable t)

       {

           t.printStackTrace();

       }

   }

}
