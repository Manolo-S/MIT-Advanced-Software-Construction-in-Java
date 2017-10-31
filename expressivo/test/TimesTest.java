import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class TimesTest {

  /* TEST STRATEGY:

        constructor
          Left is null.
          Right is null.
          Check if value is empty.

        differentiate
          1 * 2 with x as the differentiation variable.
          x * x with x as the differentiation variable.
          x * x with y as the differentiation variable.
          x * y with x as the differentiation variable.
          x * y with y as the differentiation variable.
          x * 1 with x as the differentiation variable.
          1 * x with x as the differentiation variable.
          x * 1 with y as the differentiation variable.
          1 * x with y as the differentiation variable.
          x * 0 with x as the differentiation variable.
          0 * x with x as the differentiation variable.
          //TODO tests that combine + and * to test precedence

        getValue
          Check if field value (OptionalDouble) is empty.

        simplify
          Times(Number(0), Number), environment is empty.
          Times(Variable x, Number(0)), environment is x.
          Times(Variable x, Number(0)), environment is y.
          Times(Number, Number), environment x.
          Times(Number, Variable x), environment x.
          Times(Number, Variable x), environment y.
          Times(Variable y, Number x), environment x.
          Times(Variable y, Number x), environment y.
          Times(Variable x, Variable y), environment x, y.
          Times(Times(Number, Number), Variable x), environment x.
          Times(Times(Number, Variable x), Variable x), environment x.
          Times(Times(Number, Number), Variable x), environment x.

        equals
          Reflexivity.
          Symmetry.
          Transitivity.
          Consistency.
          null.
          Check if it works recursively on two objects Times(Times, Number).


        hashCode
          Two identical Times objects.

        toString
          Number times Variable.
          Check if toString works recursively.

   */

  @Test(expected = AssertionError.class)
  public void testAssertionsEnabled() {
    assert false; // make sure  assertions are enabled with VM argument: -ea
  }

  /* Constructor -------------------------------------------------------------------------------------- */

  /* Left is null.  */
  @Test(expected = AssertionError.class)
  public void testConstructor_leftIsNull() {
    Expression left = null;
    Expression right = new Number(5);

    Expression times = new Times(left, right);
  }

  /* Right is null.  */
  @Test(expected = AssertionError.class)
  public void testConstructor_rightIsNull() {
    Expression left = new Number(5);
    Expression right = null;

    Expression times = new Times(left, right);
  }

  /* Value is empty  */
  @Test
  public void testConstructor_valueIsEmpty() {
    Expression left = new Number(3);
    Expression right = new Number(3);

    Expression times = new Times(left, right);

    assertFalse(times.getValue().isPresent());
  }


   /* differentiate  ------------------------------------------------------------------------------------------ */


  /* 1 * 2 with x as the differentiation variable. */
  @Test
  public void testDifferentiate_1_times_2_diffVar_x() {
    String diffVar = "x";
    Expression left = new Number(1);
    Expression right = new Number(2);
    Expression product = new Times(left, right);
    Expression expected = new Number(0);

    Expression derivative = product.differentiate(diffVar);

    assertTrue(expected.equals(derivative));
  }

  /* x * x with x as the differentiation variable. */
  @Test
  public void testDifferentiate_x_times_x_diffVar_x() {
    String diffVar = "x";
    Expression left = new Variable("x");
    Expression right = new Variable("x");
    Expression product = new Times(left, right);
    Expression expected = new Times(new Number(2), new Variable("x"));

    Expression derivative = product.differentiate(diffVar);

    assertTrue(expected.equals(derivative));
  }


  /* x * x with y as the differentiation variable. */
  @Test
  public void testDifferentiate_x_times_x_diffVar_y() {
    String diffVar = "y";
    Expression left = new Variable("x");
    Expression right = new Variable("x");
    Expression product = new Times(left, right);
    Expression expected = new Number(0);

    Expression derivative = product.differentiate(diffVar);

    assertTrue(expected.equals(derivative));

  }

  /*  x * y with x as the differentiation variable. */
  @Test
  public void testDifferentiate_x_times_y_diffVar_x() {
    String diffVar = "x";
    Expression left = new Variable("x");
    Expression right = new Variable("y");
    Expression product = new Times(left, right);
    Expression expected = new Variable("y");

    Expression derivative = product.differentiate(diffVar);

    assertTrue(expected.equals(derivative));
  }

  /* x * 1 with x as the differentiation variable. */
  @Test
  public void testDifferentiate_x_times_1_diffVar_x() {
    String diffVar = "x";
    Expression left = new Variable("x");
    Expression right = new Number(1);
    Expression product = new Times(left, right);
    Expression expected = new Number(1);

    Expression derivative = product.differentiate(diffVar);

    assertTrue(expected.equals(derivative));
  }

  /* 1 * x with x as the differentiation variable. */
  @Test
  public void testDifferentiate_1_times_x_diffVar_x() {
    String diffVar = "x";
    Expression left = new Number(1);
    Expression right = new Variable("x");
    Expression product = new Times(left, right);
    Expression expected = new Number(1);

    Expression derivative = product.differentiate(diffVar);

    assertTrue(expected.equals(derivative));
  }

  /* x * 1 with y as the differentiation variable. */
  @Test
  public void testDifferentiate_x_times_1_diffVar_y() {
    String diffVar = "y";
    Expression left = new Variable("x");
    Expression right = new Number(1);
    Expression product = new Times(left, right);
    Expression expected = new Number(0);

    Expression derivative = product.differentiate(diffVar);

    assertTrue(expected.equals(derivative));
  }

  /* 1 * x with y as the differentiation variable. */
  @Test
  public void testDifferentiate_1_times_x_diffVar_y() {
    String diffVar = "y";
    Expression left = new Number(1);
    Expression right = new Variable("x");
    Expression product = new Times(left, right);
    Expression expected = new Number(0);

    Expression derivative = product.differentiate(diffVar);

    assertTrue(expected.equals(derivative));
  }

  /* x * 0 with x as the differentiation variable. */
  @Test
  public void testDifferentiate_x_times_0_diffVar_x() {
    String diffVar = "x";
    Expression left = new Variable("x");
    Expression right = new Number(0);
    Expression product = new Times(left, right);
    Expression expected = new Number(0);

    Expression derivative = product.differentiate(diffVar);

    assertTrue(expected.equals(derivative));
  }

  /* 0 * x with x as the differentiation variable. */
  @Test
  public void testDifferentiate_0_times_x_diffVar_x() {
    String diffVar = "x";
    Expression left = new Number(0);
    Expression right = new Variable("x");
    Expression product = new Times(left, right);
    Expression expected = new Number(0);

    Expression derivative = product.differentiate(diffVar);

    assertTrue(expected.equals(derivative));
  }


  /* getValue  ------------------------------------------------------------------------------------------ */

  /* Check if the OptionalDouble the field value points at is empty. */
  @Test
  public void testGetValue() {
    Expression left = new Number(5);
    Expression right = new Number(5);

    Expression product = new Times(left, right);

    assertFalse(product.getValue().isPresent());
  }

  /* simplify  ------------------------------------------------------------------------------------------ */

  /*
  Times(Number(0), Number), environment is empty.
  Times(Variable x, Number(0)), environment is x.
  Times(Variable x, Number(0)), environment is y.
  Times(Number, Number), environment x.
  Times(Number, Variable x), environment x.
  Times(Number, Variable x), environment y.
  Times(Variable y, Number x), environment x.
  Times(Variable y, Number x), environment y.
  Times(Variable x, Variable y), environment x, y.
  Times(Times(Number, Number), Variable x), environment x.
  Times(Times(Number, Variable x), Variable x), environment x.
  Times(Times(Number, Number), Variable x), environment x.
  */

  /* Times(Number(0), Number(5), environment is empty. */
  @Test
  public void testSimplify_1() {
    Map<String, Double> environment = new HashMap<>();
    Expression left = new Number(0);
    Expression right = new Number(5);
    Expression product = new Times(left, right);
    Expression expected = new Number(0.0);

    Expression result = product.simplify(environment);

    assertTrue(expected.equals(result));
  }

  /* Times(Variable x, Number(0)), environment is x. */
  @Test
  public void testSimplify_2() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("x", 2.0);
    Expression left = new Variable("x");
    Expression right = new Number(0);
    Expression product = new Times(left, right);
    Expression expected = new Number(0.0);

    Expression result = product.simplify(environment);

    assertTrue(expected.equals(result));
  }

  /* Times(Variable x, Number(0)), environment is y. */
  @Test
  public void testSimplify_3() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("y", 3.0);
    Expression left = new Variable("x");
    Expression right = new Number(0);
    Expression product = new Times(left, right);
    Expression expected = new Number(0.0);

    Expression result = product.simplify(environment);

    assertTrue(expected.equals(result));
  }

  /* Times(Number, Number), environment x. */
  @Test
  public void testSimplify_4() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("x", 3.0);
    Expression left = new Number(4.0);
    Expression right = new Number(6.0);
    Expression product = new Times(left, right);
    Expression expected = new Number(24.0);

    Expression result = product.simplify(environment);

    assertTrue(expected.equals(result));
  }

  /* Times(Number, Variable x), environment x. */
  @Test
  public void testSimplify_5() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("x", 3.0);
    Expression left = new Number(3.0);
    Expression right = new Variable("x");
    Expression product = new Times(left, right);
    Expression expected = new Number(9.0);

    Expression result = product.simplify(environment);

    assertTrue(expected.equals(result));
  }

  /*  Times(Number, Variable x), environment y. */
  @Test
  public void testSimplify_6() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("y", 3.0);
    Expression left = new Number(3.0);
    Expression right = new Variable("x");
    Expression product = new Times(left, right);
    Expression expected = new Times(new Number(3.0), new Variable("x"));

    Expression result = product.simplify(environment);
    System.out.println(result);
    assertTrue(expected.equals(result));
  }

  /* Times(Variable y, Number), environment x. */
  @Test
  public void testSimplify_7() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("x", 3.0);
    Expression left = new Variable("y");
    Expression right = new Number(5.0);
    Expression product = new Times(left, right);
    Expression expected = new Times(left, right);

    Expression result = product.simplify(environment);

    assertTrue(expected.equals(result));
  }

  /* Times(Variable y, Number), environment y. */
  @Test
  public void testSimplify_8() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("y", 3.0);
    Expression left = new Variable("y");
    Expression right = new Number(5.0);
    Expression product = new Times(left, right);
    Expression expected = new Number(15.0);

    Expression result = product.simplify(environment);

    assertTrue(expected.equals(result));
  }

  /* Times(Variable x, Variable y), environment x, y. */
  @Test
  public void testSimplify_9() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("x", 3.0);
    environment.put("y", 5.0);
    Expression left = new Variable("x");
    Expression right = new Variable("y");
    Expression product = new Times(left, right);
    Expression expected = new Number(15.0);

    Expression result = product.simplify(environment);

    assertTrue(expected.equals(result));
  }


  /* Times(Times(Number, Number), Variable x), environment x. */
  @Test
  public void testSimplify_10() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("x", 3.0);
    Expression left = new Times(new Number(4.0), new Number(6.0));
    Expression right = new Variable("x");
    Expression product = new Times(left, right);
    Expression expected = new Number(72.0);

    Expression result = product.simplify(environment);

    assertTrue(expected.equals(result));
  }


  /* Times(Times(Number, Variable x), Variable x), environment x. */
  @Test
  public void testSimplify_11() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("x", 3.0);
    Expression left = new Times(new Number(5.0), new Variable("x"));
    Expression right = new Variable("x");
    Expression product = new Times(left, right);
    Expression expected = new Number(45.0);

    Expression result = product.simplify(environment);

    assertTrue(expected.equals(result));
  }

  /* Times(Plus(Number, Number), Variable x), environment x. */
  @Test
  public void testSimplify_12() {
    Map<String, Double> environment = new HashMap<>();
    environment.put("x", 3.0);
    Expression left = new Plus(new Number(5.0), new Number(6.0));
    Expression right = new Variable("x");
    Expression product = new Times(left, right);
    Expression expected = new Number(33.0);

    Expression result = product.simplify(environment);

    assertTrue(expected.equals(result));
  }


  /* equals -------------------------------------------------------------------------------------------- */
   /* Reflexivity. */
  @Test
  public void testEquals_reflexivity() {
    Expression left = new Number(5.0);
    Expression right = new Variable("x");
    Expression times = new Times(left, right);

    assertTrue(times.equals(times));
  }

  /* Symmetry. */
  @Test
  public void testEquals_symmetry() {
    Expression left = new Number(5.0);
    Expression right = new Variable("x");
    Expression product1 = new Times(left, right);
    Expression product2 = new Times(left, right);

    assertTrue(product1.equals(product2));
    assertTrue(product2.equals(product1));
  }


  /* Transitivity. */
  @Test
  public void testEquals_transitivity() {
    Expression left = new Number(5.0);
    Expression right = new Variable("x");
    Expression product1 = new Times(left, right);
    Expression product2 = new Times(left, right);
    Expression product3 = new Times(left, right);

    assertTrue(product1.equals(product2));
    assertTrue(product2.equals(product3));
    assertTrue(product1.equals(product3));
  }

  /* Consistency. */
  @Test
  public void testEquals_consistency() {
    Expression left = new Number(5.0);
    Expression right = new Variable("x");
    Expression product1 = new Times(left, right);
    Expression product2 = new Times(left, right);

    assertTrue(product1.equals(product2));
    assertTrue(product1.equals(product2));
    assertTrue(product1.equals(product2));
    assertTrue(product1.equals(product2));
    assertTrue(product1.equals(product2));
    assertTrue(product1.equals(product2));
    assertTrue(product1.equals(product2));
    assertTrue(product1.equals(product2));
    assertTrue(product1.equals(product2));
    assertTrue(product1.equals(product2));
  }

  /* null. */
  @Test
  public void testEquals_null() {
    Expression left = new Number(5.0);
    Expression right = new Variable("x");
    Expression times = new Times(left, right);

    assertFalse(times.equals(null));
  }

  /* Check if it works recursively on two equal objects Times(Times,Number) */
  @Test
  public void testEquals_recursion() {
    Expression left = new Variable("x");
    Expression right = new Number(3.0);
    Expression product1 = new Times(new Times(left, right), right);
    Expression product2 = new Times(new Times(left, right), right);

    assertTrue(product1.equals(product2));
  }


  /* hashCode ------------------------------------------------------------------------------------------ */
  @Test
  public void testHashCode() {
    Expression left = new Number(2.0);
    Expression right = new Variable("y");
    Expression product1 = new Times(left, right);
    Expression product2 = new Times(left, right);

    assertTrue(product1.hashCode() == product2.hashCode());
  }

  /* toString ------------------------------------------------------------------------------------------ */

  @Test
  public void testToString_numberTimesVariable() {
    Expression left = new Number(5);
    Expression right = new Variable("x");
    String expected = "(5) * (x)";

    String result = new Times(left, right).toString();

    assertTrue(expected.equals(result));
  }

  @Test
  public void testToString_recursive() {
    Expression left = new Number(5);
    Expression right = new Variable("x");
    Expression product = new Times(left, right);
    String expected = "((5) * (x)) * (y)";

    String result = new Times(product, new Variable("y")).toString();

    assertTrue(expected.equals(result));
  }


  /* equals ----------------------------------------------------------------------------------------------*/

  //TODO: equals and hashCode

}
