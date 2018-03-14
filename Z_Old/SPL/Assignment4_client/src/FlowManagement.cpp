/*
 * FlowManagement.cpp
 *
 *  Created on: Jan 15, 2015
 *      Author: yotamd
 */

#include "FlowManagement.h"
#include <iostream>
#include <string>
#include "Message.h"

using namespace std;

const string FlowManagement::USAGE("support the following commands:\r\n \
Login [User name] [Phone]\r\n \
Logout\r\n \
List Users\r\n \
List Groups\r\n \
List Group [Group Name]\r\n \
Send Group [Group Name] [Message]\r\n \
Send User [Phone] [Message]\r\n \
Add [Group Name] [Phone]\r\n \
Remove [Group Name] [Phone]\r\n \
Create Group [GroupName] [UserName1] [UserName2]... \r\n \
Exit");
const string FlowManagement::NEXT_REQUEST("what would you like to do?");
const string FlowManagement::LOGIN_MESSAGE("input user and phone: ");
const string FlowManagement::WRONG_LOGIN("couldn't understand text. exiting");

const string FlowManagement::LOGOUT_STRING("Logout"),
		FlowManagement::LIST_USERS_STRING("List Users"),
		FlowManagement::LIST_GROUPS_STRING("List Groups"),
		FlowManagement::LIST_GROUP_STRING("List Group"),
		FlowManagement::ADD_STRING("Add"),
		FlowManagement::REMOVE_STRING("Remove"),
		FlowManagement::SEND_USER_STRING("Send User"),
		FlowManagement::SEND_GROUP_STRING("Send Group"),
		FlowManagement::CREATE_GROUP_STRING("Create Group");

const string FlowManagement::USER_INPUT_DELIMITER(" ");

FlowManagement::FlowManagement(): _notLoggedOut(false){


}



WhatsappMessangerConnectionHandler* FlowManagement::login(const string &host, short port, string* cookieString) {
	cout << LOGIN_MESSAGE.c_str() << flush;
	string loginCredencials;
	getline (cin, loginCredencials);
	vector<string> creds = Message::splitString(loginCredencials, " ");
	if (creds.size() < 2){
		cout << WRONG_LOGIN.c_str();
		exit(99);
	}
	else{
		WhatsappMessangerConnectionHandler *connection = new WhatsappMessangerConnectionHandler(host, port, cookieString);
		connection->sendLoginRequest(creds.at(0), creds.at(1));
		_notLoggedOut = true;
		return connection;
	}

}

void FlowManagement::loop(WhatsappMessangerConnectionHandler* connection, boost::mutex* lock){
	while (_notLoggedOut){
		cout << NEXT_REQUEST.c_str() << endl << flush;
		string nextRequest;
		getline (cin, nextRequest);
		parseAndSendRequest(nextRequest, connection, lock);
	}
}

void FlowManagement::parseAndSendRequest(const string &requestString,
		WhatsappMessangerConnectionHandler* connection, boost::mutex* lock){
	vector<string> parsedRequest = Message::splitString(requestString, " ");
	lock->lock();
	if (requestString.find(LOGOUT_STRING) == 0){
		connection->sendlogoutRequest();
		_notLoggedOut = false;
	}
	else if (requestString.find(LIST_USERS_STRING) == 0){
		connection->sendListUsersRequest();
	}
	else if (requestString.find(LIST_GROUPS_STRING) == 0){
		connection->sendListGroupsRequest();
	}
	else if (requestString.find(LIST_GROUP_STRING) == 0 && parsedRequest.size() >= 3){
		connection->sendListGroupRequest(parsedRequest.at(2));
	}
	else if (requestString.find(ADD_STRING) == 0 && parsedRequest.size() >= 3){
		connection->sendAddRequest(parsedRequest.at(1), parsedRequest.at(2));
	}
	else if (requestString.find(REMOVE_STRING) == 0 && parsedRequest.size() >= 3){
			connection->sendRemoveRequest(parsedRequest.at(1), parsedRequest.at(2));
		}
	else if (requestString.find(SEND_USER_STRING) == 0 && parsedRequest.size() >= 4){
			connection->sendDirectMessageRequest(parsedRequest.at(2),
					requestString.substr(1 + requestString.find(USER_INPUT_DELIMITER,requestString.find(USER_INPUT_DELIMITER,requestString.find(USER_INPUT_DELIMITER) + 1) + 1)));
		}
	else if (requestString.find(SEND_GROUP_STRING) == 0 && parsedRequest.size() >= 4){
				connection->sendGroupMessageRequest(parsedRequest.at(2),
						requestString.substr(1 + requestString.find(USER_INPUT_DELIMITER,requestString.find(USER_INPUT_DELIMITER,requestString.find(USER_INPUT_DELIMITER) + 1) + 1)));
	}
	else if (requestString.find(CREATE_GROUP_STRING) == 0 && parsedRequest.size() >= 4){
				string groupName = parsedRequest.at(2);
				parsedRequest.erase(parsedRequest.begin(), parsedRequest.begin()+3);
				connection->sendCreateGroupMessageRequest(groupName, parsedRequest);
		}
	else {
		printUsage();
	}
	lock->unlock();
}


void FlowManagement::printUsage(){
	cout << USAGE.c_str() << endl<< flush;
}


FlowManagement::~FlowManagement() {

}

