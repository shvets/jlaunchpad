package org.sf.jlaunchpad.install;

import javax.swing.*;
import java.io.FilterOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.awt.*;

/**
 * This class represents console control.
 *
 * @author Alexander Shvets
 * @version 1.0 01/19/2008
 */
public class Console {
  private JTextArea consoleArea = new JTextArea();

  private final FilterOutputStream filterOutputStream = new FilterOutputStream(new ByteArrayOutputStream()) {
    public void write(byte b[], int off, int len) throws IOException
    {
      super.write(b, off, len);

      consoleArea.append(new String(b, off, len));
      consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
    }
  };

  public Console() {
    System.setOut(new PrintStream(filterOutputStream));
    System.setErr(new PrintStream(filterOutputStream));

    consoleArea.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    Font currentFont = consoleArea.getFont();
    consoleArea.setFont(new Font(currentFont.getName(), Font.BOLD, currentFont.getSize()));
    consoleArea.setEditable(false);

    JPanel panel11 = new JPanel();
    panel11.setLayout(new BoxLayout(panel11, BoxLayout.X_AXIS));

    consoleArea.setRows(25);
    consoleArea.setColumns(80);
  }

  public JComponent getComponent() {
    return consoleArea;
  }

  public void clear() {
    consoleArea.setText("");
  }

}
