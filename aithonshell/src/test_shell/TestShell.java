import java.util.Scanner;

class TestShell {
  public static void main(String[] args) {
    int loop = 1;
    System.out.println("test output");

    Scanner scanner = new Scanner(System.in);

    while(loop == 1) {
      String i = scanner.next();

      switch (i) {
        case "q":
          loop = 0;
          break;
        case "d":
          break;
      }
    }
  }
}
