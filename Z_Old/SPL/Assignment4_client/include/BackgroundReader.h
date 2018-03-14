/*
 * BackgroundReader.h
 *
 *  Created on: Jan 13, 2015
 *      Author: yotamd
 */

#ifndef BACKGROUNDREADER_H_
#define BACKGROUNDREADER_H_

#include <vector>
#include <boost/thread.hpp>
#include <boost/date_time.hpp>
#include "WhatsappMessangerConnectionHandler.h"
#include "Message.h"


class BackgroundReader {
public:
	BackgroundReader(WhatsappMessangerConnectionHandler* connection, boost::mutex* lock, string * cookieString);
	virtual ~BackgroundReader();
	static const boost::posix_time::seconds SLEEP_TIME;

	// the task that should run
	// see @link http://www.boost.org/doc/libs/1_54_0/doc/html/thread/synchronization.html
	void operator()();

	BackgroundReader(const BackgroundReader &other);

	BackgroundReader& operator=(const BackgroundReader &other);


private:
	WhatsappMessangerConnectionHandler* _connection;
	boost::mutex* _connectionLock;
	bool _queueMessageSent;
	string * _cookieString;

};

#endif /* BACKGROUNDREADER_H_ */
