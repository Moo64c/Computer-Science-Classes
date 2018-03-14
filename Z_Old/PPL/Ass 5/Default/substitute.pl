/*
 * **********************************************
 * aux predicates (see ex5-aux.pl documentation)
 * **********************************************
 */
:- use_module('ex5-aux').

/*
 * **********************************************
 * Question 5b2:
 * 
 * Remove exactly one rule from the substitute/3 procedure 
 * such that the modified procedure would be equivalent to 
 * the original.
 * **********************************************
 */

% Signature: substitute(Exp, S, ExpS)/3
% Purpose: remove exactly one rule from the code,
%          when the result procedure is equivalent to the start one.
% Pre-condition: All logic programming constant symbols in a query must be
%               declared either as expression constants  or as expression
%               variables. That is, belong to one of the constant or variable
%               lists. Otherwise, the program enters an infinite loop.
% Examples:
% ?- substitute(p(a, b(x), c(d(y)), x), [[z, 5], [x, 6]], Ans).
% Ans = p(a, b(6), c(d(y)), 6).
% 
substitute(Exp, [], Exp) :- 
	!.                                               %1
substitute(X, S, XS) :- 
	variable(X), 
	member([X, XS], S),                     %2
	!.
substitute(X, _S, X) :- 
	variable(X),                                         %3
	!.
substitute(X, _S, X) :- 
	constant(X),                                         %4
	!.
substitute([E|Exp],S,[ES|ExpS]) :-                                           %5
	!,
	substitute(E, S, ES),
	substitute(Exp, S, ExpS),
	!.
substitute(Exp, S, ExpS) :- 
	Exp =.. List,                                    %6
	substitute(List, S, ListS), 
	ExpS =.. ListS.
