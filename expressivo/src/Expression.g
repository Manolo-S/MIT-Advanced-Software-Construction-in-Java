	root ::= expr;
  @skip whitespace{
  expr ::= term ('+' term)*;
  term ::= factor ('*' factor)*;
  factor ::= '('expr')'| number | variable;
  }
  number ::= [0-9]+ | [0-9]+[.][0-9]+;
  variable ::= [a-zA-Z]+;
  whitespace ::= [\s]+;