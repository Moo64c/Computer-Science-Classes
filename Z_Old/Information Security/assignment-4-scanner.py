from scapy.all import *
from itertools import groupby, count

import sys
import threading
import time


Filtered = []
Open = []
Closed = []
tt =[]


def scan(dst_ip,dst_port,src_port):
	stealth_scan_resp = sr1(IP(dst=dst_ip)/TCP(sport=src_port,dport=dst_port,flags="S"),timeout=2, verbose=0)
	time.sleep(3)                                     
	if(str(type(stealth_scan_resp))=="<type 'NoneType'>"):
		Filtered.append(dst_port)
	elif(stealth_scan_resp.haslayer(TCP)):
		if(stealth_scan_resp.getlayer(TCP).flags == 0x12):
			send_rst = sr(IP(dst=dst_ip)/TCP(sport=src_port,dport=dst_port,flags="R"),timeout=2, verbose=0)
			Open.append(dst_port)
	elif (stealth_scan_resp.getlayer(TCP).flags == 0x14):
		Closed.append(dst_port)
	elif(stealth_scan_resp.haslayer(ICMP)):
		if(int(stealth_scan_resp.getlayer(ICMP).type)==3 and int(stealth_scan_resp.getlayer(ICMP).code) in [1,2,3,9,10,13]):
			Filtered.append(src_port)

     

def format_range(str):
	# Comma seperated list.
	if (str.find(",") != -1):
		return str.split(",")
	# Range
	elif (str.find("-") != -1):
		genius = str.split("-");
		return range(int(genius[0]),int(genius[1]));
	else:
		return int(str)


def make_ranges(numberlist):
    ranges=set()
    for number in numberlist:
        adjacent_to = set( r for r in ranges if r[0]-1 == number or r[1] == number )
        if len(adjacent_to) == 0:
            # Add a new partition.
            r = (number,number+1)
            assert r[0] <= number < r[1] # Trivial, right?
            ranges.add( r )
        elif len(adjacent_to) == 1:
            # Extend this partition, build a new range.
            ranges.difference_update( adjacent_to )
            r= adjacent_to.pop()
            if r[0]-1 == number: # Extend low end
                r= (number,r[1])
            else:
                r= (r[0],number+1) # Extend high end
            assert r[0] <= number < r[1] 
            ranges.add( r )
        elif len(adjacent_to) == 2:
            # Merge two adjacent partitions.
            ranges.difference_update( adjacent_to )
            r0= adjacent_to.pop()
            r1= adjacent_to.pop()
            r = ( min(r0[0],r1[0]), max(r0[1],r1[1]) )
            assert r[0] <= number < r[1]
            ranges.add( r )
    return ranges


def formatpagelist (numberlist):
	ranges= make_ranges( numberlist )
	def format_a_range( low, high ):
		if low == high-1: return "{0}".format( low )
		return "{0}-{1}".format( low, high-1 )
	strings = [ format_a_range(min(r), max(r)) for r in sorted(ranges) ]
	return ",".join( strings )


def format_response(lst):
	sorted(lst);
	return formatpagelist(lst)

def as_range(iterable): 
	l = list(iterable)
	if len(l) > 1:
		return '{0}-{1}'.format(l[0], l[-1])
	else:
		return '{0}'.format(l[0])



for x in format_range(sys.argv[2]):
	x = int(x);
	t=threading.Thread(target=scan,kwargs={'dst_ip':str(sys.argv[1]),'src_port':80,'dst_port':x}, name='scan')
	t.start()
	tt.append(t)

for t in tt:
	t.join()


print "\nFiltered: "
print format_response(Filtered)
print "\nOpen: "
print format_response(Open)
print "\nClosed: "
print format_response(Closed)



