/*
 * **********************************************
 * aux predicates (see ex5-aux.pl documentation)
 * **********************************************
 */
:- use_module('ex5-aux').

/*
 * **********************************************
 * Question 3:
 * **********************************************
 */

% Signature: allDiff(List, Dups)/2
% Purpose: Dups contains all the elements that appear more than once in List
%          Notice: Dups may not contain duplicate members! 
% Pre-Condition: List is a proper list
% Examples:
% ?- allDiff([2, 3, 4, 5], Dups).
% Dups = []
% ?- allDiff([2, 3, 4, 2, 8, 9, 2], Dups).
% Dups = [2]
%
not_member(X,A):- member(X,A),!,false.
not_member(_,_).

allYAreDiffsInX(_, []).
allYAreDiffsInX(X, [Y|Ys]):- append(Z,W,X), member(Y,Z), member(Y,W),  allYAreDiffsInX(X, Ys).

notDupList([]).
notDupList([Y|Ys]):- not_member(Y,Ys), notDupList(Ys).

allDiffOfXInY([], _, _).
allDiffOfXInY([X|Xs], Z, Y):- not_member(X,Z), allDiffOfXInY(Xs, [X|Z], Y).
allDiffOfXInY([X|Xs], Z, Y):- member(X,Y), allDiffOfXInY(Xs, Z, Y).

allDiff(X,Y):- allDiffOfXInY(X, [], Y), allYAreDiffsInX(X,Y), notDupList(Y),!.
	

% Signature: noDups(List, WithoutDups)/2
% Purpose: WithoutDups contains the same elements of List with 
%          duplications removed. Meaning, for every element X in 
%          List you should keep the first appearance of X and remove 
%          the rest.
% Pre-Condition: List is a proper list
% Examples:
% ?- noDups([1, 2, 3, 4, 5], X).
% X = [1, 2, 3, 4, 5].
% ?- noDups([1, 2, 3, 4, 5, 3, 4, 5], X).
% X = [1, 2, 3, 4, 5].
% ?- noDups([1, 2, 3, 4, 5, 3, 4, 5], [1, 2, 3, 4, 5]).
% true.
% ?- noDups([1, 2, 3, 4, 5, 3, 4, 5], [1, 2, 4, 5, 3]).
% false.
%
noDups([X| Xs], Acc, Y):- member(X, Acc), noDups(Xs, Acc, Y).
noDups([X| Xs], Acc, [Y|Ys]):- not_member(X,Acc),X = Y, noDups(Xs, [X|Acc], Ys).
noDups([], _, []).
noDups(List, WithoutDups) :-noDups(List, [], WithoutDups).


% Signature: nondupCFG(Text)\1
% Purpose: Test is in the grammer defined by "s" ,
%          and contains no duplicate words.
% Pre-Condition: Text is bound
% Examples:
% ?- nondupCFG([a, cat, saw, the, dog]).
% true.
% ?- nondupCFG([a, cat, saw, a, dog]).
% false.
% ?- nondupCFG([a, cat, understood, the, dog]).
% false.
%

nondupCFG(Text) :- s(Text), noDups(Text).
