import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class PlusTest {

  /* TEST STRATEGY:

      constructor
        Left is null.
        Right is null.
        Value is empty.

      differentiate
        1 + 2 with x as the differentiation variable.
        x + x with x as the differentiation variable.
        x + x with y as the differentiation variable.
        x + y with x as the differentiation variable.
        x + y with y as the differentiation variable.
        x + 1 with x as the differentiation variable.
        1 + x with x as the differentiation variable.
        x + 1 with y as the differentiation variable.
        1 + x with y as the differentiation variable.
        x + 0 with x as the differentiation variable.
        0 + x with x as the differentiation variable.
        //TODO tests that combine + and * to test precedence


      getValue
        Check if the OptionalDouble the field value points at is empty.

      simplify
        Plus(Number(0), Number), environment is empty.
        Plus(Variable x, Number(0)), environment is x.
        Plus(Variable x, Number(0)), environment is y.
        Plus(Number, Number), environment x.
        Plus(Number, Variable x), environment x.
        Plus(Number, Variable x), environment y.
        Plus(Variable y, Number x), environment x.
        Plus(Variable y, Number x), environment y.
        Plus(Variable x, Variable y), environment x, y.
        Plus(Plus(Number, Number), Variable x), environment x.
        Plus(Plus(Number, Variable x), Variable x), environment x.
        Plus(Times(Number, Number), Variable x), environment x.

      equals
        Reflexivity.
        Symmetry.
        Transitivity.
        Consistency.
        null.
        Check if it works recursively on two objects Plus(Plus,Number).


      hashCode
        Two identical Plus objects.

      toString
        Number plus Variable.
        Check if toString works recursively.

   */


  @Test(expected = AssertionError.class)
  public void testAssertionsEnabled() {
    assert false; // make sure assertions are enabled with VM argument: -ea
  }

  /* Constructor -------------------------------------------------------------------------------------- */

  /* Left is null.  */
  @Test(expected = AssertionError.class)
  public void testConstructor_leftIsNull() {
    Expression left = null;
    Expression right = new Number(5);

    Expression plus = new Plus(left, right);
  }

  /* Right is null.  */
  @Test(expected = AssertionError.class)
  public void testConstructor_rightIsNull() {
    Expression left = new Number(5);
    Expression right = null;

    Expression plus = new Plus(left, right);
  }

  /* Value is empty  */
  @Test
  public void testConstructor_valueIsEmpty() {
    Expression left = new Number(3);
    Expression right = new Number(3);

    Expression plus = new Plus(left, right);

    assertFalse(plus.getValue().isPresent());
  }


  /* differentiate  ------------------------------------------------------------------------------------------ */


  /* 1 + 2 with x as the differentiation variable. */
  @Test
  public void testDifferentiate_1_plus_2_diffVar_x() {
    String diffVar = "x";
    Expression left = new Number(1);
    Expression right = new Number(2);
    Expression sum = new Plus(left, right);
    Expression expected = new Number(0);

    Expression derivative = sum.differentiate(diffVar);

    assertTrue(expected.equals(derivative));
  }

  /* x + x with x as the differentiation variable. */
  @Test
  public void testDifferentiate_x_plus_x_diffVar_x() {
    String diffVar = "x";
    Expression left = new Variable("x");
    Expression right = new Variable("x");
    Expression sum = new Plus(left, right);
    Expression expected = new Number(2);

    Expression derivative = sum.differentiate(diffVar);

    assertTrue(expected.equals(derivative));
  }


  /* x + x with y as the differentiation variable. */
  @Test
  public void testDifferentiate_x_plus_x_diffVar_y() {
    String diffVar = "y";
    Expression left = new Variable("x");
    Expression right = new Variable("x");
    Expression sum = new Plus(left, right);
    Expression expected = new Number(0);

    Expression derivative = sum.differentiate(diffVar);

    assertTrue(expected.equals(derivative));

  }

  /*  x + y with x as the differentiation variable. */
  @Test
  public void testDifferentiate_x_plus_y_diffVar_x() {
    String diffVar = "x";
    Expression left = new Variable("x");
    Expression right = new Variable("y");
    Expression sum = new Plus(left, right);
    Expression expected = new Number(1);

    Expression derivative = sum.differentiate(diffVar);

    assertTrue(expected.equals(derivative));
  }

  /* x + 1 with x as the differentiation variable. */
  @Test
  public void testDifferentiate_x_plus_1_diffVar_x() {
    String diffVar = "x";
    Expression left = new Variable("x");
    Expression right = new Number(1);
    Expression sum = new Plus(left, right);
    Expression expected = new Number(1);

    Expression derivative = sum.differentiate(diffVar);

    assertTrue(expected.equals(derivative));
  }

  /* 1 + x with x as the differentiation variable. */
  @Test
  public void testDifferentiate_1_plus_x_diffVar_x() {
    String diffVar = "x";
    Expression left = new Number(1);
    Expression right = new Variable("x");
    Expression sum = new Plus(left, right);
    Expression expected = new Number(1);

    Expression derivative = sum.differentiate(diffVar);

    assertTrue(expected.equals(derivative));
  }

  /* x + 1 with y as the differentiation variable. */
  @Test
  public void testDifferentiate_x_plus_1_diffVar_y() {
    String diffVar = "y";
    Expression left = new Variable("x");
    Expression right = new Number(1);
    Expression sum = new Plus(left, right);
    Expression expected = new Number(0);

    Expression derivative = sum.differentiate(diffVar);

    assertTrue(expected.equals(derivative));
  }

  /* 1 + x with y as the differentiation variable. */
  @Test
  public void testDifferentiate_1_plus_x_diffVar_y() {
    String diffVar = "y";
    Expression left = new Number(1);
    Expression right = new Variable("x");
    Expression sum = new Plus(left, right);
    Expression expected = new Number(0);

    Expression derivative = sum.differentiate(diffVar);

    assertTrue(expected.equals(derivative));
  }

  /* x + 0 with x as the differentiation variable. */
  @Test
  public void testDifferentiate_x_plus_0_diffVar_x() {
    String diffVar = "x";
    Expression left = new Variable("x");
    Expression right = new Number(0);
    Expression sum = new Plus(left, right);
    Expression expected = new Number(1);

    Expression derivative = sum.differentiate(diffVar);

    assertTrue(expected.equals(derivative));
  }

  /* 0 + x with x as the differentiation variable. */
  @Test
  public void testDifferentiate_0_plus_x_diffVar_x() {
    String diffVar = "x";
    Expression left = new Number(0);
    Expression right = new Variable("x");
    Expression sum = new Plus(left, right);
    Expression expected = new Number(1);

    Expression derivative = sum.differentiate(diffVar);

    assertTrue(expected.equals(derivative));
  }

  /* getValue  ------------------------------------------------------------------------------------------ */

  /* Check if the OptionalDouble the field value points at is empty. */
  @Test
  public void testGetValue() {
    Expression left = new Number(5);
    Expression right = new Number(5);

    Expression plus = new Plus(left, right);

    assertFalse(plus.getValue().isPresent());
  }


  /* simplify  ------------------------------------------------------------------------------------------ */

  /* Plus(Number(0), Number(5), environment is empty. */
  @Test
  public void testSimplify_1() {
    Map<String, Double> environment = new HashMap<>();
    Expression left = new Number(0);
    Expression right = new Number(5);
    Expression plus = new Plus(left, right);
    Expression expected = new Number(5.0);

    Expression result = plus.simplify(environment);

    assertTrue(expected.equals(result));
  }

  /* Plus(Variable x, Number(0)), environment is x. */
  @Test
  public void testSimplify_2() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("x", 2.0);
    Expression left = new Variable("x");
    Expression right = new Number(0);
    Expression plus = new Plus(left, right);
    Expression expected = new Number(2.0);

    Expression result = plus.simplify(environment);

    assertTrue(expected.equals(result));
  }

  /* Plus(Variable x, Number(0)), environment is y. */
  @Test
  public void testSimplify_3() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("y", 3.0);
    Expression left = new Variable("x");
    Expression right = new Number(0);
    Expression plus = new Plus(left, right);
    Expression expected = new Variable("x");

    Expression result = plus.simplify(environment);

    assertTrue(expected.equals(result));
  }

  /* Plus(Number, Number), environment x. */
  @Test
  public void testSimplify_4() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("x", 3.0);
    Expression left = new Number(4.0);
    Expression right = new Number(6.0);
    Expression plus = new Plus(left, right);
    Expression expected = new Number(10.0);

    Expression result = plus.simplify(environment);

    assertTrue(expected.equals(result));
  }

  /* Plus(Number, Variable x), environment x. */
  @Test
  public void testSimplify_5() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("x", 3.0);
    Expression left = new Number(3.0);
    Expression right = new Variable("x");
    Expression plus = new Plus(left, right);
    Expression expected = new Number(6.0);

    Expression result = plus.simplify(environment);

    assertTrue(expected.equals(result));
  }

  /*  Plus(Number, Variable x), environment y. */
  @Test
  public void testSimplify_6() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("y", 3.0);
    Expression left = new Number(3.0);
    Expression right = new Variable("x");
    Expression plus = new Plus(left, right);
    Expression expected = new Plus(left, right);

    Expression result = plus.simplify(environment);

    assertTrue(expected.equals(result));
  }

  /* Plus(Variable y, Number x), environment x. */
  @Test
  public void testSimplify_7() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("x", 3.0);
    Expression left = new Variable("y");
    Expression right = new Number(5.0);
    Expression plus = new Plus(left, right);
    Expression expected = new Plus(left, right);

    Expression result = plus.simplify(environment);

    assertTrue(expected.equals(result));
  }

  /* Plus(Variable y, Number x), environment y. */
  @Test
  public void testSimplify_8() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("y", 3.0);
    Expression left = new Variable("y");
    Expression right = new Number(5.0);
    Expression plus = new Plus(left, right);
    Expression expected = new Number(8.0);

    Expression result = plus.simplify(environment);

    assertTrue(expected.equals(result));
  }

  /* Plus(Variable x, Variable y), environment x, y. */
  @Test
  public void testSimplify_9() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("x", 3.0);
    environment.put("y", 5.0);
    Expression left = new Variable("x");
    Expression right = new Variable("y");
    Expression plus = new Plus(left, right);
    Expression expected = new Number(8.0);

    Expression result = plus.simplify(environment);

    assertTrue(expected.equals(result));
  }


  /* Plus(Plus(Number, Number), Variable x), environment x. */
  @Test
  public void testSimplify_10() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("x", 3.0);
    Expression left = new Plus(new Number(4.0), new Number(6.0));
    Expression right = new Variable("x");
    Expression plus = new Plus(left, right);
    Expression expected = new Number(13.0);

    Expression result = plus.simplify(environment);

    assertTrue(expected.equals(result));
  }


  /* Plus(Plus(Number, Variable x), Variable x), environment x. */
  @Test
  public void testSimplify_11() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("x", 3.0);
    Expression left = new Plus(new Number(5.0), new Variable("x"));
    Expression right = new Variable("x");
    Expression plus = new Plus(left, right);
    Expression expected = new Number(11.0);

    Expression result = plus.simplify(environment);

    assertTrue(expected.equals(result));
  }

  /* Plus(Times(Number, Number), Variable x), environment x. */
  @Test
  public void testSimplify_12() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("x", 3.0);
    Expression left = new Times(new Number(5.0), new Number(6.0));
    Expression right = new Variable("x");
    Expression plus = new Plus(left, right);
    Expression expected = new Number(33.0);

    Expression result = plus.simplify(environment);

    assertTrue(expected.equals(result));
  }


  /* equals -------------------------------------------------------------------------------------------- */
   /* Reflexivity. */
  @Test
  public void testEquals_reflexivity() {
    Expression left = new Number(5.0);
    Expression right = new Variable("x");
    Expression plus = new Plus(left, right);

    assertTrue(plus.equals(plus));
  }

  /* Symmetry. */
  @Test
  public void testEquals_symmetry() {
    Expression left = new Number(5.0);
    Expression right = new Variable("x");
    Expression plus1 = new Plus(left, right);
    Expression plus2 = new Plus(left, right);

    assertTrue(plus1.equals(plus2));
    assertTrue(plus2.equals(plus1));
  }


  /* Transitivity. */
  @Test
  public void testEquals_transitivity() {
    Expression left = new Number(5.0);
    Expression right = new Variable("x");
    Expression plus1 = new Plus(left, right);
    Expression plus2 = new Plus(left, right);
    Expression plus3 = new Plus(left, right);

    assertTrue(plus1.equals(plus2));
    assertTrue(plus2.equals(plus3));
    assertTrue(plus1.equals(plus3));
  }

  /* Consistency. */
  @Test
  public void testEquals_consistency() {
    Expression left = new Number(5.0);
    Expression right = new Variable("x");
    Expression plus1 = new Plus(left, right);
    Expression plus2 = new Plus(left, right);

    assertTrue(plus1.equals(plus2));
    assertTrue(plus1.equals(plus2));
    assertTrue(plus1.equals(plus2));
    assertTrue(plus1.equals(plus2));
    assertTrue(plus1.equals(plus2));
    assertTrue(plus1.equals(plus2));
    assertTrue(plus1.equals(plus2));
    assertTrue(plus1.equals(plus2));
    assertTrue(plus1.equals(plus2));
    assertTrue(plus1.equals(plus2));
  }

  /* null. */
  @Test
  public void testEquals_null() {
    Expression left = new Number(5.0);
    Expression right = new Variable("x");
    Expression plus = new Plus(left, right);

    assertFalse(plus.equals(null));
  }

  /* Check if it works recursively on two equal objects Plus(Plus,Number) */
  @Test
  public void testEquals_recursion() {
    Expression left = new Variable("x");
    Expression right = new Number(3.0);
    Expression plus1 = new Plus(new Plus(left, right), right);
    Expression plus2 = new Plus(new Plus(left, right), right);

    assertTrue(plus1.equals(plus2));
  }


  /* hashCode ------------------------------------------------------------------------------------------ */
  @Test
  public void testHashCode() {
    Expression left = new Number(2.0);
    Expression right = new Variable("y");
    Expression plus1 = new Plus(left, right);
    Expression plus2 = new Plus(left, right);

    assertTrue(plus1.hashCode() == plus2.hashCode());
  }


  /* toString ------------------------------------------------------------------------------------------ */

  /* Number plus Variable. */
  @Test
  public void testToString_numberPlusVariable() {
    Expression sum = new Plus(new Number(5), new Variable("x"));
    String expected = "(5) + (x)";

    String result = sum.toString();

    assertTrue(expected.equals(result));
  }

  /* Check if toString works recursively. */
  @Test
  public void testToString_recursively() {
    Expression plus1 = new Plus(new Number(5), new Variable("x"));
    Expression plus2 = new Plus(plus1, new Variable("y"));
    String expected = "((5) + (x)) + (y)";

    String result = plus2.toString();

    assertTrue(expected.equals(result));
  }

}


