import networkx as nx
import networkx.readwrite.gml as gml
import os

graphs = [
    {
        "name": "test_graph",
        "random": 0.2,
        "size": 20
    },
    {
        "name": "g1k_1",
        "random": 0.1,
        "size": 1000
    },
    {
        "name": "g1k_2",
        "random": 0.2,
        "size": 1000
    },
    {
        "name": "g1k_3",
        "random": 0.3,
        "size": 1000
    },
    {
        "name": "g1k_4",
        "random": 0.4,
        "size": 1000
    },
    {
        "name": "g1k_5",
        "random": 0.5,
        "size": 1000
    },

    {
        "name": "g5k_1",
        "random": 0.1,
        "size": 5000
    },
    {
        "name": "g5k_2",
        "random": 0.2,
        "size": 5000
    },
    {
        "name": "g5k_3",
        "random": 0.3,
        "size": 5000
    },
    {
        "name": "g5k_4",
        "random": 0.4,
        "size": 1000
    },
    {
        "name": "g5k_5",
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
    print("Generating graph " + graph["name"] + "...");
    G = nx.fast_gnp_random_graph(graph["size"], graph["random"]);
    gml.write_gml(G, graph["name"] + ".gml");
    print("Graph generation done, saved as " + graph["name"] + ".gml");

print "Done!";
exit();
