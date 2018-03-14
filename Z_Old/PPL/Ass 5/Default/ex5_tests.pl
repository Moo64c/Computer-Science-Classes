% Test cases for assignment 5.
% To use this file: open new Prolog session and load this file.

% File syntax: test(<name>) :- <actions>.

:- [testutils, q3, q4, q7].
:- abolish(substitute/3).
:- [substitute].
:- begin_tests(ex5_tests_a).

/*
 * **********************************************
 * Question 3 Tests:
 * **********************************************
 */

% Question 3.a

test(page_in_category_01) :- runCheck(page_in_category(P, C), 
                             (P, C), 
                             [(adp, 786983), 
                              (cnn, 786983), 
                              (ocpc, 564677), 
                              (cnn, 786983), 
                              (nbc, 564677)]).
test(page_in_category_02) :- getFail(page_in_category(_, 689969)).
test(page_in_category_03) :- getFail(page_in_category(_, 111111)).
test(page_in_category_04) :- getFail(page_in_category(aisi, _)).
test(page_in_category_05) :- getFail(page_in_category(aisiaisi, _)).
test(page_in_category_06) :- runCheck(page_in_category(cnn, X), 
                                   (X), 
                                   [(786983), (786983)]).

test(page_in_category_07) :- runCheck(page_in_category(X, 564677), 
                                   (X), 
                                   [(ocpc), (nbc)]).

% Question 3.b

test(splitter_category_01) :- getTrue(splitter_category(689969)).
test(splitter_category_02) :- getTrue(splitter_category(786983)).
test(splitter_category_03) :- getTrue(splitter_category(564677)).
test(splitter_category_04) :- getFail(splitter_category(858585)).
test(splitter_category_05) :- getFail(splitter_category(578483)).
test(splitter_category_06) :- getFail(splitter_category(a)).
test(splitter_category_07) :- sortedCheck(splitter_category(X), (X), [564677, 689969, 786983]).

% Question 3.c

test(namespace_list_01) :- runCheck((namespace_list(category, Y), msort(Y, Z)), 
                           (Z), 
                           ([[564677, 578483, 689969, 786983]])).
test(namespace_list_02) :- runCheck((namespace_list(X, Y), msort(Y, Z)), 
                          (X, Z), 
                          [(article, [558585, 689695, 858585]), 
                           (article_conversation, [558588]), 
                           (user, [696696]), 
                           (user_conversation, [696678]), 
                           (project, []), 
                           (project_conversation, []), 
                           (file, [568585]), 
                           (file_conversation, []), 
                           (mediawiki, []), 
                           (mediawiki_conversation, []), 
                           (template, [858485]), 
                           (template_conversation, []), 
                           (category, [564677, 578483, 689969, 786983]), 
                           (category_conversation, [786984]), 
                           (draft, [464236]), 
                           (draft_conversation, []), 
                           (module, []), 
                           (module_conversation, [])]).
test(namespace_list_03) :- getFail(namespace_list(a, _)).
test(namespace_list_04) :- getFail(namespace_list(_, a)).
test(namespace_list_05) :- sortedCheck((permutation([558585, 689695, 858585], 
                                X), namespace_list(Y, X)), (Y), [(article)]).
test(namespace_list_06) :- getFail((permutation([558585, 689695, 8585853], X), 
                   namespace_list(_, X))).


/*
 * **********************************************
 * Question 4
 * **********************************************
 */

