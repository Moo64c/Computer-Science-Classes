import networkx as nx
import networkx.readwrite.gml as gml
import os

graphs = [
    {
        "name": "test_graph",
        "random": 0.1,
        "size": 20
    },
    {
        "random": 0.01,
        "size": 200
    },
    {
        "random": 0.05,
        "size": 100
    },
    {
        "random": 0.02,
        "size": 500
    },
    {
        "random": 0.1,
        "size": 1000
    },
    {
        "random": 0.5,
        "size": 1000
    }
];

# Clean old files.
dir_name = "/home/amir/Studies/Computer-Science-Classes/Parameterized Algorithms";
deleted = 0;
items = os.listdir(dir_name);
for item in items:
    if item.endswith(".gml"):
        os.remove(os.path.join(dir_name, item));
        deleted += 1;
if (deleted > 0):
    print("Deleted " + str(deleted) + " old graph files.");

print("Generating " + str(len(graphs)) + " graphs:");

for graph in graphs:
    name = "" + str(graph["size"]) + "_" + str(graph["random"]);
    if ("name" in graph):
        name = graph["name"];
    print("Generating graph " + name + "...");
    G = nx.fast_gnp_random_graph(graph["size"], graph["random"]);
    gml.write_gml(G, name + ".gml");
    print("Graph generation done, saved as " + name + ".gml");

print "Done!";
exit();
