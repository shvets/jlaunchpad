package org.sf.jlaunchpad.util;

import java.util.HashMap;
import java.text.MessageFormat;

/**
 * Helper class for the format attribute.
 *
 * @since 1.7
 */
public class FormatElement {
  /**
   * The selected value in this enumeration.
   */
  protected String value;

  private static HashMap<String, MessageFormat> formatMap = new HashMap<String, MessageFormat>();
  private static final String CHECKSUM = "CHECKSUM";
  private static final String MD5SUM = "MD5SUM";
  private static final String SVF = "SVF";

  static {
    formatMap.put(CHECKSUM, new MessageFormat("{0}"));
    formatMap.put(MD5SUM, new MessageFormat("{0} *{1}"));
    formatMap.put(SVF, new MessageFormat("MD5 ({1}) = {0}"));
  }

  /**
   * Constructor for FormatElement
   */
  public FormatElement() {
    super();
  }

  /**
   * Invoked by {@link org.apache.tools.ant.IntrospectionHelper IntrospectionHelper}.
   *
   * @param value the <code>String</code> value of the attribute
   */
  public final void setValue(String value) {
  //  int idx = indexOfValue(value);
/*        if (idx == -1) {
            throw new BuildException(value + " is not a legal value for this attribute");
        }
*/
    //int index = idx;
    this.value = value;
  }

  /**
   * get the index of a value in this enumeration.
   *
   * @param value the string value to look for.
   * @return the index of the value in the array of strings
   *         or -1 if it cannot be found.
   * @see #getValues()
   */
  public final int indexOfValue(String value) {
    String[] values = getValues();
    if (values == null || value == null) {
      return -1;
    }
    for (int i = 0; i < values.length; i++) {
      if (value.equals(values[i])) {
        return i;
      }
    }
    return -1;
  }

  /**
   * @return the selected value.
   */
  public final String getValue() {
    return value;
  }

  /**
   * Get the default value - CHECKSUM.
   *
   * @return the defaul value.
   */
  public static FormatElement getDefault() {
    FormatElement e = new FormatElement();
    e.setValue(CHECKSUM);
    return e;
  }

  /**
   * Convert this enumerated type to a <code>MessageFormat</code>.
   *
   * @return a <code>MessageFormat</code> object.
   */
  public MessageFormat getFormat() {
    return formatMap.get(getValue());
  }

  /**
   * Get the valid values.
   *
   * @return an array of values.
   */
  public String[] getValues() {
    return new String[]{CHECKSUM, MD5SUM, SVF};
  }

}
