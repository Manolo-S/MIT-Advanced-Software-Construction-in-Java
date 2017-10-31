import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;

/**
 * This immutable data type represents a sum of two expressions.
 */

final class Plus implements Expression {

  private final Expression left, right;
  private final OptionalDouble value;

   /* Abstraction function
        Represents the sum of two expressions
      Rep invariant
        left and right can't be null.
        value is an empty OptionalDouble.
      Safety from rep exposure
        All fields are immutable and final.
   */

  /**
   * @param left operand of the multiplication
   * @param right operand of the multiplication
   */
  Plus(Expression left, Expression right) {
    assert left != null : "Expressions can't be null.";
    assert right != null : "Expressions can't be null.";
    this.left = left;
    this.right = right;
    value = OptionalDouble.empty();
    checkRep();
  }

  /**
   * Checks if the representation invariants hold
   */
  private void checkRep() {
    assert left != null : "The getLeft side of the plus expression should not be null";
    assert right != null : "The getRight side of the plus expression should not be null";
    assert value != null : "the value of the plus expression should not be null";
  }

  /**
   * Differentiates an expression and simplifies it.
   *
   * @param diffVar is a string of the pattern [a-zA-Z]+
   * @return the derivative of this
   */
  @Override
  public Expression differentiate(String diffVar) {
    Map<String, Double> environment = new HashMap<>();
    Expression leftDifferentiated = left.differentiate(diffVar).simplify(environment);
    Expression rightDifferentiated = right.differentiate(diffVar).simplify(environment);
    Expression derivative = new Plus(leftDifferentiated, rightDifferentiated);
    return derivative.simplify(environment);
  }

  /**
   * @return an empty OptionalDouble
   */
  @Override
  public OptionalDouble getValue() {
    return value;
  }

  /**
   * Recursively simplifies an expression.
   *
   * Simplification works as follows: First left and right are recursively simplified.
   * Then:
   *  1) if left is a Number and right is a Number, a Number is returned  with the sum of left and right.
   *  2) if left and right are equal then Times(2.0, left) is returned.
   *  3) if left or right is Number(0.0) then the only the other side, e.g. Number(5.0), is returned.
   *  4) otherwise a Plus is returned with the left and right sides recursively simplified.
   *
   * @param environment contains a collection of variable + value pairs.
   * @return a recursively simplified expression.
   */
  @Override
  public Expression simplify(Map<String, Double> environment) {
    Expression leftSimplified = left.simplify(environment);
    Expression rightSimplified = right.simplify(environment);
    boolean leftHasValue = leftSimplified.getValue().isPresent();
    boolean rightHasValue = rightSimplified.getValue().isPresent();
    if (leftHasValue && rightHasValue) {
      return new Number(
          leftSimplified.getValue().getAsDouble() + rightSimplified.getValue().getAsDouble());
    }

    if (leftSimplified.equals(rightSimplified)){
      return new Times(new Number(2), leftSimplified);
    }

    if ((leftHasValue && (leftSimplified.getValue().getAsDouble() == 0.0)) && !rightHasValue) {
      return rightSimplified;
    }

    if ((rightHasValue && (rightSimplified.getValue().getAsDouble() == 0.0)) && !leftHasValue) {
      return leftSimplified;
    } else {
      return new Plus(leftSimplified, rightSimplified);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || !(o instanceof Plus)) {
      return false;
    }

    Plus plus = (Plus) o;

    if (!left.equals(plus.left)) {
      return false;
    }
    return right.equals(plus.right);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + left.hashCode();
    result = 31 * result + right.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "(" + left + ") + (" + right + ")";
  }
}
