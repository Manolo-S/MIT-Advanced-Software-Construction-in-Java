import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;

/**
 * This immutable data type represents a multiplication of two expressions.
 */

final class Times implements Expression {

  private final Expression left, right;
  private final OptionalDouble value;

  /* Abstraction function
       represents the product of two expressions: getLeft * getRight
     Rep invariant
       left and right can't be null.
       value is an empty OptionalDouble.
     Safety from rep exposure
       all fields are immutable and final
  */

  /**
   * @param left operand of the multiplication
   * @param right operand of the multiplication
   */
  Times(Expression left, Expression right) {
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
    assert left != null : "The getLeft side of the times expression should not be null";
    assert right != null : "The getRight side of the times expression should not be null";
    assert value != null : "the value of the plus expression should not be null";
  }


  /**
   * Differentiates an expression and simplifies it.
   *
   * @param diffVar the variable to differentiate by, a case-sensitive nonempty string of letters
   * @return the derivative of this
   */
  @Override
  public Expression differentiate(String diffVar) {
    Map<String, Double> environment = new HashMap<>();
    Expression leftDerivative = left.differentiate(diffVar).simplify(environment);
    Expression rightDerivative = right.differentiate(diffVar).simplify(environment);
    Expression leftProduct = (new Times(left, rightDerivative)).simplify(environment);
    Expression rightProduct = (new Times(right, leftDerivative)).simplify(environment);
    return (new Plus(leftProduct, rightProduct)).simplify(environment);
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
   *  1) if left is a Number and right is a Number, a Number is returned  with the product of left and right.
   *  2) if left or right is Number(0.0) then Number(0.0) is returned.
   *  3) if left or right is Number(1.0) then the other operand is recursively simplified and returned.
   *  4) otherwise a Times is returned with the left and right sides recursively simplified.
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
          leftSimplified.getValue().getAsDouble() * rightSimplified.getValue().getAsDouble());
    }

    if (leftHasValue && leftSimplified.getValue().getAsDouble() == 0 ||
        rightHasValue && rightSimplified.getValue().getAsDouble() == 0) {
      return new Number(0);
    }

    if (leftHasValue && leftSimplified.getValue().getAsDouble() == 1.0) {
      return rightSimplified;
    }

    if (rightHasValue && rightSimplified.getValue().getAsDouble() == 1.0) {
      return leftSimplified;
    } else {
      return new Times(leftSimplified, rightSimplified);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || !(o instanceof Times)) {
      return false;
    }

    Times times = (Times) o;

    if (!left.equals(times.left)) {
      return false;
    }
    return right.equals(times.right);
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
    return "(" + left + ") * (" + right + ")";
  }
}