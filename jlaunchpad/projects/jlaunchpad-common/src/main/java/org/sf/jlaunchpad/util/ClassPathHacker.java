package org.sf.jlaunchpad.util;

// ClassPathHacker.java

import java.lang.reflect.*;
import java.io.*;
import java.net.*;
 
public class ClassPathHacker {
        private static final Class[] parameters = new Class[]{URL.class};
         
        public static void addFile(String s) {
                File f = new File(s);
                addFile(f);
        }
        
        /* File.toURL() was deprecated, so use File.toURI().toURL() */
        public static void addFile(File f) {
                try {
                        addURL(f.toURI().toURL());
                }
                catch (Exception e) {
                        e.printStackTrace();
                }
        }
        
        public static void addURL(URL u) {      
                URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
                try {
                        /* Class was uncheched, so used URLClassLoader.class instead */
                        Method method = URLClassLoader.class.getDeclaredMethod("addURL",parameters);
                        method.setAccessible(true);
                        method.invoke(sysloader,new Object[]{u});
                        System.out.println("Dynamically added " + u.toString() + " to classLoader");
                } 
                catch (Exception e) {
                        e.printStackTrace();
                }
        }

       public static void addResource(String myClass, String path) { 

                // Retrieve the current classpath
                ClassLoader currentClassLoader = ClassPathHacker.class.getClassLoader();

                // Convert myClassPath to an URL
                URL classpathURL = null;
                try {
                  File fileClasspath = new File(path);
                classpathURL = fileClasspath .toURL();
                }
                catch (MalformedURLException ex)
                {
                // handle the exception
                }

                // Construct the new URL
                URL [] urlClasspathArray = new URL [1];
                urlClasspathArray[0] = classpathURL;

                // Create the new classloader
                URLClassLoader newClassLoader = new URLClassLoader(urlClasspathArray,currentClassLoader);

                // Load the class
                Class theClass = null;
                try {
                theClass = newClassLoader.loadClass(myClass);
                }
                catch (ClassNotFoundException ex)
                {
                // handle the exception
                }

                // At this step,
                // Either an exception has been thrown (MalformedURLException or
                // ClassNotFoundException)
                // or the class "myClass" is loaded.
        }


}
