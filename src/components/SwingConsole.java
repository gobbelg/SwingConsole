/**
 *
 */
package components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import com.formdev.flatlaf.FlatLightLaf;

/**
 * Creates a console-like interface in Java Swing so that a scanner can read from the PipedReader
 * pipedReader field of the object, and a user can write to the text area using the println()
 * methods that might be used with System.out in a console application.
 *
 * @author ggobbel
 *
 */
public class SwingConsole {

  public class SwingConsoleWindow extends JFrame {

    /**
    *
    */
    private static final long serialVersionUID = -2323921291219435830L;

    private JTextField entry;

    private JLabel jLabel1;
    private JScrollPane jScrollPane1;
    private JLabel status;

    private JTextPane textArea;
    private Document textAreaDocument;
    final Color entryBackground;


    public SwingConsoleWindow() {

      initComponents();

      this.entryBackground = this.entry.getBackground();

      KeyListener keyListener = new KeyListener() {
        @Override
        public void keyPressed(KeyEvent e) {
          // Do nothing
        }

        @Override
        public void keyReleased(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            getTextField();
          }

        }

        @Override
        public void keyTyped(KeyEvent e) {
          // Do nothing
        }
      };
      this.entry.addKeyListener(keyListener);
    }

    public SwingConsoleWindow(PipedWriter pw) {
      this();
    }

    public void clear() {
      this.textArea.setText("");
    }

    /**
     * @param entryText
     */
    private void appendLine(String entryText) {
      try {
        int endPosition = this.textAreaDocument.getLength();
        this.textAreaDocument.insertString(endPosition, entryText + "\n", null);

        /*
         * Force scroll bar to end of text
         */
        endPosition = this.textAreaDocument.getLength();
        this.textArea.select(endPosition, endPosition);
      } catch (BadLocationException e) {
        System.err.println("Appending at bad location in text area\n" + e.getLocalizedMessage());
        e.printStackTrace();
      }
    }

    private void getTextField() {
      String entryText = this.entry.getText();
      this.entry.setText("");
      try {
        SwingConsole.this.pipedWriter.write(entryText + "\r\n");
      } catch (IOException e) {
        System.exit(-1);
        e.printStackTrace();
      }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {
      this.entry = new JTextField();
      this.textArea = new JTextPane();
      this.status = new JLabel();
      this.jLabel1 = new JLabel();
      this.textAreaDocument = this.textArea.getDocument();

      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      setTitle("SwingConsole");

      // this.textArea.setColumns(20);
      // this.textArea.setLineWrap(true);
      // this.textArea.setRows(5);
      // this.textArea.setWrapStyleWord(true);
      this.textArea.setEditable(false);
      this.jScrollPane1 = new JScrollPane(this.textArea);

      int fontSize = 16;
      String fontName = "Helvetica";
      this.textArea.setFont(new Font(fontName, Font.BOLD, fontSize));

      Font font = new Font(fontName, Font.BOLD, (int) (0.75 * fontSize));
      this.jLabel1.setFont(font);
      this.jLabel1.setText("User entry:");

      GroupLayout layout = new GroupLayout(getContentPane());
      getContentPane().setLayout(layout);

      // Create a parallel group for the horizontal axis
      ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

      // Create a sequential and a parallel groups
      SequentialGroup h1 = layout.createSequentialGroup();
      ParallelGroup h2 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);

      // Add a container gap to the sequential group h1
      h1.addContainerGap();

      // Add a scroll pane and a label to the parallel group h2
      h2.addComponent(this.jScrollPane1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
          450, Short.MAX_VALUE);
      h2.addComponent(this.status, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450,
          Short.MAX_VALUE);

      // Create a sequential group h3
      SequentialGroup h3 = layout.createSequentialGroup();
      h3.addComponent(this.jLabel1);
      h3.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
      h3.addComponent(this.entry, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE);

      // Add the group h3 to the group h2
      h2.addGroup(h3);
      // Add the group h2 to the group h1
      h1.addGroup(h2);

      h1.addContainerGap();

      // Add the group h1 to the hGroup
      hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);
      // Create the horizontal group
      layout.setHorizontalGroup(hGroup);


      // Create a parallel group for the vertical axis
      ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
      // Create a sequential group v1
      SequentialGroup v1 = layout.createSequentialGroup();
      // Add a container gap to the sequential group v1
      v1.addContainerGap();

      v1.addComponent(this.jScrollPane1, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE);
      v1.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);

      // Create a parallel group v2
      ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
      v2.addComponent(this.jLabel1);
      v2.addComponent(this.entry, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
          GroupLayout.PREFERRED_SIZE);

      // Add the group v2 tp the group v1
      v1.addGroup(v2);

      v1.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
      v1.addComponent(this.status);
      v1.addContainerGap();


      // Add the group v1 to the group vGroup
      vGroup.addGroup(v1);
      // Create the vertical group
      layout.setVerticalGroup(vGroup);
      pack();
    }

    void message(String msg) {
      this.status.setText(msg);
    }
  }

  private final PipedWriter pipedWriter;
  public final PipedReader pipedReader;
  private SwingConsoleWindow swingConsoleWindow;

  /**
   * Default constructor for SwingConsole class
   */
  public SwingConsole() {
    this.pipedWriter = new PipedWriter();
    this.pipedReader = new PipedReader();
    try {
      this.pipedWriter.connect(this.pipedReader);
    } catch (IOException e) {
      System.err.println("Unable to connect pipe reader to writer");
      e.printStackTrace();
      System.exit(-1);
    }
    buildSwingConsoleWindow(this.pipedWriter);
  }

  /*
   * Clear all the contents in the text area leaving a blank window
   */
  public void clear() {
    this.swingConsoleWindow.clear();
  }


  public void println() {
    this.swingConsoleWindow.appendLine("");
  }

  /*
   * Method to print directly to the text area of the swing console window
   */
  public void println(String line) {
    this.swingConsoleWindow.appendLine(line);
  }

  public void setLocation(int x, int y) {
    SwingConsole.this.swingConsoleWindow.setLocation(x, y);

  }

  public void setLocationRelativeTo(Component component) {
    SwingConsole.this.swingConsoleWindow.setLocationRelativeTo(component);

  }

  public void setSize(int length, int height) {
    SwingConsole.this.swingConsoleWindow.setSize(length, height);

  }

  public void setTitle(String title) {
    this.swingConsoleWindow.setTitle(title);
  }

  private void buildSwingConsoleWindow(PipedWriter pw) {

    /*
     * Comment this try block out and uncomment the UIManager line below it to return to
     * conventional Java Swing appearance. Also, commenting out the line below,
     * "FlatDarculaLaF.install()," will create a light but modern look and feel.
     */
    try {
      //UIManager.setLookAndFeel(new FlatLightLaf());
      FlatLightLaf.setup();
      //com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme.installLafInfo();
    } catch (Exception ex) {
      System.err.println("Failed to initialize LaF");
    }

    // Turn off metal's use of bold fonts
    // UIManager.put("swing.boldMetal", Boolean.FALSE);
    SwingConsole.this.swingConsoleWindow = new SwingConsoleWindow(pw);
    SwingConsole.this.swingConsoleWindow.setVisible(true);
  }

}
