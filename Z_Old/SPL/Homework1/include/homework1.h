#ifndef HOMEWORK1_H_INCLUDED
#define HOMEWORK1_H_INCLUDED

#include <cmath>
#include <iostream>
#include <vector>

using namespace std;

/******
 * Converts a decimal number to base2.
 */
long base2(int number) {
    long numberInBase2 = 0;

    int index = 0;
    while (number > 0) {

        if (number % 2 == 1) {
            numberInBase2 += pow(10, index);
        }

        number = number / 2;
        index++;
    }


    return numberInBase2;
}

vector<string> split(char sentence[]) {
    vector<string> words;
    string word = "";
    char space = ' ';
    int size = sizeof(sentence) / sizeof(char);

    for (int i = 0; i < min(size, 100); i++) {
        if (sentence[i] ==  space) {
          words.push_back(word);
          word = "";
        }
        else {
            word += sentence[i];
        }
    }
    words.push_back(word);

    return words;
}

void printWords(vector<string> words) {

    for (int index = 0; index < words.size(); ++index) {
        cout << words[index] << endl;
    }
}



#endif // HOMEWORK1_H_INCLUDED
