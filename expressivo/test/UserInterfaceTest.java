import static junit.framework.TestCase.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import lib6005.parser.UnableToParseException;
import org.junit.Test;

public class UserInterfaceTest {

  /* TEST STRATEGY:

        processInput
          exit

          "" empty string
          3
          x + 3
          x * x
          X * Y
          3 * (2 + x)

          expression: 0, command: !d/dx
          expression: 4, command: !d/dx
          expression: x, command: !d/dx
          expression: x+x, command: !d/dx
          expression: x + x, command: !d/dx check if white space are ignored between operands and operators
          expression: x + X, command: !d/dx check if X is not mistaken for x
          expression: x + 3, command: !d/dx
          expression: 3 + x, command: !d/dx
          expression: x + y, command: !d/dx
          expression: x + x + 3, command: !d/dx
          expression: x* x, command: !d/dx
          expression: x* x, command: !d/dx
          expression: x * x * x, command: !d/dx
          expression: x * 3, command: !d/dx
          expression: x * y, command: !d/dx
          expression: x * (x + 3), command: !d/dx
          expression: x + x * 3, command: !d/dx
          expression: x + 3, command: !d/dy
          expression y * 4 + x, command: !d/dy

          expression 3, command: !simplify
          expression x + x, command: !simplify
          expression 3, command: !simplify x=3
          expression 3, command: !simplify  x = 3  check if whitespace is ignored between operand and operator
          expression x * 3, command: !simplify  x=3, y=5 check if non relevant environment variables are ignored
          expression x * 3, command: !simplify y=5
          expression x + 3 * x, command: !simplify x=3  check if a result can be reduced to one number

          check that the expression remains preserved by performing multiple consecutive commands on
          the same expression  x*x: !simplify x=3, !d/dx



        displayOutput
          "" empty string
          "Enter an expression first."
          "Parse error: unknown expression."
          "x + 3"
          "x + (2 * 3)"

        makeEnvironment

          test run() with the help of http://stefanbirkner.github.io/system-rules/ or Mockito etc.?
   */



  @Test(expected = AssertionError.class)
  public void testAssertionsEnabled() {
    assert false; // make sure  assertions are enabled with VM argument: -ea
  }



  /* makeEnvironment------------------------------------------------------------------------------------- */





  /* processInput  -------------------------------------------------------------------------------------- */

  /* "" empty string */
  @Test
  public void testProcessInput_emptyString() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();

    String output = ui.processInput("");

