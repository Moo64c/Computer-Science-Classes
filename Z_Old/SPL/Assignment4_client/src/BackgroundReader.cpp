/*
 * BackgroundReader.cpp
 *
 *  Created on: Jan 13, 2015
 *      Author: yotamd
 */

#include "BackgroundReader.h"

const boost::posix_time::seconds BackgroundReader::SLEEP_TIME(2);

BackgroundReader::BackgroundReader(WhatsappMessangerConnectionHandler* connection, boost::mutex* connectionLock, string * cookieString):
_connection(connection), _connectionLock(connectionLock), _queueMessageSent(false), _cookieString(cookieString){
	cout << "started reader" << endl << flush;
}

BackgroundReader::BackgroundReader(const BackgroundReader &other):
_connection(other._connection), _connectionLock(other._connectionLock), _queueMessageSent(other._queueMessageSent), _cookieString(other._cookieString)
{}

BackgroundReader& BackgroundReader::operator=(const BackgroundReader &other)
{
	return (*this);
}

BackgroundReader::~BackgroundReader() {

}
const string EMPTY_MESSAGE("\r\n$"), EMPTY_MESSAGE2("$");

void BackgroundReader::operator()() {
	while (_connection != 0){ // TODO consider finding a better way to check if look should continue.
		_connectionLock->lock();
			vector<Message*> messageVector = _connection->readAwaitingMessages();
			// if no incoming messages - send queue message.
			if (messageVector.empty() && !_queueMessageSent && !(_cookieString->empty())){


				_connection->sendQueueRequest();
				_queueMessageSent = true;
			}
			else if (!messageVector.empty())	_queueMessageSent = false;

		_connectionLock->unlock();

		// print out incoming messages.
		for (int i = 0; i < (int) messageVector.size(); i++){
			if (EMPTY_MESSAGE.compare(messageVector.at(i)->c_str()) != 0 && EMPTY_MESSAGE2.compare(messageVector.at(i)->c_str()) != 0) {
				string noDollarMessage(messageVector.at(i)->c_str());
				noDollarMessage = noDollarMessage.substr(0,messageVector.at(i)->length() - 1);
				cout << "new message: "<<  noDollarMessage << endl;
			}
			delete messageVector.at(i);
		}


		// sleep for two seconds.
		boost::this_thread::sleep(SLEEP_TIME);
	}
}

