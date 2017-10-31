# MIT-Advanced-Software-Construction-in-Java

Course page: https://www.edx.org/course/advanced-software-construction-java-mitx-6-005-2x

<b>TOPICS</b>
1. <b>Recursion</b>:
    recursive steps and base cases, 
    helper methods, 
    recursion vs. iteration
2. <b>Recursive Data Types</b>
    data type definitions, 
    functions over recursive data types, 
    immutable lists
3. <b>Regular Expressions & Grammars</b>
    grammar terminals, nonterminals, productions, 
    regular expressions, 
    matching a grammar or regular expression, 
    using a grammar or regular expression to parse
4. <b>Parser Generators</b>
    using a parser-generator library to convert a grammar to a parser
5. <b>Concurrency</b>
    models of concurrency: shared memory and message passing, 
    processes and threads, 
    time-slicing, 
    race conditions
6. <b>Thread Safety</b>
    confinement, 
    immutability, 
    threadsafe data types
7. <b>Sockets and Networking</b>
    client/server model, 
    sockets, streams, and buffers, 
    blocking
8. <b>Queues & Message Passing</b>
    synchronous queues, 
    poison pills
9. <b>Locks & Synchronization</b>
    locks, 
    deadlock, 
    monitor pattern, 
    Java's synchronized keyword
    
<b>Problem Set 1: Expressivo</b>

<b>Overview</b>



Wouldn’t it be nice not to have to differentiate all our calculus homework by hand every time? Wouldn’t it be just lovely to type it into a computer and have the computer do it for us instead? For example, we could interact with it like this (> is user prompt):
(If the output is an expression, your system may output an equivalent expression, including variations in spacing, parentheses, simplification, and number representation.If a number, your system may output an equivalent number, accurate to at least 4 decimal places.)

> x * x * x

  x * x * x

> !simplify x=2

8

> !d/dx

(x* x ) * 1 + (x * 1 + 1 * x) * x

> !simplify x=0.5000

.75

> x * y

x * y

> !d/dy

0 * y + x * 1

In this system, a user can enter either an expression or a command.

An expression is a polynomial consisting of:

+ and * (for addition and multiplication)
nonnegative numbers in decimal representation, which consist of digits and an optional decimal point (e.g. 7 and 4.2)
variables, which are case-sensitive nonempty sequences of letters (e.g. y and Foo)
parentheses (for grouping)
Order of operations uses the standard PEMDAS rules.

Space characters around symbols are irrelevant and ignored, so 2.3*pi means the same as 2.3 * pi. Spaces may not occur within numbers or variables, so 2 . 3 * p i is not a valid expression.

When the user enters an expression, that expression becomes the current expression and is echoed back to the user (user input in green):

If a number, your system may output an equivalent number, accurate to at least 4 decimal places.

> x * x * x

x * x * x

> x + x * x * y + x

x + x * x * y + x
A command starts with !. The command operates on the current expression, and may also update the current expression. Valid commands:

d/dvar 
produces the derivative of the current expression with respect to the variable var, and updates the current expression to that derivative

simplify var1=num1 ... varn=numn 
substitutes numi for vari in the current expression, and evaluates it to a single number if the expression contains no other variables; does not update the current expression

Entering an invalid expression or command prints an error but does not update the current expression. The error should include a human-readable message but is not otherwise specified.

More examples:

If a number, your system may output an equivalent number, accurate to at least 4 decimal places.

> x * x * x

x * x * x

> 3xy

ParseError: unknown expression

> !d/dx

(x * x) * 1 + (x * 1 + 1 * x) * x

> !d/dx

(x * x) * 0 + (x * 1 + 1 * x) * 1 + (x * 1 + 1 * x) * 1 + (x * 0 + 1 * 1 + x * 0 + 1 * 1 ) * x

> !d/d

ParseError: missing variable in derivative command

> !simplify

(x * x) * 0 + (x * 1 + 1 * x) * 1 + ( x * 1 + 1 * x) * 1 + (x * 0 + 1 * 1 + x * 0 + 1 * 1) * x

> !simplify x=1

6.0

> !simplify x=0 y=5

0

The three things that a user can do at the console correspond to three provided method specifications in the code for this problem set:

Expression.parse()
Commands.differentiate()
Commands.simplify()
These methods are used by Main to provide the console user interface described above.

Problem 1: we will create the Expression data type to represent expressions in the program.

Problem 2: we will create the parser that turns a string into an Expression, and implement Expression.parse().

Problems 3-4: we will add new Expression operations for differentiation and simplification, and implement Commands.differentiate() and Commands.simplify().
