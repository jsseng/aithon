import java.util.Scanner;
import java.io.Console;

class TestShell {
  public static void main(String[] args) {
    int loop = 1;
    System.out.println("test output");

    Scanner scanner = new Scanner(System.in);
    //Console console = System.console();
    //System.out.println(console);

    while(loop == 1) {
      String i = scanner.nextLine();
      //char[] input = console.readPassword("");
      //String i = new String(input);

      System.out.println("Received: " + i);

      if (i.equals("q")) {
          loop = 0;
      } else if (i.equals("d")) {

      }
    }
  }
}
