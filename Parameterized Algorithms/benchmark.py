import time

class benchmarker:
    def __init__(self, name):
        self.module_name = name
        self.benchmarks = [];
        self.add("start");

    def add(self, name):
        self.benchmarks.append({
            "name": name,
            "time": time.time(),
        });

    def display(self):
        # Print benchmarks array.
        benchmarks = self.benchmarks;
        print("Processing graph " + self.module_name + " was done in: " + str((benchmarks[len(benchmarks) - 1]["time"] - benchmarks[0]["time"])) + " seconds.");
        for index, benchmark in enumerate(benchmarks):
            if (index == 0):
                continue;

            # Show difference from last step.
            last_benchmark = benchmarks[index - 1];
            print(" - " + benchmark["name"] + ": " + str(benchmark["time"] - last_benchmark["time"]) + " seconds");
