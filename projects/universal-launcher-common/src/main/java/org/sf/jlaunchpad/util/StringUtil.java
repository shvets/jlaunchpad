package org.sf.jlaunchpad.util;

/**
 * This is the class for holding file utilities.
 *
 * @author Alexander Shvets
 * @version 1.0 07/14/2007
 */
public final class StringUtil {

  /**
   * Substitutes properties with actual values.
   *
   * @param text input string
   * @param startSymbol start symbol
   * @param endSymbol end symbol
   * @return substituted string
   */
  public static String substituteProperties(String text, String startSymbol, String endSymbol) {
    if(!text.contains(startSymbol)) {
      return text;
    }

    StringBuffer newText = new StringBuffer();

    String s = text;

    boolean ok = false;
    while(!ok) {
      int index1 = s.indexOf(startSymbol);

      if(index1 == -1) {
        newText.append(s);

        ok = true;
      }
      else {
        int index2 = s.substring(index1+1).indexOf(endSymbol);

        if(index2 == -1) {
          newText.append(s);

          ok = true;
        }
        else {
          index2 += (index1 + 1);

          String propertyName = s.substring(index1+1, index2);

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

}