% Question 4.a
test(allDiff_01) :- runCheck(allDiff([2, 3, 4, 5], Dups), (Dups), ([[]])).
test(allDiff_02) :- runCheck(allDiff([2, 3, 4, 2, 8, 9, 2], Dups), (Dups), ([[2]])).
test(allDiff_03) :- runCheck(allDiff([], Dups), (Dups), ([[]])).
test(allDiff_04) :- runCheck(allDiff([1, 1, 1, 1], Dups), (Dups), ([[1]])).
test(allDiff_05) :- runCheck(allDiff([1, 1, 1, 1, 5, 1, 1, 1, 1], Dups), (Dups), ([[1]])).
test(allDiff_06) :- runCheck(allDiff([5, 1, 1, 1, 1, 5, 1, 1, 1, 1, 3, 5], Dups), (Dups), ([[1, 5]])).
test(allDiff_07) :- getTrue(allDiff([1, 1, 1, 1, 5, 1, 1, 1, 1], [1])).
test(allDiff_08) :- getFail(allDiff([1, 1, 1, 1, 5, 1, 1, 1, 1], [1, 1])).
test(allDiff_09) :- getFail(allDiff([1, 1, 1, 1, 5, 1, 1, 1, 1], [1, 5])).
test(allDiff_10) :- getFail(allDiff([1, 1, 1, 1, 5, 1, 1, 1, 1], [5, 1])).

% Question 4.b
test(noDups_01) :- runCheck(noDups([1, 2, 3, 4, 5], X), (X), ([[1, 2, 3, 4, 5]])).
test(noDups_02) :- runCheck(noDups([1, 2, 3, 4, 5, 3, 4, 5], X), (X), ([[1, 2, 3, 4, 5]])).
test(noDups_03) :- getTrue(noDups([1, 2, 3, 4, 5, 3, 4, 5], [1, 2, 3, 4, 5])).
test(noDups_04) :- getFail(noDups([1, 2, 3, 4, 5, 3, 4, 5], [1, 2, 4, 5, 3])).

% Question 4.c
test(nondupCFG_01) :- getTrue(nondupCFG([a, cat, saw, the, dog])).
test(nondupCFG_02) :- getTrue(nondupCFG([the, cat, saw, a, dog])).
test(nondupCFG_03) :- getFail(nondupCFG([the, cat, saw, the, dog])).
test(nondupCFG_04) :- getFail(nondupCFG([the, cat, saw, a, weird, dog])).

/*
 * **********************************************
 * Question 7
 * **********************************************
 */

test(solve_01) :- getTrue(solve(true, _)).
test(solve_02) :- getFail(solve(false, _)).
test(solve_03) :- runCheck(solve(member(X, [1, 2]), _), (X), [1, 1, 1, 1, 2, 2, 2, 2]).
 
/*
 * **********************************************
 * Question 5
 * **********************************************
 */

% Question 5b2

test(subs_01) :- runCheck(substitute(p(a, b(x), c(d(y)), x), [[z, 5], [x, 6]], Ans), (Ans), [p(a, b(6), c(d(y)), 6)]).
test(subs_02) :- runCheck(substitute(p(a, b(x), c(d(y)), x), [[x, b], [y, 5]], Ans), (Ans), [p(a, b(b), c(d(5)), b)]).
test(subs_02) :- getTrue(substitute(p(z, b(x), c(d(y)), x), [[x, b], [z, 6], [y, 5]], p(6, b(b), c(d(5)), b))).

:- end_tests(ex5_tests_a).
:- run_tests(ex5_tests_a), 
   now, 
   writeln(': The test suite a succeeded');
   writeln('ERROR: The test suite a FAILED').

% Question 5b3

:- abolish(substitute/3).
:- [unify].
:- begin_tests(ex5_tests_b).

test(unify_01) :- runCheck(unify(a(x, 5), a(6, y), Mgu), (Mgu), [[[y, 5], [x, 6]]]).
test(unify_02) :- runCheck(unify(c(a(x), z, x), c(z, y, a(d)), Mgu), (Mgu), [[[x, a(d)], [y, a(a(d))], [z, a(a(d))]]]).
test(unify_03) :- getFail(unify(c(a(x), d, x), c(z, y, a(z)), _Mgu)).

:- end_tests(ex5_tests_b).
:- run_tests(ex5_tests_b), 
   now, 
   writeln(': The test suite b succeeded');
   writeln('ERROR: The test suite b FAILED').

