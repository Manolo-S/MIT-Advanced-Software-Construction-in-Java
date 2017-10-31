import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for the Expression abstract data type.
 */

public class ExpressionTest {

  // Testing strategy
  //   TODO

  @Test(expected = AssertionError.class)
  public void testAssertionsEnabled() {
    assert false; // make sure assertions are enabled with VM argument: -ea
  }

  /*-------------------------------------------------------------------------------------------------*/

  //TODO: fill out
  @Test
  public void testParse() {

  }

/* make(int n)  -------------------------------------------------------------------------------------------------*/

  @Test
  public void testMake_withNegativeInteger() {
    //TODO: fill out
  }




/* make(String str)  -------------------------------------------------------------------------------------------------*/


  @Test
  public void testMake_withEmptyString() {
    //TODO: fill out

  }



  /* plus -------------------------------------------------------------------------------------------------*/



  /* times -------------------------------------------------------------------------------------------------*/


  /* toString -------------------------------------------------------------------------------------------------*/


  @Test
  public void testToString_emptyExpression() {

    //TODO: Does this make sense?
  }

  @Test
  public void testToString_singleNumber() {
    Expression expr = new Number(1);

    String string = expr.toString();

    assertTrue("(1)".equals(string));
  }

  @Test
  public void testToString_singleVariable() {
    Expression expr = new Variable("x");

    String string = expr.toString();

    assertTrue("(x)".equals(string));
  }

  @Test
  public void testToString_expressionWithPlusAndTimes() {
    Expression var = new Variable("x");
    Expression num1 = new Number(3);
    Expression num2 = new Number(5);

    Expression times = new Times(num1, var);
    Expression plus = new Plus(num2, times);

    String string = plus.toString();

    assertTrue("(5)+((3)*(x))".equals(string));
  }

  /* equals --------------------------------------------------------------------------------------------------*/

  @Test
  public void testEquals_reflexivity() {
    Expression var1 = new Variable("x");

    assertTrue(var1.equals(var1));
  }

  @Test
  public void testEquals_symmetry() {
    Expression var1 = new Variable("x");
    Expression var2 = new Variable("x");

    assertTrue(var1.equals(var2));
    assertTrue(var2.equals(var1));
  }

  @Test
  public void testEquals_transitivity() {
    Expression var1 = new Variable("x");
    Expression var2 = new Variable("x");
    Expression var3 = new Variable("x");

    assertTrue(var1.equals(var2));
    assertTrue(var2.equals(var3));
  }

  @Test
  public void testEquals_consistency(){
    Expression var1 = new Variable("x");
    Expression var2 = new Variable("x");

    assertTrue(var1.equals(var2));
    assertTrue(var1.equals(var2));
    assertTrue(var1.equals(var2));
    assertTrue(var1.equals(var2));
    assertTrue(var1.equals(var2));
    assertTrue(var1.equals(var2));
    assertTrue(var1.equals(var2));
    assertTrue(var1.equals(var2));
    assertTrue(var1.equals(var2));
    assertTrue(var1.equals(var2));
  }

  @Test
  public void testEquals_sum() {
    Expression var1 = new Variable("x");
    Expression num1 = new Variable("3");
    Expression sum1 = new Plus(var1, num1);
    Expression var2 = new Variable("x");
    Expression num2 = new Variable("3");
    Expression sum2 = new Plus(var2, num2);

    assertTrue(sum1.equals(sum2));
  }

  @Test
  public void testEquals_multiplication(){
    Expression var1 = new Variable("x");
    Expression num1 = new Variable("3");
    Expression multiplication1 = new Times(var1, num1);
    Expression var2 = new Variable("x");
    Expression num2 = new Variable("3");
    Expression multiplication2 = new Times(var2, num2);

    assertTrue(multiplication1.equals(multiplication2));
  }

  @Test
  public void testEquals_sumAndMultiplication(){
    Expression var1 = new Variable("x");
    Expression num1 = new Number(3);
    Expression num2 = new Number(5);
    Expression times1 = new Times(num1, var1);
    Expression sum1 = new Plus(num2, times1);

    Expression var2 = new Variable("x");
    Expression num3 = new Number(3);
    Expression num4 = new Number(5);
    Expression times2 = new Times(num3, var2);
    Expression sum2 = new Plus(num4, times2);

    assertTrue(sum1.equals(sum2));
  }

  /* hashCode -----------------------------------------------------------------------------------*/

  @Test
  public void testHashCode_sumAndMultiplication(){
    Expression var1 = new Variable("x");
    Expression num1 = new Number(3);
    Expression num2 = new Number(5);
    Expression times1 = new Times(num1, var1);
    Expression sum1 = new Plus(num2, times1);

    Expression var2 = new Variable("x");
    Expression num3 = new Number(3);
    Expression num4 = new Number(5);
    Expression times2 = new Times(num3, var2);
    Expression sum2 = new Plus(num4, times2);

    assertEquals(sum1.hashCode(), sum2.hashCode());
  }
}

