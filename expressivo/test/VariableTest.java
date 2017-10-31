import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;
import org.junit.Test;

public class VariableTest {

  /* TEST STRATEGY:

      constructor
        Try storing an empty string.
        Try storing an invalid string containing a digit.
        Try storing an invalid string containing a symbol.
        Try storing an invalid string containing a white space.
        Store a valid string of length 1.
        Store a valid string of length 2.

      differentiate
        Differentiation variable not equal to variable named variable.
        Differentiation variable equal to variable named variable.

      getValue
        Get the value of the variable. Since it doesn't contain a number it's empty.

      simplify
        Simplify with an empty environment.
        Simplify with an environment containing the variable named variable.
        Simplify with an environment containing a variable which is not named variable.
        Simplify with an environment containing the variable named variable and another variable.

      equals
        Reflexivity.
        Symmetry.
        Transitivity.
        Consistency.
        null.
        Two objects x and y.
        One object x and one object X.

      hashCode
        Two identical Variable objects.

      toString
        One lowercase letter.
        One uppercase letter.
        One uppercase and one lowercase letter.
  */


  @Test(expected = AssertionError.class)
  public void testAssertionsEnabled() {
    assert false; // make sure assertions are enabled with VM argument: -ea
  }

  /* constructor ---------------------------------------------------------------------------------------*/

  /* Try storing an empty string. */
  @Test(expected = AssertionError.class)
  public void testVariable_emptyString() {
    Expression variable = new Variable("");
  }

  /* Try storing an invalid string containing a digit. */
  @Test(expected = AssertionError.class)
  public void testVariable_stringWithDigit() {
    Expression variable = new Variable("x2");
  }

  /* Try storing an invalid string containing a symbol. */
  @Test(expected = AssertionError.class)
  public void testVariable_stringWithSymbol() {
    Expression variable = new Variable("x@");
  }

  /* Try storing an invalid string containing a white space. */
  @Test(expected = AssertionError.class)
  public void testVariable_stringWithWhiteSpace() {
    Expression variable = new Variable("Fo o");
  }

  /* Store a valid string of length 1. */
  @Test
  public void testVariable_validStringLength1() {
    Expression variable = new Variable("x");
    double valueOfX = 5.0;
    Map<String, Double> environment = new HashMap<>();
    environment.put("x", valueOfX);
    double valueOfVariableSimplified = variable.simplify(environment).getValue().getAsDouble();

    assertTrue(valueOfX == valueOfVariableSimplified);
  }

  /* Store a valid string of length 2. */
  @Test
  public void testVariable_validStringLength2() {
    Expression variable = new Variable("fo");
    double valueOfFo = 5.0;
    Map<String, Double> environment = new HashMap<>();
    environment.put("fo", valueOfFo);
    double valueOfvariableSimplified = variable.simplify(environment).getValue().getAsDouble();

    assertTrue(valueOfFo == valueOfvariableSimplified);
  }



  /* differentiate  ----------------------------------------------------------------------------------------*/

  /* Differentiation variable not equal to variable named variable. */
  @Test
  public void testDifferentiate_DifferentiationVariableNotEqualToVariable() {
    Expression variable = new Variable("x");
    Expression expected = new Number(0);
    String differentiationVariable = "y";

    Expression derivative = variable.differentiate(differentiationVariable);

    assertTrue(expected.equals(derivative));
  }

  /* Differentiation variable equal to variable named variable. */
  @Test
  public void testDifferentiate_DifferentiationVariableEqualToVariable() {
    Expression variable = new Variable("x");
    Expression expected = new Number(1);
    String differentiationVariable = "x";

    Expression derivative = variable.differentiate(differentiationVariable);

    assertTrue(expected.equals(derivative));
  }


 /* getValue  ----------------------------------------------------------------------------------------*/

  @Test
  public void testGetValue() {
    Expression variable = new Variable("x");

    OptionalDouble optionalDouble = variable.getValue();

    assertFalse(optionalDouble.isPresent());
  }



  /* simplify  ----------------------------------------------------------------------------------------*/

  /* Simplify with an empty environment. */
  @Test
  public void testSimplify_emptyEnvironment() {
    Expression variable = new Variable("x");
    Map<String, Double> environment = new HashMap<>();

    Expression simplifiedExpression = variable.simplify(environment);

    assertTrue(variable.equals(simplifiedExpression));
  }

