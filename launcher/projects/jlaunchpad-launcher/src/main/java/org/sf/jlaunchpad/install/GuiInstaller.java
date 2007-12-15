package org.sf.jlaunchpad.install;

import org.sf.jlaunchpad.core.LauncherException;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * The class perform initial (gui) installation of jlaunchpad launcher.
 *
 * @author Alexander Shvets
 * @version 1.0 01/14/2007
 */
public class GuiInstaller extends CoreInstaller
       implements CaretListener, ActionListener {
  private JTextField javaHomeField = new JTextField(35);
  private JTextField launcherHomeField = new JTextField(35);
  private JTextField repositoryHomeField = new JTextField(35);
//  private JComboBox javaSpecVersionComboBox = new JComboBox(new String[] { "1.5", "1.6", "1.7"});

  private JCheckBox useProxyCheckbox = new JCheckBox();
  private JTextField proxyHostField = new JTextField(35);
  private JTextField proxyPortField = new JTextField(6);

  private JCheckBox proxyAuthCheckbox = new JCheckBox();
  private JTextField proxyUserField = new JTextField(15);
  private JPasswordField proxyPasswordField = new JPasswordField(15);

  private JButton installButton = new JButton("Install");
  private JTextArea console = new JTextArea();

  private JTabbedPane tabbedPane = new JTabbedPane();

  private final FilterOutputStream filterOutputStream = new FilterOutputStream(new ByteArrayOutputStream()) {
    public void write(byte b[], int off, int len) throws IOException {
      super.write(b, off, len);

      console.append(new String(b, off, len));
      console.setCaretPosition(console.getDocument().getLength());
    }
  };

  private GuiInstallerFrame frame = new GuiInstallerFrame();

  private String[] args;

  /**
   * Creates new GUI installer.
   * @param args command line arguments
   * @throws LauncherException the exception
   */
  public GuiInstaller(String[] args) throws LauncherException {
    this.args = args;

    System.setOut(new PrintStream(filterOutputStream));
    System.setErr(new PrintStream(filterOutputStream));

    try {
      load();
    }
    catch (IOException e) {
      throw new LauncherException(e);
    }

    console.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    Font currentFont = console.getFont();
    console.setFont(new Font(currentFont.getName(), Font.BOLD, currentFont.getSize()));

    javaHomeField.addCaretListener(this);
    launcherHomeField.addCaretListener(this);
    repositoryHomeField.addCaretListener(this);

    useProxyCheckbox.addActionListener(this);
    proxyHostField.addCaretListener(this);
    proxyPortField.addCaretListener(this);

    proxyAuthCheckbox.addActionListener(this);
    proxyUserField.addCaretListener(this);
    proxyPasswordField.addCaretListener(this);

    frame.getContentPane().add(createContent(), BorderLayout.CENTER);

    tryEnableInstallButton();
    
    frame.setVisible(true);
  }

  public void caretUpdate(CaretEvent e) {
    tryEnableInstallButton();
  }

  public void actionPerformed(ActionEvent e) {
    tryEnableInstallButton();
  }

  private void tryEnableInstallButton() {
    String javaHome = javaHomeField.getText().trim();
    String launcherHome = launcherHomeField.getText().trim();
    String repositoryHome = repositoryHomeField.getText().trim();

    boolean enabled = false;

    if (javaHome != null && javaHome.length() > 0 && new File(javaHome).exists()) {
      if (launcherHome != null && launcherHome.length() > 0) {
        if (repositoryHome != null && repositoryHome.length() > 0) {
          if (!useProxyCheckbox.isSelected()) {
            enabled = true;
          }
          else {
            String proxyHost = proxyHostField.getText().trim();
            String proxyPort = proxyPortField.getText().trim();

            if (proxyHost != null && proxyHost.length() > 0) {
              if (proxyPort != null && proxyPort.length() > 0) {
                try {
                  Integer.parseInt(proxyPort);

                  if (!proxyAuthCheckbox.isSelected()) {
                    enabled = true;
                  }
                  else {
                    String proxyUser = proxyUserField.getText().trim();
                    char[] proxyPassword = proxyPasswordField.getPassword();

                    if (proxyUser != null && proxyUser.length() > 0) {
                      if (proxyPassword != null && proxyPassword.length > 0) {
                        enabled = true;
                      }
                    }

                  }
                }
                catch (NumberFormatException e) {
                  //noinspection UnnecessarySemicolon
                  ;
                }
              }
            }
          }
        }
      }
    }

    if (useProxyCheckbox.isSelected()) {
      proxyHostField.setEnabled(true);
      proxyPortField.setEnabled(true);
      proxyAuthCheckbox.setEnabled(true);

      if (proxyAuthCheckbox.isSelected()) {
        proxyUserField.setEnabled(true);
        proxyPasswordField.setEnabled(true);
      }
      else {
        proxyUserField.setEnabled(false);
        proxyPasswordField.setEnabled(false);
      }
    }
    else {
      proxyHostField.setEnabled(false);
      proxyPortField.setEnabled(false);
      proxyAuthCheckbox.setEnabled(false);
      proxyUserField.setEnabled(false);
      proxyPasswordField.setEnabled(false);
    }

    installButton.setEnabled(enabled);

    //tabbedPane.setEnabledAt(1, enabled);
  }

  public JPanel createContent() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    JComponent panel1 = makeBasicSettingsPanel();
    tabbedPane.addTab("Launcher Settings", null, panel1, "Specifies important locations.");
    tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

    JComponent panel2 = makeProxySettingsPanel();
    tabbedPane.addTab("Proxy Settings", null, panel2, "Proxy Settings");
    tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

    //The following line enables to use scrolling tabs.
    tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(tabbedPane);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(makeInstallPanel());
    panel.add(Box.createRigidArea(new Dimension(0, 10)));

    return panel;
  }

  protected JComponent makeBasicSettingsPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    //JLabel javaSpecVersionLabel = new JLabel("Java Specification Version:");

    JLabel javaHomeLabel = new JLabel("Java Home:              ");

    JButton javaHomeSearchButton = new JButton("Search...");

    javaHomeSearchButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(javaHomeField.getText().trim()));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = fileChooser.showDialog(frame, "Select Java Home...");

        if (returnVal == JFileChooser.APPROVE_OPTION) {
          javaHomeField.setText(fileChooser.getSelectedFile().getPath());
        }
      }
    });

    JLabel launcherHomeLabel = new JLabel("Launcher Home:     ");

    JButton launcherHomeSearchButton = new JButton("Search...");

    launcherHomeSearchButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(launcherHomeField.getText().trim()));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = fileChooser.showDialog(frame, "Select Launcher Home...");

        if (returnVal == JFileChooser.APPROVE_OPTION) {
          launcherHomeField.setText(fileChooser.getSelectedFile().getPath());
        }
      }
    });

    JLabel repositoryHomeLabel = new JLabel("Repository Home:    ");

    JButton repositoryHomeSearchButton = new JButton("Search...");

    repositoryHomeSearchButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(repositoryHomeField.getText().trim()));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = fileChooser.showDialog(frame, "Select Repository Home...");

        if (returnVal == JFileChooser.APPROVE_OPTION) {
          repositoryHomeField.setText(fileChooser.getSelectedFile().getPath());
        }
      }
    });

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.weightx = 0.5;

