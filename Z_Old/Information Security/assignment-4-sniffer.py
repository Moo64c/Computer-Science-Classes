import socket

#s = socket.socket(socket.AF_INET, socket.SOCK_RAW, socket.IPPROTO_TCP);

#while True:
#	print s.recvfrom(65565);

from scapy.all import *


def displayPacket(x):
	str  = "\n==================================== \n";
	# Generic stuff.
	str += x.sprintf("Type:{TCP: TCP}{UDP: UDP}{ICMP: ICMP}  || Length: %IP.len%\n");
	str += x.sprintf("{Ether:%Ether.src% -> %Ether.dst%}\n");
	str += x.sprintf("{IP:%IP.src% -> %IP.dst%\n}");

	packetType = x.sprintf("{TCP:TCP}{UDP:UDP}{ICMP:ICMP}");
	if (packetType == "TCP" or packetType == "UDP"):
		# Add port number.
		str += x.sprintf("Port: %{TCP:TCP}{UDP:UDP}.sport% -> %{TCP:TCP}{UDP:UDP}.dport%\n");
		# Add flags.
		str += x.sprintf("Flags: {TCP:%TCP.flags%}{UDP:%UDP.flags%}\n");
		# Payload.
		payload = bytes(x.sprintf("{Raw:%Raw.load%}"))
		str += "Payload(ASCII):\n" + payload.decode("ascii");
	# ICMP
	str += x.sprintf("{ICMP:ICMP type: %ICMP.type% code: %ICMP.code%\n}")
	str +=  "\n==================================== \n";
	print str;


sniff(iface="enp0s3", prn=displayPacket)

