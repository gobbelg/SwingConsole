package demos;

import java.util.Scanner;
import components.SwingConsole;

public class SwingConsoleDemo {

  /**
   * @param args
   */
  public static void main(String[] args) {

    /*
     * The following two lines are all that are needed to start using the SwingConsole for input and
     * output
     */
    SwingConsole sc = new SwingConsole();
    Scanner scanner = new Scanner(sc.pipedReader);

    /*
     * SwingConsole extends JFrame, so you can set location and size with some of the methods used
     * to do so for a JFrame object
     */
    sc.setSize(800, 600);
    sc.setLocationRelativeTo(null);

    /*
     * The title of the SwingConsole can be changed from its default, 'Swing Console,' to the name
     * of the application or anything else if desired
     */
    sc.setTitle("SMARTCOM-DIACON");

    /*
     * You can add a message at the top of the console to begin
     */
    sc.println("Welcome to the SMARTCOM-DIACON System!\n");


    /*
     * Example of how the SwingConsole object can be used in an application.
     *
     * Whatever text a user enters into the JTextField at the bottom of the SwingConsole window is
     * returned to the scanner object instantiated with the SwingConsole's pipedReader field.
     */
    String input = "";
    while (!input.equalsIgnoreCase("exit")) {

      if (input.equalsIgnoreCase("clear")) {
        sc.clear();
        input = "";
        continue;
      }

      input = scanner.nextLine();
      sc.println("User entered: " + input);
    }



    /*
     * Scanner should be closed when finished using the console
     */
    scanner.close();
    System.exit(0);
  }

}


