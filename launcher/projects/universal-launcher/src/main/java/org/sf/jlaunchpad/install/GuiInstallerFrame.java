package org.sf.jlaunchpad.install;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.*;

/**
 * This class represent frame for GUI installer.
 *
 * @author Alexander Shvets
 * @version 1.0 07/14/2007
 */
public class GuiInstallerFrame extends JFrame {
  private boolean isCancel = false;

  /**
   * Creates new GUI frame.
   */
  public GuiInstallerFrame() {
    setTitle("Installing Launcher...");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);

    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

    setLocation(
      (int) (size.getWidth() / 2 - this.getWidth() / 2),
      (int) (size.getHeight() / 2 - this.getHeight() / 2));

    getGlassPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
  }

  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      if (canExit()) {
        super.processWindowEvent(e);
      }
    } else {
      super.processWindowEvent(e);
    }
  }

  /**
   * Checks if installer can perform "exit" operation.
   *
   * @return true if installer can perform "exit" operation; false otherwise.
   */
  public boolean canExit() {
    if(isCancel) {
      return true;
    }

    int value = JOptionPane.showConfirmDialog(this,
      "Do you want to leave installation?",
      "Confirmation",
      JOptionPane.YES_NO_OPTION);

    if (value == JOptionPane.NO_OPTION) {
      return false;
    }

    if (value == JOptionPane.YES_OPTION) {
      return true;
    }

    return true;
  }

  /**
   * Performs "exit" operation.
   */
  public void exit() {
    WindowEvent evt = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);

    processWindowEvent(evt);
  }

  /**
   * Performs "cancel" operation.
   */
  public void cancel() {
    isCancel = true;

    exit();
  }

}
