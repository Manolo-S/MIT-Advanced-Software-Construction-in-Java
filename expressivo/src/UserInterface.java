import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import lib6005.parser.UnableToParseException;


/**
 * User interface
 * Processes user text based input and displays the result
 * Valid commands:
 * //TODO write short user manual?
 * //TODO is this an ADT? if so, write comment with abstraction function, rep invariant etc.
 */

public class UserInterface {

  private String expression;

  /**
   * Puts variable name, value pairs from a simplify command in a map.
   *
   * @param command a string that starts with "!simplify " and contains "variable name = value"
   * substrings
   * @return a map with variable name, value pairs
   */
  private Map<String, Double> makeEnvironment(String command) {
    String regex = "^!simplify(\\s+[a-zA-Z]+\\s*=\\s*\\d+(\\.\\d+)?\\s*(,)?\\s*)+\\s*$";
    assert command.matches(regex) : "Simplify command has wrong format";
    Map<String, Double> environment = new HashMap<>();
    List<String> l = Arrays.asList(command.substring(9).split(","));
    if (l.isEmpty()) {
      return environment;
    }
    for (String variable : l) {
      String variableName = variable.replaceAll("\\s*([a-zA-Z]+)\\s*=.*", "$1");
      String variableValue = variable.replaceAll("\\s*[a-zA-Z]+\\s*=\\s*", "");
      Double value = Double.parseDouble(variableValue);
      environment.put(variableName, value);
    }
    return environment;
  }

  /**
   * Processes user input.
   *
   * @param input from the text based user interface
   * @return a string with the processed user input or a message in case the input is invalid
   */
  String processInput(String input) throws IOException, UnableToParseException {
    String diffVar = "";

    if (input.equals("exit")) {
      System.exit(0);
    }

    if (input.matches("!d/d[a-zA-Z]") && !expression.equals("")) {
      diffVar = input.substring(4);
      return Commands.differentiate(expression, diffVar);
    }

    if (input.matches("!d")) {
      return "Error: Differentiation command not valid.";
    }

    if (input.matches("^!simplify(\\s+[a-zA-Z]+\\s*=\\s*\\d+(\\.\\d+)?\\s*(,)?\\s*)+\\s*$")
        && expression != "") {
      Map<String, Double> environment;
      environment = makeEnvironment(input);
      return Commands.simplify(expression, environment);
    }

    if (input.matches("!simplify\\s+[a-zA-Z]+\\s*=\\s*\\d+.*") && expression == "") {
      return "Enter an expression first.";
    }

    if (input.matches("^!simplify$")) {
      Map<String, Double> environment = new HashMap<>();
      return Commands.simplify(expression, environment);
    }

    if (input.matches("!simplify")) {
      return "Error: Simplify command not valid.";
    }

    if (input.equals("")) {
      return "";
    }

    /* process expression */
    try {
      Map<String, Double> environment = new HashMap<>();
      expression = Commands.simplify(input, environment);
      return expression;
    } catch (Exception e) {
      return "Parse error: unknown expression.";
    }
  }

  /**
   * Sends output to standard output
   *
   * @param output contains an expression or an error or informational message
   */
  void displayOutput(String output) {
    if (output.matches("(Enter.*|.*[Ee]rror.*|\\s)")) {
      System.out.println(output);
    } else {
      output = output.replaceAll("\\(([\\w])\\)", "$1");
      System.out.println(output);
    }
  }

  /**
   * Starts the user interface, processes the input and displays the result.
   */
  void run() throws IOException, UnableToParseException {
    Scanner scanner = new Scanner(System.in);
    String input = "";
    String output = "";

    while (true) {
      System.out.print("\n> ");
      input = scanner.nextLine();
      output = processInput(input);
      displayOutput(output);
    }
  }
}
