import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class NumberTest {

  /* TEST STRATEGY:

      constructor
        Try storing a negative decimal.
        Store non-negative integer.
        Store a non-negative float with a non-zero fraction.
        Store a non-negative double with a non-zero fraction

      differentiate
        Differentiate a non-negative decimal double.

      getValue
        Get the stored non-negative decimal in the double range.

      simplify
        Simplify with an empty environment.
        Simplify with a differentiation variable.

      equals
        Reflexivity.
        Symmetry.
        Transitivity.
        Consistency.
        null.
        An integer and an equal double.
        An integer and a double that are different by 0.01.

      hashCode
        Two identical Number objects.

      toString
        Convert an integer.
        Convert a double with a non zero fraction.
        Convert a double with a zero fraction.
   */


  /* See if assertions are enabled. */
  @Test(expected = AssertionError.class)
  public void testAssertionsEnabled() {
    assert false; // make sure assertions are enabled with VM argument: -ea
  }


  /* constructor -------------------------------------------------------------------------------*/


  /* Try storing a negative decimal. */
  @Test(expected = AssertionError.class)
  public void testNumber_negativeDecimal() {
    int value = -2;
    Expression number = new Number(value);
  }

  /* Store non-negative integer. */
  @Test
  public void testNumber_storeNonNegInteger() {
    int integerValue = 5;
    Expression number = new Number(integerValue);

    assertTrue(integerValue == number.getValue().getAsDouble());
  }

  /* Store a non-negative float with a non-zero fraction. */
  @Test
  public void testNumber_storeNonNegFloatWithNonZeroFraction() {
    float floatValue = 5.3f;
    Expression number = new Number(floatValue);

    assertTrue(floatValue == number.getValue().getAsDouble());
  }

  /* Store a non-negative double with a non-zero fraction. */
  @Test
  public void testNumber_storeNonNegDoubleWithNonZeroFraction() {
    double doubleValue = 5.3;
    Expression number = new Number(doubleValue);

    assertTrue(doubleValue == number.getValue().getAsDouble());
  }


  /* differentiate -----------------------------------------------------------------------------*/

  /* Differentiate a non-negative decimal double. */
  @Test
  public void testDifferentiate_nonNegDouble() {
    double value = 5.0;
    String differentiationVariable = "x";
    Expression number = new Number(value);
    Expression expected = new Number(0);
    Expression derivative = number.differentiate(differentiationVariable);

    assertTrue(expected.equals(derivative));
  }

  /* getValue -----------------------------------------------------------------------------------*/

  /* Get the stored non-negative decimal in the double range. */
  @Test
  public void testGetValue() {
    int value = 5;
    Expression number = new Number(value);

    double retrievedValue = number.getValue().getAsDouble();

    assertTrue(value == retrievedValue);

  }

  /* simplify ----------------------------------------------------------------------------------*/

  /* Simplify with an empty environment.  */
  @Test
  public void testSimplify_emptyEnvironment() {
    int value = 5;
    Expression number = new Number(value);
    Map<String, Double> environment = new HashMap<>();
    Expression simplifiedExpression = number.simplify(environment);

    assertTrue(number.equals(simplifiedExpression));
  }

  /* Simplify with a differentiation variable. */
  @Test
  public void testSimplify_withDifferentiationVariable() {
    int value = 5;
    Expression number = new Number(value);
    Map<String, Double> environment = new HashMap<>();
    environment.put("x", 3.0);

    Expression simplifiedExpression = number.simplify(environment);

    assertTrue(number.equals(simplifiedExpression));
  }

  /* toString ---------------------------------------------------------------------------------------*/

  /* Convert an integer. */
  @Test
  public void testToString_integer() {
    Expression number = new Number(5234);

    String string = number.toString();

    assertTrue("5234".equals(string));
  }

  /* Convert a double with a non zero fraction. */
  @Test
  public void testToString_doubleWithNonZeroFraction() {
    Expression number = new Number(6.3);

    String string = number.toString();

    assertTrue("6.3".equals(string));
  }

  /* Convert a double with a zero fraction. */
  @Test
  public void testToString_doubleWithZerofraction() {
    Expression number = new Number(2.0);

    String string = number.toString();

    assertTrue("2".equals(string));
  }

  /* equals -------------------------------------------------------------------------------------- */

  /* Reflexivity. */
  @Test
  public void testEquals_reflexivity() {
    Number number = new Number(3);
    assertTrue(number.equals(number));
  }

  /* Symmetry. */
  @Test
  public void testEquals_symmetry() {
    Number number1 = new Number(3);
    Number number2 = new Number(3);

    assertTrue(number1.equals(number2));
    assertTrue(number2.equals(number1));
  }

  /* Transitivity. */
  @Test
  public void testEquals_transitivity() {
    Number number1 = new Number(3);
    Number number2 = new Number(3);
    Number number3 = new Number(3);

    assertTrue(number1.equals(number2));
    assertTrue(number2.equals(number3));
    assertTrue(number1.equals(number3));
  }

  /* Consistency. */
  @Test
  public void testEquals_consistency() {
    Number number1 = new Number(3);
    Number number2 = new Number(3);

    assertTrue(number1.equals(number2));
    assertTrue(number1.equals(number2));
    assertTrue(number1.equals(number2));
    assertTrue(number1.equals(number2));
    assertTrue(number1.equals(number2));
    assertTrue(number1.equals(number2));
    assertTrue(number1.equals(number2));
    assertTrue(number1.equals(number2));
    assertTrue(number1.equals(number2));
    assertTrue(number1.equals(number2));
  }

  /* null */
  @Test
  public void testEquals_null(){
    Number number = new Number(2.0);

    assertFalse(number.equals(null));

  }


  /* An integer and an equal double. */
  @Test
  public void testEquals_integerAndDoubleEqual() {
    Number number1 = new Number(3);
    Number number2 = new Number(3.0);

    assertTrue(number1.equals(number2));
  }

  /* An integer and a double that are different by 0.01. */
  @Test
  public void testEquals_integerAndDoubleNotEqual() {
    Number number1 = new Number(3);
    Number number2 = new Number(3.01);

    assertFalse(number1.equals(number2));
  }

  /* hashCode -----------------------------------------------------------------------------------------*/

  /* Two identical Number objects. */
  @Test
  public void testHashCode() {
    Number number1 = new Number(2);
    Number number2 = new Number(2);

    assertEquals(number1.hashCode(), number2.hashCode());
  }
}
