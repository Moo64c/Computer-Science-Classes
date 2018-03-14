/*
 * WhatsappMessangerConnectionHandler.h
 *
 *  Created on: 9 ���� 2015
 *      Author: yotam
 *  based on http://www.cs.bgu.ac.il/~spl151/PracticalSession09/Boost_Echo_Client
 */

#ifndef INCLUDE_WHATSAPPMESSANGERCONNECTIONHANDLER_H_
#define INCLUDE_WHATSAPPMESSANGERCONNECTIONHANDLER_H_

#include <string>
#include <iostream>
#include <boost/asio.hpp>
#include <vector>
#include <cstdarg>

#include "Message.h"

using boost::asio::ip::tcp;
using namespace std;

class WhatsappMessangerConnectionHandler {

	static const string COOKIES, SET_COOKIE, USER_NAME,
			PHONE, GROUP_NAME, TYPE, TARGET,
			GROUP_TYPE, USER_TYPE, DIRECT_TYPE,
			GROUPS_TYPE, LIST, CONTENT, USER;

	static const string QUEUE_URI,
			LOGIN_URI,
			LOGOUT_URI,
			LIST_URI,
			SEND_URI,
			ADD_URI,
			REMOVE_URI,
			CREATE_GROUP_URI;

	static const string MESSAGE_BODY_PARAMETER_SEPARATOR,
			MESSAGE_BODY_KEY_VALUE_SEPARATOR,
			MESSAGE_BODY_VALUES_SEPARATOR;

	static const char MESSAGE_DELIMITER;

public:
	WhatsappMessangerConnectionHandler(string host, short port, string * cookieString);
	virtual ~WhatsappMessangerConnectionHandler();

	vector<Message*> readAwaitingMessages();

	WhatsappMessangerConnectionHandler(const WhatsappMessangerConnectionHandler &other);

	WhatsappMessangerConnectionHandler& operator= (const WhatsappMessangerConnectionHandler &other);



	// Close down the connection properly.
    void close();

    //requests incoming messages.
    bool sendQueueRequest();

    //logs in
    bool sendLoginRequest(const string &userName, const string &phone);

    //logs out
    bool sendlogoutRequest();

    //requests full user list request.
    bool sendListUsersRequest();

    // request group list request.
    bool sendListGroupsRequest();

    // request list of users in specific group.
    bool sendListGroupRequest(const string &groupName);

    // send a message to user.
    bool sendDirectMessageRequest(const string &groupName, const string &stringMessage);

    // send a message group.
    bool sendGroupMessageRequest(const string &groupName, const string &stringMessage);

    // add a users to a group
    bool sendAddRequest(const string &groupName, const string &phone);

    // removes a user from a group
    bool sendRemoveRequest(const string &groupName, const string &phone);

    // creates a new group.
    bool sendCreateGroupMessageRequest(const string &groupName, const vector<string> &users);




private:
    const string host_;
    const short port_;
    boost::asio::io_service io_service_;   // Provides core I/O functionality
    tcp::socket socket_;
    string frame;
    string * _cookie;


    // Connect to the remote machine
    bool connect();

    // Read a fixed number of bytes from the server - blocking.
    // Returns false in case the connection is closed before bytesToRead bytes can be read.
    bool getBytes(char bytes[], unsigned int bytesToRead);

    // Send a fixed number of bytes from the client - blocking.
    // Returns false in case the connection is closed before all the data is sent.
    bool sendBytes(const char bytes[], int bytesToWrite);

    // Read an ascii line from the server
    // Returns false in case connection closed before a newline can be read.
    Message* getMessage();


    // Get Ascii data from the server until the delimiter character
    // Returns false in case connection closed before null can be read.
    Message* getFrameAscii(char delimiter);

    // Send a message to the remote host.
    // Returns false in case connection is closed before all the data is sent.
    bool sendFrameAscii(const Message& message, char delimiter);

    // Send an ascii line from the server
    // Returns false in case connection closed before all the data is sent.
    bool sendMessage(const Message& message);

    //creates message body from string parameters in format <param1>=<param2>&<param3>=...
    string createBodyMessage(int numOfArgs, ...);

};

#endif /* INCLUDE_WHATSAPPMESSANGERCONNECTIONHANDLER_H_ */
