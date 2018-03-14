/*
 * Message.cpp
 *
 *  Created on: Jan 12, 2015
 *      Author: yotamd
 */

#include "Message.h"
#include <stdlib.h>
#include <sstream>
#include <string>
#include <stdio.h>
#include <iostream>
#include "urlencode.cpp"


const string Message::MESSAGE_403("REQUEST FORBIDDEN"),
	Message::MESSAGE_404("PAGE NOT FOUND"),
	Message::MESSAGE_405("METHOD NOT ALLOWED"),
	Message::MESSAGE_418("I'M A TEAPOT/ THE MOST NOT FUNNY JOKE EVER");

const string Message::GET_STRING("GET"),
	Message::POST_STRING("POST"),
	Message::INVALID_STRING("INVALID"),
	Message::HTTP_VERSION("HTTP/1.1");

const string Message::HEADER_BODY_SEPARATOR("\r\n\r\n"),
		Message::HEADER_KEY_VALUE_SEPARATOR(": "),
		Message::HEADER_TO_HEADER_SEPARATOR("\r\n"),
		Message::META_DATA_SEPARATOR(" ")
		;


//the rest of the data is not important, and thus set to default values.
Message::Message(const string& _stringMessage, string *cookie):
		_messageBody(), _headers(), _requestType(), _requestURI(), _status(REQUEST_STATUS), _type(RESPONSE) {
	parseMessageBody(_stringMessage);
	parseCookie(_stringMessage, cookie);
	parseStatus(_stringMessage);

}

Message::Message(const string& messageBody, const map<string, string>& headers,
		REQUEST_TYPE requestType, const string& requestURI):
_messageBody(messageBody), _headers(headers), _requestType(requestType), _requestURI(requestURI), _status(REQUEST_STATUS), _type(REQUEST){

}


void Message::parseMessageBody(const string& _rawString){

	//split message from first double line break
	_messageBody = _rawString.substr(_rawString.find(HEADER_BODY_SEPARATOR) + 4);

}

void Message::parseCookie(const string& _rawString, string* cookie) const{

	vector<string> messageLines= splitString(_rawString,HEADER_TO_HEADER_SEPARATOR);
	for (int i = 0; i < (int) messageLines.size(); i++){
		if (messageLines.at(i).find("Set-Cookie: ") == 0){

			//get the part from after "Set-Cookie: "
			(*cookie).clear();
			(*cookie).append(messageLines.at(i).substr(messageLines.at(i).find(": ") + 2));
			break;
		}
	}
}

void Message::parseStatus(const string& _rawString){ //throws error if not valid request status.
	_status = (STATUS) atoi(_rawString.substr(_rawString.find(" ")+1, 3).c_str());
}



Message::~Message() { }

string Message::getStringFromParameters() const{
	string ans;
	switch (_requestType){
	case GET:
		ans.append(GET_STRING);
		break;
	case POST:
		ans.append(POST_STRING);
		break;
	case INVALID:
		ans.append(INVALID_STRING);
		break;
	}
	ans.append(META_DATA_SEPARATOR);
	ans.append(_requestURI);
	ans.append(META_DATA_SEPARATOR);
	ans.append(HTTP_VERSION);
	for (map<string, string>::const_iterator it = _headers.begin(); it != _headers.end(); it++){
		ans.append(HEADER_TO_HEADER_SEPARATOR);
		ans.append(it->first);
		ans.append(HEADER_KEY_VALUE_SEPARATOR);
		ans.append(it->second);
	}
	ans.append(HEADER_BODY_SEPARATOR);
	ans.append(_messageBody);
	return ans;
}


const char * Message::c_str() const{
	return getString().c_str();
}

// return the number of chars in message.
int Message::length() const{
	return getString().length();
}

string Message::getString() const {
	switch(_status){
	case REQUEST_STATUS:
		return this->getStringFromParameters();
		break;
	case OK:
		return _messageBody;
		break;
	case FORBIDDEN:
		return MESSAGE_403;
		break;
	case NOT_FOUND:
		return MESSAGE_404;
		break;
	case NOT_ALLOWED_METHOD:
		return MESSAGE_405;
		break;
	case I_AM_A_TEAPOT:
		return MESSAGE_418;
		break;
	}
	return 0;
}

vector<string> Message::splitString(string heap, string needle) {
	int start = 0;
	int foundAt = 0;
	vector<string> strings;
	string found;
	while (heap.find(needle, start) != string::npos) {
		foundAt = heap.find(needle, start);
		found = heap.substr(start, foundAt - start);
		strings.push_back(found);
		start = foundAt+needle.length();
	}

  if (start != (int) heap.size()) {
		// Add last word.
		found = heap.substr(start);
		strings.push_back(found);
	}

	return strings;
}
