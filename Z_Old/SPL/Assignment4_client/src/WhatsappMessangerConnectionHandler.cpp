/*
 * WhatsappMessangerConnection.cpp
 *
 *  Created on: 9 ���� 2015
 *      Author: yotam
 */

#include "WhatsappMessangerConnectionHandler.h"
#include <map>
#include <cctype>
#include <iomanip>
#include <sstream>
#include <string>
#include <stdio.h>
#include <iostream>

//#include "urlencode.cpp"

using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

const string WhatsappMessangerConnectionHandler::COOKIES("Cookie"),
		WhatsappMessangerConnectionHandler::SET_COOKIE("Set-Cookie"),
		WhatsappMessangerConnectionHandler::USER_NAME("UserName"),
		WhatsappMessangerConnectionHandler::PHONE("Phone"),
		WhatsappMessangerConnectionHandler::GROUP_NAME("GroupName"),
		WhatsappMessangerConnectionHandler::TYPE("Type"),
		WhatsappMessangerConnectionHandler::TARGET("Target"),
		WhatsappMessangerConnectionHandler::GROUP_TYPE("Group"),
		WhatsappMessangerConnectionHandler::USER_TYPE("Users"),
		WhatsappMessangerConnectionHandler::DIRECT_TYPE("Direct"),
		WhatsappMessangerConnectionHandler::GROUPS_TYPE("Groups"),
		WhatsappMessangerConnectionHandler::LIST("List"),
		WhatsappMessangerConnectionHandler::CONTENT("Content"),
		WhatsappMessangerConnectionHandler::USER("User");

const string WhatsappMessangerConnectionHandler::QUEUE_URI("/queue.jsp"),
		WhatsappMessangerConnectionHandler::LOGIN_URI("/login.jsp"),
		WhatsappMessangerConnectionHandler::LOGOUT_URI("/logout.jsp"),
		WhatsappMessangerConnectionHandler::LIST_URI("/list.jsp"),
		WhatsappMessangerConnectionHandler::SEND_URI("/send.jsp"),
		WhatsappMessangerConnectionHandler::ADD_URI("/add_user.jsp"),
		WhatsappMessangerConnectionHandler::REMOVE_URI("/remove_user.jsp"),
		WhatsappMessangerConnectionHandler::CREATE_GROUP_URI("/create_group.jsp");

const string WhatsappMessangerConnectionHandler::MESSAGE_BODY_PARAMETER_SEPARATOR("&"),
		WhatsappMessangerConnectionHandler::MESSAGE_BODY_KEY_VALUE_SEPARATOR("="),
		WhatsappMessangerConnectionHandler::MESSAGE_BODY_VALUES_SEPARATOR(",");

const char WhatsappMessangerConnectionHandler::MESSAGE_DELIMITER = '$';

static string url_encode(const string &value) {
    ostringstream escaped;
    escaped.fill('0');
    escaped << hex;

    for (string::const_iterator i = value.begin(), n = value.end(); i != n; ++i) {
        string::value_type c = (*i);

        // Keep alphanumeric and other accepted characters intact
        if (isalnum(c) || c == '-' || c == '_' || c == '.' || c == '~') {
            escaped << c;
            continue;
        }

        // Any other characters are percent-encoded
        escaped << '%' << setw(2) << int((unsigned char) c);
    }

    return escaped.str();
}




WhatsappMessangerConnectionHandler::WhatsappMessangerConnectionHandler(string host, short port, string* cookieString): host_(host), port_(port), io_service_(), socket_(io_service_), frame(), _cookie(cookieString){
	connect();
}


WhatsappMessangerConnectionHandler::~WhatsappMessangerConnectionHandler() {
	close();
}

WhatsappMessangerConnectionHandler::WhatsappMessangerConnectionHandler(const WhatsappMessangerConnectionHandler &other)
:host_(other.host_), port_(other.port_), io_service_(), socket_(io_service_), frame(), _cookie(other._cookie)
{
	connect();
}

WhatsappMessangerConnectionHandler& WhatsappMessangerConnectionHandler::operator= (const WhatsappMessangerConnectionHandler &other){
	return (*this);
}

vector<Message*> WhatsappMessangerConnectionHandler::readAwaitingMessages(){
	//check if there is data in the socket, so the method won't block.
	vector<Message*> res;
	if (socket_.available() == 0) {
		return res;
	}

	else {
		while (socket_.available() > 0){
			Message* message = getMessage();
			if (message != 0)
				res.push_back(message);
		}
		return res;
	}
}


bool WhatsappMessangerConnectionHandler::connect() {
    std::cout << "Starting connect to "
        << host_ << ":" << port_ << std::endl;
    try {
        tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
        boost::system::error_code error;
        socket_.connect(endpoint, error);
        if (error)
            throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool WhatsappMessangerConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
            tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);
        }
        if(error)
            throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool WhatsappMessangerConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
            tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
        if(error)
            throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

Message* WhatsappMessangerConnectionHandler::getMessage() {
    return getFrameAscii(MESSAGE_DELIMITER);
}