    assertTrue(output.isEmpty());
  }

  @Test
  public void testProcessInput_1() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    String expected = "(x) + (3)";

    String output = ui.processInput("x + 3");

    assertTrue(expected.equals(output));
  }


  @Test
  public void testProcessInput_2() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    String expected = "(x) * (x)";

    String output = ui.processInput("x * x");

    assertTrue(expected.equals(output));
  }

  /*check if uppercase variables remain uppercase */
  @Test
  public void testProcessInput_3() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    String expected = "(X) * (Y)";

    String output = ui.processInput("X * Y");

    assertTrue(expected.equals(output));
  }

  @Test
  public void testProcessInput_4() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    String expected = "(3) * ((2) + (x))";

    String output = ui.processInput("3 * (2 + x)");

    assertTrue(expected.equals(output));
  }

  /* expression: 0, command: !d/dx */
  @Test
  public void testProcessInput_5() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("0");
    String expected = "0";

    String output = ui.processInput("!d/dx");

    assertTrue(expected.equals(output));
  }

  /* expression: 4, command: !d/dx */
  @Test
  public void testProcessInput_6() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("4");
    String expected = "0";

    String output = ui.processInput("!d/dx");

    assertTrue(expected.equals(output));
  }

  /* expression: x, command: !d/dx */
  @Test
  public void testProcessInput_7() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x");
    String expected = "1";

    String output = ui.processInput("!d/dx");

    assertTrue(expected.equals(output));
  }

  /* expression: x+x */
  @Test
  public void testProcessInput_8() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x+x");
    String expected = "2";

    String output = ui.processInput("!d/dx");

    assertTrue(expected.equals(output));
  }


  /* expression: x + x, command: !d/dx check if white space are ignored between operands and operators */
  @Test
  public void testProcessInput_9() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x + x");
    String expected = "2";

    String output = ui.processInput("!d/dx");

    assertTrue(expected.equals(output));
  }

  /* expression: x + X, command: !d/dx check if X is not mistaken for x */
  @Test
  public void testProcessInput_10() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x + X");
    String expected = "1";

    String output = ui.processInput("!d/dx");

    assertTrue(expected.equals(output));
  }

  /* expression: x + 3, command: !d/dx */
  @Test
  public void testProcessInput_11() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x + 3");
    String expected = "1";

    String output = ui.processInput("!d/dx");

    assertTrue(expected.equals(output));
  }

  /* expression: x + y, command: !d/dx */
  @Test
  public void testProcessInput_12() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x + y");
    String expected = "1";

    String output = ui.processInput("!d/dx");

    assertTrue(expected.equals(output));
  }

  /*  expression: x + x + 3, command: !d/dx */
  @Test
  public void testProcessInput_13() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x + x + 3");
    String expected = "2";

    String output = ui.processInput("!d/dx");

    assertTrue(expected.equals(output));
  }

  /*  expression: x* x, command: !d/dx * check if white spoce is ignored between operand and operator */
  @Test
  public void testProcessInput_14() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x* x");
    String expected = "(2) * (x)";

    String output = ui.processInput("!d/dx");

    assertTrue(expected.equals(output));
  }

  /*  expression: x *x, command: !d/dx * check if white spoce is ignored between operand and operator */
  @Test
  public void testProcessInput_15() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x *x");
    String expected = "(2) * (x)";

    String output = ui.processInput("!d/dx");

    assertTrue(expected.equals(output));
  }


  /*  expression: x * x * x, command: !d/dx  */
  @Test
  public void testProcessInput_16() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x * x * x");
    String expected = "((x) * (x)) + ((x) * ((2) * (x)))";

    String output = ui.processInput("!d/dx");

    assertTrue(expected.equals(output));
  }

  /*  expression: x * 3, command: !d/dx  */
  @Test
  public void testProcessInput_17() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x * 3");
    String expected = "3";

    String output = ui.processInput("!d/dx");

    assertTrue(expected.equals(output));
  }

  /*  expression: x * y, command: !d/dx  */
  @Test
  public void testProcessInput_18() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x * y");
    String expected = "y";

    String output = ui.processInput("!d/dx");

    assertTrue(expected.equals(output));
  }

  /*  expression: x * (x + 3), command: !d/dx  */
  @Test
  public void testProcessInput_19() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x * (x + 3)");
    String expected = "(x) + ((x) + (3))";

    String output = ui.processInput("!d/dx");

    assertTrue(expected.equals(output));
  }

  /*  expression: x + x * 3), command: !d/dx  */
  @Test
  public void testProcessInput_20() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x + x * 3");
    String expected = "4";

    String output = ui.processInput("!d/dx");

    assertTrue(expected.equals(output));
  }


  /*  expression: x + 3, command: !d/dy  */
  @Test
  public void testProcessInput_21() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x + 3");
    String expected = "0";

    String output = ui.processInput("!d/dy");

    assertTrue(expected.equals(output));
  }


  /*  expression: y * 4 + x, command: !d/dy  */
  @Test
  public void testProcessInput_22() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("y * 4 + x");
    String expected = "4";

    String output = ui.processInput("!d/dy");

    assertTrue(expected.equals(output));
  }

  /* expression 3, command: !simplify */
  @Test
  public void testProcessInput_23() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("3");
    String expected = "3";

    String output = ui.processInput("!simplify");

    assertTrue(expected.equals(output));
  }

  /* expression x + x, command: !simplify */
  @Test
  public void testProcessInput_24() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x + x");
    String expected = "(2) * (x)";

    String output = ui.processInput("!simplify");

    assertTrue(expected.equals(output));
  }

  /* expression 3, command: !simplify  x=3*/
  @Test
  public void testProcessInput_25() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("3");
    String expected = "3";

    String output = ui.processInput("!simplify x=3");

    assertTrue(expected.equals(output));
  }

  /* expression 3, command: !simplify  x = 3  check if whitespace is ignored between operand and operator */
  @Test
  public void testProcessInput_26() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("3");
    String expected = "3";

    String output = ui.processInput("!simplify x = 3");

    assertTrue(expected.equals(output));
  }


  /* expression x * 3, command: !simplify  x=3, y=5 check if non relevant environment variables are ignored*/
  @Test
  public void testProcessInput_27() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x * 3");
    String expected = "9";

    String output = ui.processInput("!simplify x=3, y=5");

    assertTrue(expected.equals(output));
  }


  /* expression x * 3, command: !simplify y=5 */
  @Test
  public void testProcessInput_28() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x * 3");
    String expected = "(x) * (3)";

    String output = ui.processInput("!simplify y=5");

    assertTrue(expected.equals(output));
  }

  /* expression x + 3 * x, command: !simplify x=3  check if a result can be reduced to one number*/
  @Test
  public void testProcessInput_29() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x + 3 * x");
    String expected = "12";

    String output = ui.processInput("!simplify x=3");

    assertTrue(expected.equals(output));
  }


  /* check that the expression remains preserved by performing multiple consecutive commands on
     the same expression  x*x: !simplify x=3, !d/dx */
  @Test
  public void testProcessInput_30() throws IOException, UnableToParseException {
    UserInterface ui = new UserInterface();
    ui.processInput("x * x");
    String expected1 = "9";
    String expected2 = "(2) * (x)";

    String output1 = ui.processInput("!simplify x=3");
    assertTrue(expected1.equals(output1));

    String output2 = ui.processInput("!d/dx");
    assertTrue(expected2.equals(output2));
  }

  /* displayOutput  -------------------------------------------------------------------------------------- */


  @Test
  public void testDisplayOutput_emptyString() throws IOException, UnableToParseException {
    PrintStream systemOut = System.out;
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    UserInterface ui = new UserInterface();
    String expected = "";
    String output = "";

    ui.displayOutput(output);

    assertTrue(expected.equals(outContent.toString().trim()));

    System.setOut(systemOut);
  }


  @Test
  public void testDisplayOutput_informationalMessage() {
    PrintStream systemOut = System.out;
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    UserInterface ui = new UserInterface();
    String expected = "Enter an expression first.";
    String output = "Enter an expression first.";

    ui.displayOutput(output);

    assertTrue(expected.equals(outContent.toString().trim()));

    System.setOut(systemOut);

  }

  @Test
  public void testDisplayOutput_errorMessage() {
    PrintStream systemOut = System.out;
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    UserInterface ui = new UserInterface();
    String expected = "Parse error: unknown expression.";
    String output = "Parse error: unknown expression.";

    ui.displayOutput(output);

    assertTrue(expected.equals(outContent.toString().trim()));

    System.setOut(systemOut);

  }


  @Test
  public void testDisplayOutput_expression_1() throws IOException, UnableToParseException {
    PrintStream systemOut = System.out;
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    UserInterface ui = new UserInterface();
    String expected = "x + 3";
    String output = "(x) + (3)";

    ui.displayOutput(output);

    assertTrue(expected.equals(outContent.toString().trim()));

    System.setOut(systemOut);
  }


  @Test
  public void testDisplayOutput_expression_2() throws IOException, UnableToParseException {
    PrintStream systemOut = System.out;
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    UserInterface ui = new UserInterface();
    String expected = "2 + (3 * x)";
    String output = "(2) + ((3) * (x))";

    ui.displayOutput(output);

    assertTrue(expected.equals(outContent.toString().trim()));

    System.setOut(systemOut);
  }


}