/*    JPanel panel10 = new JPanel();

    constraints.gridy = 0;

    panel10.setLayout(new GridBagLayout());

    constraints.gridx = 0; panel10.add(javaSpecVersionLabel, constraints);
    constraints.gridx = 1; 
    panel10.add(javaSpecVersionComboBox, constraints);
*/
    JPanel panel11 = new JPanel();

    constraints.gridy = 0;

    panel11.setLayout(new GridBagLayout());

    constraints.gridx = 0; panel11.add(javaHomeLabel, constraints);
    constraints.gridx = 1; panel11.add(javaHomeField, constraints);
    constraints.gridx = 2; panel11.add(javaHomeSearchButton, constraints);

//    JPanel panel12 = new JPanel();

//    constraints.gridy = 1;

//    panel12.setLayout(new GridBagLayout());

    constraints.gridy = 1;

    JPanel panel12 = new JPanel();

    panel12.setLayout(new GridBagLayout());

    constraints.gridx = 0; panel12.add(launcherHomeLabel, constraints);
    constraints.gridx = 1; panel12.add(launcherHomeField, constraints);
    constraints.gridx = 2; panel12.add(launcherHomeSearchButton, constraints);

    constraints.gridy = 2;

    JPanel panel13 = new JPanel();

    panel13.setLayout(new GridBagLayout());

    constraints.gridx = 0; panel13.add(repositoryHomeLabel, constraints);
    constraints.gridx = 1; panel13.add(repositoryHomeField, constraints);
    constraints.gridx = 2; panel13.add(repositoryHomeSearchButton, constraints);