bool WhatsappMessangerConnectionHandler::sendMessage(const Message& message){\
//	cout << "outgoing message:\n" << message.c_str() << endl << flush;
    return sendFrameAscii(message, MESSAGE_DELIMITER);
}

Message* WhatsappMessangerConnectionHandler::getFrameAscii(char delimiter) {
    char ch;
    // Check if there is more data to read.
    // Stop when we encounter the 0 character.
    // Notice that the 0 character is not appended to the frame string.
    if (socket_.available() > 0){
        do{
            if(!getBytes(&ch, 1))
            {
                return 0;
            }
            frame.append(1, ch);
        }while (socket_.available() > 0 && delimiter != ch);
        if (ch == delimiter){
        	if (frame.length() > 1){
        		Message* msg = new Message(frame, _cookie);
        		frame.clear();
        		return msg;
        	}
        	else {
        		frame.clear();
        		return 0;
        	}
        }
    }
    else {
    	return 0;
    }
    return 0;

}

bool WhatsappMessangerConnectionHandler::sendFrameAscii(const Message& message, char delimiter){
    bool result = sendBytes(message.c_str(),message.length());
    if(!result) return false;
    return sendBytes(&delimiter,1);
}

// Close down the connection properly.
void WhatsappMessangerConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}

bool WhatsappMessangerConnectionHandler::sendQueueRequest(){
	string messageBody;
	map<string, string> headers;
	headers.insert(pair<string, string>(COOKIES, (*_cookie)) );
	Message::REQUEST_TYPE requestType = Message::GET;
	string requestURI(QUEUE_URI);

	Message message(messageBody, headers, requestType, requestURI);

	return sendMessage(message);
}

bool WhatsappMessangerConnectionHandler::sendLoginRequest(const string &userName, const string &phone){
	string messageBody;
	messageBody.append(USER_NAME);
	messageBody.append(MESSAGE_BODY_KEY_VALUE_SEPARATOR);
	messageBody.append(url_encode(userName));
	messageBody.append(MESSAGE_BODY_PARAMETER_SEPARATOR);
	messageBody.append(PHONE);
	messageBody.append(MESSAGE_BODY_KEY_VALUE_SEPARATOR);
	messageBody.append(url_encode(phone));

	map<string, string> headers;

	Message::REQUEST_TYPE requestType = Message::POST;
	string requestURI(LOGIN_URI);

	Message message(messageBody, headers, requestType, requestURI);

	return sendMessage(message);

}


bool WhatsappMessangerConnectionHandler::sendlogoutRequest(){
	string messageBody;
	map<string, string> headers;
	headers.insert(pair<string, string>(COOKIES, (*_cookie)) );
	Message::REQUEST_TYPE requestType = Message::GET;
	string requestURI(LOGOUT_URI);

	Message message(messageBody, headers, requestType, requestURI);


	return sendMessage(message);

}


bool WhatsappMessangerConnectionHandler::sendListUsersRequest(){
	string messageBody;
	messageBody.append(LIST);
	messageBody.append(MESSAGE_BODY_KEY_VALUE_SEPARATOR);
	messageBody.append(USER_TYPE);

	map<string, string> headers;
	headers.insert(pair<string, string>(COOKIES, (*_cookie)) );
	Message::REQUEST_TYPE requestType = Message::POST;
	string requestURI(LIST_URI);

	Message message(messageBody, headers, requestType, requestURI);

	return sendMessage(message);

}


bool WhatsappMessangerConnectionHandler::sendListGroupsRequest(){
	string messageBody;
	messageBody.append(LIST);
	messageBody.append(MESSAGE_BODY_KEY_VALUE_SEPARATOR);
	messageBody.append(GROUPS_TYPE);

	map<string, string> headers;
	headers.insert(pair<string, string>(COOKIES, (*_cookie)) );
	Message::REQUEST_TYPE requestType = Message::POST;
	string requestURI(LIST_URI);

	Message message(messageBody, headers, requestType, requestURI);

	return sendMessage(message);

}

bool WhatsappMessangerConnectionHandler::sendCreateGroupMessageRequest(const string &groupName, const vector<string> &users){
	string messageBody;
	messageBody.append(GROUP_NAME);
	messageBody.append(MESSAGE_BODY_KEY_VALUE_SEPARATOR);
	messageBody.append(url_encode(groupName));
	messageBody.append(MESSAGE_BODY_PARAMETER_SEPARATOR);
	messageBody.append(USER_TYPE);
	messageBody.append(MESSAGE_BODY_KEY_VALUE_SEPARATOR);

	for (int i = 0; i < (int) users.size(); i++){
		messageBody.append(url_encode(users.at(i)));
		if (i != (int) users.size() - 1){
			messageBody.append(MESSAGE_BODY_VALUES_SEPARATOR);
		}
	}

	map<string, string> headers;
	headers.insert(pair<string, string>(COOKIES, (*_cookie)) );
	Message::REQUEST_TYPE requestType = Message::POST;
	string requestURI(CREATE_GROUP_URI);

	Message message(messageBody, headers, requestType, requestURI);

	return sendMessage(message);
}


