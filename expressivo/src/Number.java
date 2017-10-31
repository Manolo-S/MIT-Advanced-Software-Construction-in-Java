import java.util.Map;
import java.util.OptionalDouble;

/**
 * This immutable data type represents a non-negative decimal in the double range from an expression
 */

final class Number implements Expression {

  private final OptionalDouble value;

/* Abstraction function
      represents a non-negative decimal in the double range from an expression
   Rep invariant
      value contains a non-negative double wrapped in an OptionalDouble
   Safety from rep exposure
      all fields are immutable and final
*/

  /**
   * @param n: n is a non-negative decimal value in the double range   *
   */
  public Number(final double n) {
    assert n >= 0 : "The number hould be non-negative.";
    this.value = OptionalDouble.of(n);
    checkRep();
  }

  /**
   * Check if the representation invariants hold
   */
  private void checkRep() {
    assert
        value.getAsDouble() >= 0 : "A number should be a non-negative decimal in the double range.";
  }

  /**
   * @param diffVar: is a string of the pattern [a-zA-Z]+
   * @return: The derivative of the number contained by value.
   */
  @Override
  public Expression differentiate(final String diffVar) {
    assert diffVar.matches("[a-zA-Z]+") : "Differentiation variable name should match [a-zA-Z]";
    checkRep();
    return new Number(0);
  }


  /**
   * @return the number this represents wrapped in an OptionalDouble
   */
  @Override
  public OptionalDouble getValue() {
    checkRep();
    return value;
  }

  /**
   * @param environment contains a collection of expression variable + value pairs
   * @return this (as there is nothing to simplify)
   */
  @Override
  public Expression simplify(final Map<String, Double> environment) {
    checkRep();
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || !(o instanceof Number)) {
      return false;
    }
    Number number = (Number) o;
    return value.getAsDouble() == number.getValue().getAsDouble();
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = (int) (31 * result + value.getAsDouble());
    return result;
  }

  @Override
  public String toString() {
    String num = String.valueOf(value.getAsDouble());
    if (num.matches("[0-9]+.0")) {
      num = num.replace(".0", "");
    }
    return num;
  }


}