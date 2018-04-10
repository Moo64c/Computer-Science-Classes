#########
# @file ByzClock.py
#  Assignment 1 for Network Security
#  Made by Amir Arbel
# PYTHON 2.7
#########

# Multithreading!
from multiprocessing import Process

# Settings for the algorithm.
# Timeout
clocktimeout = 2;
# "M" in the psuedo-code
clock_bound = 3;
# Number of clocks... "n"
number_of_processes = 20;
# "f"
faulty_nodes = 0;
# Clock list.
clocks = [];
# List of queues for each clock.
queues = [];


# Simple object to hold the clock with id.
class ByzClock(object):
    def __init__(self, id):
        self.id = id;
        self.reset();
        self.lastIncrement = false;

    def set(self, clock):
        self.clock = clock;

    def setRandom(self):
        result = random.randint(0,1);
        self.set(result);
        if (result == 1):
            lastIncrement = true;
        else
            lastIncrement == false;


    #The action – (increment clock value by 1) mod M
    def increment(self):
        self.clock = addModM(self.clock, 1);
        self.lastIncrement = true;

    # The action – set Pi’s clock value to 0
    def reset(self):
        self.clock = 0;
        self.lastIncrement = false;

    def get(self):
        return self.clock;

    def getId(self):
        return self.id;

    def threadAction(self):
        for index in count (0, number_of_processes):
            # push the clock value to the queue.
            queues[index].put({'id' :self.getId(), 'clock' : self.get()});
            
        for index in count (0, number_of_processes):
            # Wait for a clock value.
            item = queues[index].get(true, clocktimeout);
            if (item['clock'] == self.get() && index < number_of_processes - f - 1):
                # Reset – fewer than n-f-1 are found
                self.reset();
            else:
                #Increment – P_i finds n-f-1 clock values identical to its own
                if (self.get() != 0):
                    self.increment();
                else:
                    if (self.lastIncrement):
                        self.set(1);
                    else:
                        self.setRandom();

    def run():
        th = threading.Thread(target=self.threadAction);
        th.start();


def addModM(value1, value2):
    return (value1 + value2) % clock_bound;

if __name__ == '__main__':
    # Init queues and clocks.
    for index in range(0, number_of_processes):
        clocks[index] = ByzClock(index);
        queues[index] = Queue();
        clocks[index].run();