  /* Simplify with an environment containing the variable named variable. */
  @Test
  public void testSimplify_environmentContainsVariable() {
    Expression variable = new Variable("x");
    Map<String, Double> environment = new HashMap<>();
    double value = 5.0;
    environment.put("x", value);

    Expression simplifiedExpression = variable.simplify(environment);

    assertTrue(value == simplifiedExpression.getValue().getAsDouble());
  }

  /* Simplify with an environment containing a variable which is not named variable. */

  @Test
  public void testSimplify_environmentDoesntContainVariable() {
    Expression variable = new Variable("x");
    Map<String, Double> environment = new HashMap<>();
    environment.put("y", 5.0);

    Expression simplifiedExpression = variable.simplify(environment);

    assertTrue(variable.equals(simplifiedExpression));
  }

  /* Simplify with an environment containing the variable named variable and another variable. */
  public void testSimplify_environmentContainsVariableAndOtherVariable() {
    Expression variable = new Variable("x");
    Map<String, Double> environment = new HashMap<>();
    double valueOfX = 5.0;
    double valueOfY = 5.0;
    environment.put("x", valueOfX);
    environment.put("y", valueOfY);

    Expression simplifiedExpression = variable.simplify(environment);

    assertTrue(valueOfX == simplifiedExpression.getValue().getAsDouble());
  }


  /* equals - ----------------------------------------------------------------------------------------*/

  /* Reflexivity. */
  @Test
  public void testEquals_reflexivity() {
    Variable variable = new Variable("x");

    assertTrue(variable.equals(variable));

  }

  /* Symmetry. */
  @Test
  public void testEquals_symmetry() {
    Variable variable1 = new Variable("x");
    Variable variable2 = new Variable("x");

    assertTrue(variable1.equals(variable2));
    assertTrue(variable2.equals(variable1));
  }


  /* Transitivity. */
  @Test
  public void testEquals_transitivity() {
    Variable variable1 = new Variable("x");
    Variable variable2 = new Variable("x");
    Variable variable3 = new Variable("x");

    assertTrue(variable1.equals(variable2));
    assertTrue(variable2.equals(variable3));
    assertTrue(variable1.equals(variable3));
  }

  /* Consistency. */
  @Test
  public void testEquals_consistency() {
    Variable variable1 = new Variable("x");
    Variable variable2 = new Variable("x");

    assertTrue(variable1.equals(variable2));
    assertTrue(variable1.equals(variable2));
    assertTrue(variable1.equals(variable2));
    assertTrue(variable1.equals(variable2));
    assertTrue(variable1.equals(variable2));
    assertTrue(variable1.equals(variable2));
    assertTrue(variable1.equals(variable2));
    assertTrue(variable1.equals(variable2));
    assertTrue(variable1.equals(variable2));
    assertTrue(variable1.equals(variable2));
  }

  /* null */
  @Test
  public void testEquals_null(){
    Variable variable = new Variable("x");

    assertFalse(variable.equals(null));

  }



  /* Two Variables x and y. */
  @Test
  public void testEquals_xAndy() {
    Variable x = new Variable("x");
    Variable y = new Variable("y");

    assertFalse(x.equals(y));
  }

  /* One object x and one object X */
  @Test
  public void testEquals_xAndX() {
    Variable x = new Variable("x");
    Variable X = new Variable("X");

    assertFalse(x.equals(X));
  }


  /* hashCode ----------------------------------------------------------------------------------------*/

  /* Two identical Variable objects. */
  @Test
  public void testHashCode() {
    Variable x1 = new Variable("x");
    Variable x2 = new Variable("x");

    assertEquals(x1.hashCode(), x2.hashCode());
  }



  /* toString ----------------------------------------------------------------------------------------*/

  /* One lowercase letter. */
  @Test
  public void testToString_lowerCase() {
    Expression variable = new Variable("x");

    String string = variable.toString();

    assertTrue("x".equals(string));
  }

  /* One uppercase letter. */
  @Test
  public void testToString_upperCase() {
    Expression variable = new Variable("X");

    String string = variable.toString();

    assertTrue("X".equals(string));
  }

  /* One uppercase and one lowercase letter. */
  @Test
  public void testToString_upperAndLowerCase() {
    Expression variable = new Variable("Fo");

    String string = variable.toString();

    assertTrue("Fo".equals(string));
  }


}