//    constraints.gridy = 3;

//    JPanel panel14 = new JPanel();

//    panel15.setLayout(new GridBagLayout());

    //constraints.gridx = 0; panel15.add(rubyHomeLabel, constraints);
    //constraints.gridx = 1; panel15.add(rubyHomeField, constraints);
    //constraints.gridx = 2; panel15.add(rubyHomeSearchButton, constraints);

//    panel.add(panel10);
    panel.add(panel11);
    panel.add(panel12);
    panel.add(panel13);
//    panel.add(panel14);
//    panel.add(panel15);

    return panel;
  }

  protected JComponent makeProxySettingsPanel() {
    JPanel panel = new JPanel();

//    SpringLayout layout = new SpringLayout();

    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    JPanel panel1 = new JPanel();
    panel1.setLayout(new FlowLayout());

    JLabel label1 = new JLabel("Use Proxy:");
    JLabel label2 = new JLabel("Host:");
    JLabel label3 = new JLabel("Port:");

    panel1.add(label1);
    panel1.add(useProxyCheckbox);
    panel1.add(label2);
    panel1.add(proxyHostField);
    panel1.add(label3);
    panel1.add(proxyPortField);

    JPanel panel2 = new JPanel();
    panel1.setLayout(new FlowLayout());

    JLabel label4 = new JLabel("Proxy Authentication:");
    JLabel label5 = new JLabel("User:");
    JLabel label6 = new JLabel("Password:");

    panel2.add(label4);
    panel2.add(proxyAuthCheckbox);
    panel2.add(label5);
    panel2.add(proxyUserField);
    panel2.add(label6);
    panel2.add(proxyPasswordField);

    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(panel1);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(panel2);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));

    return panel;
  }

  protected JComponent makeInstallPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    installButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        Thread thread = new Thread() {
          public void run() {
            Component glassPane = frame.getGlassPane();

            glassPane.setVisible(true);

            glassPane.addMouseListener(new MouseAdapter() {
              @SuppressWarnings({"UnnecessarySemicolon"})
              public void mousePressed(MouseEvent e) {
                ; // supress
              }
            });

            try {
              GuiInstaller.this.install(args);
            }
            catch (Exception e) {
              e.printStackTrace();
            }
            finally {
              glassPane.setVisible(false);
            }
          }
        };

        thread.start();
      }
    });

    JButton closeButton = new JButton("Close");

    closeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        frame.cancel();
      }
    });

    JPanel panel11 = new JPanel();
    panel11.setLayout(new BoxLayout(panel11, BoxLayout.X_AXIS));

    panel11.add(Box.createRigidArea(new Dimension(200, 0)));
    panel11.add(installButton);
    panel11.add(Box.createRigidArea(new Dimension(50, 0)));
    panel11.add(closeButton);
    panel11.add(Box.createRigidArea(new Dimension(200, 0)));

    JPanel panel12 = new JPanel();
    panel12.setLayout(new BoxLayout(panel12, BoxLayout.X_AXIS));

    console.setRows(10);
    console.setColumns(300);
    panel12.add(new JScrollPane(console));

    panel.add(panel11);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(panel12);

    return panel;
  }

  public void install(final String[] args) throws LauncherException {
    try {
      updateProperties();

      try {
        save();
      }
      catch (IOException e) {
        throw new LauncherException(e);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    GuiInstaller.super.install(args);
  }

  private void updateProperties() {
    System.setProperty("java.home.internal", javaHomeField.getText().trim());

    if (useProxyCheckbox.isSelected()) {
      System.setProperty("proxyHost", proxyHostField.getText().trim());
      System.setProperty("proxyPort", proxyPortField.getText().trim());

      if (proxyAuthCheckbox.isSelected()) {
        System.setProperty("proxyUser", proxyUserField.getText().trim());
        System.setProperty("proxyPassword", new String(proxyPasswordField.getPassword()).trim());
      }
    }
    else {
/*      System.setProperty("proxyHost", "");
      System.setProperty("proxyPort", "");
      System.setProperty("proxyUser", "");
      System.setProperty("proxyPassword", "");
      */
    }

    System.setProperty("launcher.home", launcherHomeField.getText().trim());
    System.setProperty("repository.home", repositoryHomeField.getText().trim());
  }


  @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection"})
  protected void load() throws IOException {
    super.load();
    updateProperty(javaHomeField, "java.home.internal");
    updateProperty(launcherHomeField, "launcher.home");
    updateProperty(repositoryHomeField, "repository.home");

    updateProperty(useProxyCheckbox, "proxySet");
    updateProperty(proxyHostField, "proxyHost");
    updateProperty(proxyPortField, "proxyPort");
    updateProperty(proxyAuthCheckbox, "proxyAuth");
    updateProperty(proxyUserField, "proxyUser");
    updateProperty(proxyPasswordField, "proxyPassword");
  }

  @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection"})
  protected void save() throws IOException {
    launcherProps.load();

    saveProperty(javaHomeField, "java.home.internal");

    saveProperty(launcherHomeField, "launcher.home");
    saveProperty(repositoryHomeField, "repository.home");

    if(useProxyCheckbox.isSelected()) {
      saveProperty(useProxyCheckbox, "proxySet");
    }
    saveProperty(proxyHostField, "proxyHost");
    saveProperty(proxyPortField, "proxyPort");

    if(useProxyCheckbox.isSelected()) {
      saveProperty(proxyAuthCheckbox, "proxyAuth");
    }
    
    saveProperty(proxyUserField, "proxyUser");
    saveProperty(proxyPasswordField, "proxyPassword");

//    launcherProps.put("launcher.version", System.getProperty("launcher.version"));

    super.save();
  }

  /**
   * Updates GUI component from the property.
   *
   * @param component GUI component
   * @param property the property to be propagated
   */
  public void updateProperty(JComponent component, String property) {
    if(component instanceof JTextField) {
      JTextField textField = (JTextField)component;

      String value = (String)launcherProps.get(property);

      if(value != null) {
        textField.setText(value.replace('/', File.separatorChar));
      }
    }
    else if(component instanceof JCheckBox) {
      JCheckBox checkBox = (JCheckBox)component;

      String value = (String)launcherProps.get(property);

      if(value != null) {
        checkBox.setSelected(Boolean.parseBoolean(value));
      }
    }
    else if(component instanceof JComboBox) {
      JComboBox comboBox = (JComboBox)component;

      String value = (String)launcherProps.get(property);

      if(value != null) {
        comboBox.setSelectedItem(value);
      }
    }
  }

  /**
   * Updates the property from GUI component.
   *
   * @param component GUI component
   * @param property the property to be updated
   */
  public void saveProperty(JComponent component, String property) {
    if(component instanceof JTextField) {
      JTextField textField = (JTextField)component;

      String value = textField.getText().trim();

        if(value != null && value.trim().length() > 0) {
          launcherProps.put(property, value.replace(File.separatorChar, '/'));
        }
    }
    else if(component instanceof JCheckBox) {
      JCheckBox checkBox = (JCheckBox)component;

      launcherProps.put(property, String.valueOf(checkBox.isSelected()));
    }
    else if(component instanceof JComboBox) {
      JComboBox comboBox = (JComboBox)component;

      launcherProps.put(property, comboBox.getSelectedItem());
    }
  }

  /**
   * Launches the GUI installer.
   *
   * @param args The application command-line arguments.
   * @throws LauncherException exception
   */
  public static void main(String[] args) throws LauncherException {
    new GuiInstaller(args);
  }

}

