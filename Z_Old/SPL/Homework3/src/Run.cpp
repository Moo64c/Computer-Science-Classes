#include "../include/LinkedList.h"
#include <string>

using namespace std;

int main(int argc, char *argv[]) {
	const	string foo = "foo";
	List *list1 = new List;
	list1->insertData(foo);
	List *list2 = new List(*list1);
	*list2 = *list1;
	delete list1;
			
	return 0;

}




