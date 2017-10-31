import java.util.Map;
import java.util.OptionalDouble;

/**
 * This immutable data type represents an expression variable
 */

final class Variable implements Expression {

  private final String variable;
  private final OptionalDouble value;

  /* Abstraction function
       represents a variable from an expression
    Rep invariant
       the variable named variable consists of one or more letters, lower and/or uppercase
       variable value is an empty OptionalDouble
    Safety from exposure
       all fields are immutable and final
  */

  /**
   * Constructor
   *
   * @param variable One or more lower and or uppercase letters.
   * @throws IllegalArgumentException if invariant is violated.
   */
  Variable(String variable) {
    assert variable.matches("[a-zA-Z]+") : "Variable named variable should match [a-zA-Z]";
    this.variable = variable;
    this.value = OptionalDouble.empty();
    checkRep();
  }

  /**
   * Check if the representation invariants hold.
   */
  private void checkRep() {
    assert variable.matches("[a-zA-Z]+") : "Variable named variable should match [a-zA-Z]";
    assert !value.isPresent();
  }


  /**
   * @param differentiationVariable Is a string of the pattern [a-zA-Z]+
   * @return An expression which is the derivative of the variable named variable
   */
  @Override
  public Expression differentiate(String differentiationVariable) {
    assert differentiationVariable.matches("[a-zA-Z]+") : "Differentiation variables should match [a-zA-Z]+";
    checkRep();
    return variable.equals(differentiationVariable) ? new Number(1) : new Number(0);
  }

  /**
   * @return An empty OptionalDouble.
   */
  @Override
  public OptionalDouble getValue() {
    checkRep();
    return value;
  }

  /**
   * @param environment Contains a collection of variable + value pairs.
   * @return If variable is found in the environment return a Number with the variable's
   *         mapped value. Otherwise return this.
   */
  @Override
  public Expression simplify(Map<String, Double> environment) {
    checkRep();
    return environment.containsKey(variable) ? new Number(environment.get(variable)) : this;
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    if (that == null || !(that instanceof Variable)) {
      return false;
    }

    Variable thatVariable = (Variable) that;

    return variable.equals(thatVariable.variable);
  }

  @Override
  public int hashCode() {
    return variable.hashCode();
  }

  @Override
  public String toString() {
    return variable;
  }

}