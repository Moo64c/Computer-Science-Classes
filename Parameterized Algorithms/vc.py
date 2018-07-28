import networkx as nx
import networkx.readwrite.gml as gml
import matplotlib.pyplot as plt
import math
# from reductions import *
from vc_helper import *
from lpvc_helper import *
from benchmark import *
import argparse

def destringizer(str):
    return int(str);


def start_vc(file_name, k = -1, debug = 0):
    benchmark = benchmarker(file_name);

    # Load graph.
    graph = gml.read_gml(file_name, "label", destringizer);
    benchmark.add("loading");
    graph, cover = vc(graph = graph, benchmark=benchmark, debug=debug, k=k);

    print "Calculated", len(cover), "vertex cover: ", cover;
    benchmark.display();
    # Color the graph nicely and display.
    show_result(graph, cover);


def start_lpvc(file_name , debug = 0, k = -1):
    benchmark = benchmarker(file_name);

    # Load graph.
    graph = gml.read_gml(file_name, "label", destringizer);
    benchmark.add("loading");

    graph, cover = lpvc_algorithm(graph, k, benchmark, debug);
    benchmark.add("Finding an optimum LPVC(G)");
    if not graph:
        print "Failed creating a vertex cover.";
        return;
    print "Calculated", len(cover), "vertex cover: ", cover;
    benchmark.display();
    # Color the graph nicely and display.
    show_result(graph, cover);
    pass

# Start program.
parser = argparse.ArgumentParser(description='Start vertex cover algorithm.');
parser.add_argument('file',
                    help=".gml file to process.",
                    type = str, default = "test_graph.gml");
parser.add_argument('--k',
                    help="Limit vertex cover by k.",
                    type = int, default = "-1");
parser.add_argument('--debug',
                    help="Debug level integer. 0 For none, 1 for messages, 2 for graphs.",
                    type = int, default = 0);
parser.add_argument('--lpvc', dest='start', action="store_const",
                   const = start_lpvc, default = start_vc,
                   help = 'Use lpvc algorithm (Theorem 3.4).');

args = parser.parse_args();
args.start(file_name=args.file, debug=args.debug, k=args.k);
