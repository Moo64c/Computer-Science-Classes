/*
 * main.cpp
 *
 *  Created on: 9 ���� 2015
 *      Author: yotam
 */



using namespace std;
#include <iostream>
//#include "urlencode.cpp"
#include "FlowManagement.h"
#include "WhatsappMessangerConnectionHandler.h"
#include "BackgroundReader.h"
#include <boost/thread.hpp>
#include <boost/date_time.hpp>
#include <stdlib.h>

void testing(){
	cout<< "another thread!!!"<< endl;
	boost::posix_time::seconds workTime(5);
	boost::this_thread::sleep(workTime);
}

int main(int argc, char *argv[])
{
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }

    std::string host = argv[1];
    unsigned short port = atoi(argv[2]);

    string *cookieString = new string();
    boost::mutex* lock = new boost::mutex();

    FlowManagement flow;
    WhatsappMessangerConnectionHandler* connection = flow.login(host, port, cookieString);

    boost::thread reader(BackgroundReader(connection, lock, cookieString));

    flow.loop(connection, lock);

	delete cookieString;
	delete lock;
	delete connection;
	return 0;
}
