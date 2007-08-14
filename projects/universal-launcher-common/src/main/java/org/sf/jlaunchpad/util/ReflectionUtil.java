package org.sf.jlaunchpad.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * This is the class for holding reflection utilities.
 *
 * @author Alexander Shvets
 * @version 1.0 02/19/2006
 */
public class ReflectionUtil {

  /**
   * Searches for the "main" method.
   *
   * @param clazz the class where we are searching for "main" methd
   * @return the "main" method
   */
  public static Method getMainMethod(final Class clazz) {
    return getMainMethod(clazz, new Class[]{java.lang.String[].class});
  }

  /**
   * Searches for the "main" method.
   *
   * @param clazz the class where we are searching for "main" methd
   * @return the "main" method
   * @param secondParameterClass second parameter class
   */
  public static Method getMainMethodWithTwoParameters(final Class clazz, final Class secondParameterClass) {
    return getMainMethod(clazz, new Class[]{java.lang.String[].class, secondParameterClass});
  }

  /**
   * Searches for the "main" method.
   *
   * @param clazz the class where we are searching for "main" methd
   * @return the "main" method
   * @param paramTypes parameter types
   */
  protected static Method getMainMethod(final Class clazz, final Class[] paramTypes) {
    Method mainMethod = null;

    final Method[] methods = clazz.getMethods();

    for (int i = 0; i < methods.length && mainMethod == null; i++) {
      final Method method = methods[i];

      if(isCorrectMethod(methods[i], "main", paramTypes)) {
        mainMethod = method;
      }
    }

    return mainMethod;
  }

  /**
   * Checks if the method has corresponding name and parameter types.
   *
   * @param method  the method to check
   * @param methodName the method name
   * @param paramTypes the array of parameter types
   * @return true is the method corresponds the search criteria; false otherwise
   */
  private static boolean isCorrectMethod(Method method, String methodName, final Class[] paramTypes) {
    boolean isCorrect = true;

    if (method.getName().equals(methodName)) {
      final int modifiers = method.getModifiers();

      final boolean correctSignature =
              Modifier.isStatic(modifiers) &&
                      Modifier.isPublic(modifiers) &&
                      (method.getReturnType() == Void.TYPE || method.getReturnType() == Object.class);

      if (correctSignature) {
        final Class[] currentParamTypes = method.getParameterTypes();

        if (currentParamTypes.length == paramTypes.length) {
          for (int j = 0; j < currentParamTypes.length && isCorrect; j ++) {
            if (currentParamTypes[j] != paramTypes[j]) {
              isCorrect = false;
            }
          }
        }
        else {
          isCorrect = false;
        }
      }
    }
    else {
      isCorrect = false;
    }

    return isCorrect;
  }

  /**
   * Tries to launch the class.
   *
   * @param mainClass the class to be launched
   * @param args the list of arguments
   * @param illegalArgumentMessage the message to be displayed if main method cannot be found
   * @return result 
   * @throws IllegalAccessException exception
   * @throws InvocationTargetException exception
   */
  public static Object launchClass(Class mainClass, String[] args, String illegalArgumentMessage)
          throws IllegalAccessException, InvocationTargetException {
    final Method mainMethod = getMainMethod(mainClass);

    return launchClass(mainMethod, new Object[] { args }, illegalArgumentMessage);
  }

  /**
   * Tries to launch the class.
   *
   * @param mainMethod main method 
   * @param args the list of arguments
   * @param illegalArgumentMessage the message to be displayed if main method cannot be found
   * @return result
   * @throws IllegalAccessException exception
   * @throws InvocationTargetException exception
   */
  public static Object launchClass(Method mainMethod, Object[] args, String illegalArgumentMessage)
          throws IllegalAccessException, InvocationTargetException {
    if (mainMethod == null) {
      throw new IllegalArgumentException(illegalArgumentMessage);
    }

    //noinspection RedundantArrayCreation
    return mainMethod.invoke(null, args);
  }

  /**
   * Gets the private filed.
   *
   * Required, if you cannot make changes in inheritance and
   * still need to access the private field.
   *
   * @param source the source object
   * @param name the field name to access
   * @param clazz the class
   * @return the private filed
   * @throws NoSuchFieldException the exception
   * @throws IllegalAccessException the exception
   */
  public static Object getPrivateField(Object source, Class clazz, String name)
          throws NoSuchFieldException, IllegalAccessException {
    Field field = clazz.getDeclaredField(name);

    field.setAccessible(true);

    Object fieldObject = field.get(source);

    field.setAccessible(false);

    return fieldObject;
  }

  /**
   * Sets the private filed.
   *
   * Required, if you cannot make changes in inheritance and
   * still need to access the private field.
   *
   * @param source the source object
   * @param clazz the class
   * @param name the field name to access
   * @throws NoSuchFieldException the exception
   * @throws IllegalAccessException the exception
   * @param value the value
   */
  public static void setPrivateField(Object source, Class clazz, String name, Object value)
          throws NoSuchFieldException, IllegalAccessException {
    Field field = clazz.getDeclaredField(name);

    field.setAccessible(true);

    field.set(source, value);

    field.setAccessible(false);
  }

  /**
   * Invokes the private method.
   *
   * Required, if you cannot make changes in inheritance and
   * still need to call the private method.
   *
   * @param source the source object
   * @param parameters the parameters list
   * @param clazz the source class
   * @param name the method name
   * @param parameterTypes the list of parameter types
   * @return the result of invokation
   * @throws NoSuchMethodException the exception
   * @throws IllegalAccessException the exception
   * @throws InvocationTargetException the exception
   */
  public static Object invokePrivateMethod(Object source, Object[] parameters, Class clazz,
                                           String name, Class[] parameterTypes)
          throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Method method = clazz.getDeclaredMethod(name, parameterTypes);

    method.setAccessible(true);

    Object methodResult = method.invoke(source, parameters);

    method.setAccessible(false);

    return methodResult;
  }

}
