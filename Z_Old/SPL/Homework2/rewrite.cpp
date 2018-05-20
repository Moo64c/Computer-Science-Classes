#include <stdio.h>
 
 
int *pPointer;
int nNumber; // Moving nNumber from the stack to the heap.
 
void SomeFunction()
{
    nNumber = 25;    
 
    // make pPointer point to nNumber:
    pPointer = &nNumber;
}
 
void main()
{
    SomeFunction(); // make pPointer point to something
 
    cout<< "Value of *pPointer: "<< *pPointer <<endl;
}
