import java.io.IOException;
import lib6005.parser.UnableToParseException;

public class Main {

  static void p(Object s) {
    System.out.println(s);
  }

  public static void main(String[] args) throws UnableToParseException, IOException {
    UserInterface ui = new UserInterface();
    ui.run();
  }
}