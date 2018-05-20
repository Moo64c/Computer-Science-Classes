/*
 * Message.h
 *
 *  Created on: Jan 12, 2015
 *      Author: yotamd
 */

#ifndef MESSAGE_H_
#define MESSAGE_H_


using namespace std;

#include <string>
#include <map>
#include <vector>


class Message {
public:

	enum REQUEST_TYPE { GET, POST, INVALID };
	enum HTTP_MESSAGE_TYPE { REQUEST, RESPONSE };
	enum STATUS { REQUEST_STATUS = 0, OK = 200, FORBIDDEN = 403, NOT_FOUND = 404, NOT_ALLOWED_METHOD = 405, I_AM_A_TEAPOT = 418 };

	static const string MESSAGE_403, MESSAGE_404, MESSAGE_405, MESSAGE_418;
	static const string GET_STRING, POST_STRING, INVALID_STRING;
	static const string HTTP_VERSION;
	static const string HEADER_BODY_SEPARATOR,
			HEADER_KEY_VALUE_SEPARATOR,
			HEADER_TO_HEADER_SEPARATOR,
			META_DATA_SEPARATOR;


	// parses a response message from string.
	Message(const string& stringMessage, string* cookie);

	//creates a request message from specific parameters.
	Message(const string& messageBody, const map<string, string>& headers, REQUEST_TYPE requestType, const string& requestURI);

	virtual ~Message();

	// return a string representation of the message to be sent.
	const char * c_str() const;

	// return the number of chars in message.
	int length() const;

	// splits a string according to delimiter.
	static vector<string> splitString(string heap, string needle);


private:
	string _messageBody;
	map<string, string> _headers;
	REQUEST_TYPE _requestType;
	string _requestURI;
	STATUS _status;
	HTTP_MESSAGE_TYPE _type;

	// parses message body of response and inserts it to _messageBody parameter.
	void parseMessageBody(const string& _rawString);

	// parses cookie of response and inserts it to cookie string pointer.
	void parseCookie(const string& _rawString, string* cookie) const;

	// parses message body of response and inserts it to _status parameter.
	void parseStatus(const string& _rawString);

	//Auxiliary method that converts parameters to string.
	string getStringFromParameters() const;


	//returns string representation of message.
	string getString() const;

};



#endif /* MESSAGE_H_ */
