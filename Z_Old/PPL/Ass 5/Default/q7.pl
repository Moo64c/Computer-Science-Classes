/*
 * **********************************************
 * aux predicates (see ex5-aux.pl documentation)
 * **********************************************
 */
:- use_module('ex5-aux').

/*
 * **********************************************
 * Question 7:
 * 
 * Change the interpreter such that every answer is 
 * returned four times instead of once.
 * 
 * The order by which answers are returned does not matter -
 * as long as all answers are retuned.
 * **********************************************
 */

% Signature: solve(Exp, Proof)/2
% Purpose: meta circular interpreter for Prolog
%          which returnes each answer 4 times.
%          The answers order is irrelevant, of course.
% Examples:
%rule(member(A, [A|_]), []).
%rule(member(A, [_|B]), [member(A, B)]).
%
% ?- solve(true, _).
% true;
% true;
% true;
% true;
% false.
% 
% ?- solve(member(X, [1,2]), _).
% X = 1 ;
% X = 2 ;
% X = 1 ;
% X = 2 ;
% X = 1 ;
% X = 2 ;
% X = 1 ;
% X = 2 ;
% false
% 

solve(A,B):- inner_solve(A,B), member(_, [1,2,3,4]).

inner_solve(true, true). %1
inner_solve([], []). %2
inner_solve([A|B],[ProofA|ProofB]) :- inner_solve(A, ProofA), inner_solve(B, ProofB). %3
inner_solve(A, node(A,Proof)) :- rule(A, B), inner_solve(B,Proof).