bool WhatsappMessangerConnectionHandler::sendListGroupRequest(const string &groupName){
	string messageBody;
	messageBody.append(LIST);
	messageBody.append(MESSAGE_BODY_KEY_VALUE_SEPARATOR);
	messageBody.append(GROUPS_TYPE);
	messageBody.append(MESSAGE_BODY_PARAMETER_SEPARATOR);
	messageBody.append(GROUP_NAME);
	messageBody.append(MESSAGE_BODY_KEY_VALUE_SEPARATOR);
	messageBody.append(url_encode(groupName));


	map<string, string> headers;
	headers.insert(pair<string, string>(COOKIES, (*_cookie)) );
	Message::REQUEST_TYPE requestType = Message::POST;
	string requestURI(LIST_URI);

	Message message(messageBody, headers, requestType, requestURI);

	return sendMessage(message);
}

//TODO
bool WhatsappMessangerConnectionHandler::sendDirectMessageRequest(const string &groupName, const string &stringMessage){
	string messageBody;
	messageBody.append(TYPE);
	messageBody.append(MESSAGE_BODY_KEY_VALUE_SEPARATOR);
	messageBody.append(DIRECT_TYPE);
	messageBody.append(MESSAGE_BODY_PARAMETER_SEPARATOR);
	messageBody.append(TARGET);
	messageBody.append(MESSAGE_BODY_KEY_VALUE_SEPARATOR);
	messageBody.append(url_encode(groupName));
	messageBody.append(MESSAGE_BODY_PARAMETER_SEPARATOR);
	messageBody.append(CONTENT);
	messageBody.append(MESSAGE_BODY_KEY_VALUE_SEPARATOR);
	messageBody.append(url_encode(stringMessage));


	map<string, string> headers;
	headers.insert(pair<string, string>(COOKIES, (*_cookie)) );
	Message::REQUEST_TYPE requestType = Message::POST;
	string requestURI(SEND_URI);

	Message message(messageBody, headers, requestType, requestURI);


	return sendMessage(message);

}

bool WhatsappMessangerConnectionHandler::sendGroupMessageRequest(const string &groupName, const string &stringMessage){
	string messageBody;
	messageBody.append(TYPE);
	messageBody.append(MESSAGE_BODY_KEY_VALUE_SEPARATOR);
	messageBody.append(GROUP_TYPE);
	messageBody.append(MESSAGE_BODY_PARAMETER_SEPARATOR);
	messageBody.append(TARGET);
	messageBody.append(MESSAGE_BODY_KEY_VALUE_SEPARATOR);
	messageBody.append(url_encode(groupName));
	messageBody.append(MESSAGE_BODY_PARAMETER_SEPARATOR);
	messageBody.append(CONTENT);
	messageBody.append(MESSAGE_BODY_KEY_VALUE_SEPARATOR);
	messageBody.append(url_encode(stringMessage));

	map<string, string> headers;
	headers.insert(pair<string, string>(COOKIES, (*_cookie)) );
	Message::REQUEST_TYPE requestType = Message::POST;
	string requestURI(SEND_URI);

	Message message(messageBody, headers, requestType, requestURI);


	return sendMessage(message);

}


bool WhatsappMessangerConnectionHandler::sendAddRequest(const string &groupName, const string &phone){
	string messageBody;
	messageBody.append(TARGET);
	messageBody.append(MESSAGE_BODY_KEY_VALUE_SEPARATOR);
	messageBody.append(url_encode(groupName));
	messageBody.append(MESSAGE_BODY_PARAMETER_SEPARATOR);
	messageBody.append(USER);
	messageBody.append(MESSAGE_BODY_KEY_VALUE_SEPARATOR);
	messageBody.append(url_encode(phone));

	map<string, string> headers;
	headers.insert(pair<string, string>(COOKIES, (*_cookie)) );
	Message::REQUEST_TYPE requestType = Message::POST;
	string requestURI(ADD_URI);

	Message message(messageBody, headers, requestType, requestURI);


	return sendMessage(message);

}

//TODO
bool WhatsappMessangerConnectionHandler::sendRemoveRequest(const string &groupName, const string &phone){
	string messageBody;
	messageBody.append(TARGET);
	messageBody.append(MESSAGE_BODY_KEY_VALUE_SEPARATOR);
	messageBody.append(url_encode(groupName));
	messageBody.append(MESSAGE_BODY_PARAMETER_SEPARATOR);
	messageBody.append(USER);
	messageBody.append(MESSAGE_BODY_KEY_VALUE_SEPARATOR);
	messageBody.append(url_encode(phone));

	map<string, string> headers;
	headers.insert(pair<string, string>(COOKIES, (*_cookie)) );
	Message::REQUEST_TYPE requestType = Message::POST;
	string requestURI(REMOVE_URI);

	Message message(messageBody, headers, requestType, requestURI);


	return sendMessage(message);

}





