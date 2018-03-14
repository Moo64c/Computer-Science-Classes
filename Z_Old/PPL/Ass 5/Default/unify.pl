/*
 * **********************************************
 * aux predicates (see ex5-aux.pl documentation)
 * **********************************************
 */
:- use_module('ex5-aux').

/*
 * **********************************************
 * Question 5b3:
 * 
 * Purpose: ExpS is the application of a substitution S on a ground term Exp,
 *          when the code is efficent than in course book one.
 * **********************************************
 */
% Signature: unify(A, B, Mgu)/3
% Examples:
% ?- unify(a(x, 5), a(6, y), Mgu).
% Mgu = [[y, 5], [x, 6]].
% ?- unify(c(a(x), z, x), c(z, y, a(d)), Mgu).
% Mgu = [[x, a(d)], [y, a(a(d))], [z, a(a(d))]]
% ?- unify(c(a(x), d, x), c(z, y, a(z)), Mgu).
% false
%
unify(A, B, Mgu) :- help(A, B, [], Mgu).                                %1

help(A, B, S, Mgu) :- substitute(A, S, AS),                             %1
	substitute(B, S, BS),
	help_helper(A, B, S, AS, BS, Mgu).

help_helper(_A, _B, S, AS, AS, S) :- !.                                   %1
help_helper(A, B, S, AS, BS, Mgu) :- disagreement(AS, BS, [X, T]),      %2
	not_occurs(T, X),
	substitute(S, [[X, T]], SS),
	help(A, B, [[X, T]|SS], Mgu).

% Signature: disagreement(A, B, Set)/3
disagreement(AS, BS, [AS, BS]) :- 
	variable(AS),
	!.                      		   %1
disagreement(AS, BS, [BS, AS]) :- 
	variable(BS),                       	  %2
	!.
disagreement([A|AS], [A|BS], [X, T]) :-           %3 
	!, 
	disagreement(AS, BS, [X, T]).
disagreement([A|_], [B|_], [X, T]) :-            %4
	!,  
	disagreement(A, B, [X, T]).
disagreement(AS, _, _) :- 
	constant(AS),                                 %5
	!,
	fail.
disagreement(_, BS, _) :- 
	constant(BS),                                 %6
	!,
	fail.
disagreement(A, B, [X, T]) :- 
	A =.. A_list,                             %7
	B =.. B_list,
	disagreement(A_list, B_list, [X, T]).

% Signature: not_occurs(T, X)/2
%
not_occurs(X, X) :-                                      %1
	!,
	fail.
not_occurs(T, _X) :- 
	variable(T),                                       %2
	!.
not_occurs(T, _X) :- 
	constant(T),                                       %3
	!.
not_occurs([T|TT], X) :-                                    %4
	!, 
	not_occurs(T, X),
	not_occurs(TT, X),
	!.
not_occurs(T, X) :- 
	T =.. T_list,                                       %5
	not_occurs(T_list, X).

% Signature: substitute(Exp, S, ExpS)/3
% Purpose: ExpS is the application of a substitution S on a ground term Exp.
% Pre-condition: All logic programming constant symbols in a query must be
%               declared either as expression constants  or as expression
%               variables. That is, belong to one of the constant or variable
%               lists. Otherwise, the program enters an infinite loop.
%
substitute(Exp, [], Exp) :- 
	!.                                               %1
substitute(X, S, XS) :- 
	variable(X),
	member([X, XS], S),                             %2
	!.
substitute(X, _S, X) :- 
	variable(X),                                         %3
	!.
substitute(X, _S, X) :- 
	constant(X),                                         %4
	!.
substitute([E|Exp], S, [ES|ExpS]) :-                            %5
	!,
	substitute(E, S, ES),
	substitute(Exp, S, ExpS),
	!.
substitute(Exp, S, ExpS) :- 
	Exp =.. List,                                    %6
	substitute(List, S, ListS),
	ExpS =.. ListS.
