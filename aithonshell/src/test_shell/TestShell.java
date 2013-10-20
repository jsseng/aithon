import java.util.Scanner;

class TestShell {
  public static void main(String[] args) {
    int loop = 1;
    System.out.println("test output");

    Scanner scanner = new Scanner(System.in);

    while(loop == 1) {
      String i = scanner.nextLine();

      System.out.println("Received: " + i);

      if (i.equals("q")) {
          loop = 0;
      } else if (i.equals("d")) {

      }
    }
  }
}
