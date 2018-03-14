/*
 * FlowManagement.h
 *
 *  Created on: Jan 15, 2015
 *      Author: yotamd
 */

#ifndef FLOWMANAGEMENT_H_
#define FLOWMANAGEMENT_H_

#include <string>
#include "WhatsappMessangerConnectionHandler.h"
#include <boost/thread.hpp>
#include <boost/date_time.hpp>

using namespace std;

class FlowManagement {

public:
	static const string USAGE, NEXT_REQUEST, LOGIN_MESSAGE, WRONG_LOGIN;
	static const string LOGOUT_STRING,
			LIST_USERS_STRING,
			LIST_GROUPS_STRING,
			LIST_GROUP_STRING,
			ADD_STRING,
			REMOVE_STRING,
			SEND_USER_STRING,
			SEND_GROUP_STRING,
			CREATE_GROUP_STRING;
	static const string USER_INPUT_DELIMITER;

	FlowManagement();
	virtual ~FlowManagement();

	WhatsappMessangerConnectionHandler *login(const string &host, short port, string* cookieString);

	void loop(WhatsappMessangerConnectionHandler* connection, boost::mutex* lock);

private:
	bool _notLoggedOut;

	void parseAndSendRequest(const string &requestString, WhatsappMessangerConnectionHandler* connection, boost::mutex* lock);
	void printUsage();
};

#endif /* FLOWMANAGEMENT_H_ */
