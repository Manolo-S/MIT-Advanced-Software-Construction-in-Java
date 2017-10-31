import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.OptionalDouble;
import lib6005.parser.GrammarCompiler;
import lib6005.parser.ParseTree;
import lib6005.parser.Parser;
import lib6005.parser.UnableToParseException;

/**
 * An immutable data type representing a polynomial expression of:
 * + and *
 * nonnegative integers and floating-point numbers
 * variables (case-sensitive nonempty strings of letters)
 *
 * <p>PS1 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {

  /* Datatype definition
       Expression = Number(n:int)
                    + Variable(var:String)
                    + Plus(getLeft:Expression, getRight:Expression)
                    + Times(getLeft:Expression, getRight:Expression)
*/

  enum DerivativesGrammarSymbols {
    ROOT,
    EXPR,
    TERM,
    FACTOR,
    NUMBER,
    VARIABLE,
    WHITESPACE
  }

  public static Expression parse(String input) throws UnableToParseException, IOException {
    //TODO change name to createParseTree? and remove call to buildAST
    Parser<DerivativesGrammarSymbols> parserlib = GrammarCompiler
        .compile(new File("./src/Expression.g"), DerivativesGrammarSymbols.ROOT);
    ParseTree<DerivativesGrammarSymbols> tree = parserlib.parse(input);
    return buildAST(tree);
  }

  /**
   * Function converts a ParseTree to an Expression.
   *
   * // @param p  ParseTree<DerivativesGrammarSymbols> that is assumed to have been constructed by
   * the grammar in Expression.g
   */

  static Expression extractExpression(ParseTree<DerivativesGrammarSymbols> child) {

    if (!child.childrenByName(DerivativesGrammarSymbols.NUMBER).isEmpty()) {
      return buildAST(child.childrenByName(DerivativesGrammarSymbols.NUMBER).get(0));
    } else if (!child.childrenByName(DerivativesGrammarSymbols.VARIABLE).isEmpty()) {
      return buildAST(child.childrenByName(DerivativesGrammarSymbols.VARIABLE).get(0));
    } else {
      return buildAST(child.childrenByName(DerivativesGrammarSymbols.EXPR).get(0));
    }
  }

  static Expression buildAST(ParseTree<DerivativesGrammarSymbols> p) {

    switch (p.getName()) {

      case NUMBER:
        return new Number(Double.parseDouble(p.getContents()));

      case VARIABLE:
        return new Variable(p.getContents());

      case TERM:
        boolean first = true;
        Expression res = null;
        for (ParseTree<DerivativesGrammarSymbols> child : p
            .childrenByName(DerivativesGrammarSymbols.FACTOR)) {
          if (first) {
            res = extractExpression(child);
            first = false;
          } else {
            res = new Times(res, extractExpression(child));
          }
        }
        return res;

      case EXPR:
        boolean firstChild = true;
        Expression result = null;
        for (ParseTree<DerivativesGrammarSymbols> child : p
            .childrenByName(DerivativesGrammarSymbols.TERM)) {
          if (firstChild) {
            result = buildAST(child);
            firstChild = false;
          } else {
            result = new Plus(result, buildAST(child));
          }
        }
        if (firstChild) {
          throw new RuntimeException("term must have a non whitespace child:" + p);
        }
        return result;

      case ROOT:
        return buildAST(p.childrenByName(DerivativesGrammarSymbols.EXPR).get(0));

      case WHITESPACE:
        throw new RuntimeException("You should never reach here:" + p);
    }
    throw new RuntimeException("You should never reach here:" + p);
  }

  //TODO: write spec

  public Expression differentiate(String diffVar);

  public OptionalDouble getValue();

  public Expression simplify(Map<String, Double> environment);

  /**
   * @return a parsable representation of this expression, such that for all e:Expression,
   * e.equals(Expression.parse(e.toString())).
   */
  @Override
  public String toString();

  /**
   * @param thatObject any object
   * @return true if and only if this and thatObject are structurally-equal Expressions, as defined
   * in the PS1 handout.
   */
  @Override
  public boolean equals(Object thatObject);

  /**
   * @return hash code value consistent with the equals() definition of structural equality, such
   * that for all e1,e2:Expression, e1.equals(e2) implies e1.hashCode() == e2.hashCode()
   */
  @Override
  public int hashCode();


    /* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires permission of course staff.
     */
}
