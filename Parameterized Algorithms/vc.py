import networkx as nx
import networkx.algorithms.approximation as app
import networkx.readwrite.gml as gml
import matplotlib.pyplot as plt
from scipy.optimize import *
import math
from reductions import *
from vc_kernel import *
from branching import *
from benchmark import *
import argparse

def destringizer(str):
    return int(str);

def vc(graph, benchmark, k, debug):

    # Run the included approximation algoritm for comparison.
    approx_cover = algorithms.approximation.vertex_cover.min_weighted_vertex_cover(graph);
    benchmark.add("Networkx vertex cover approximation algorithm: " + str(len(approx_cover)) + " nodes in cover.");

    # Get kernel for solution.
    kernel = get_lp_kernel(graph, benchmark, debug);
    benchmark.add("kernel found");
    cleaned_kernel = [xv for v, xv in kernel];
    if (k == -1):
        # Unspecified k variable.
        # TODO: Can we reduce from 2 * k?
        k = 2 * sum(cleaned_kernel);

    # Use the kernel: branching.
    cover = vertex_cover_from_kernel(graph, kernel, k, debug);
    benchmark.add("vertex cover from kernel");

    print "Calculated", len(cover), "vertex cover: ", cover;
    benchmark.display();
    # Color the graph nicely and display.
    show_result(graph, cover);
    return -1;

def show_result(graph, cover):
    # Color nodes in cover as blue.
    node_colors = [];
    for node in graph.nodes():
        if node in cover:
            node_colors.append('blue');
        else:
            node_colors.append('red');

    # Color covered edges in blue.
    edge_colors = [];
    for u, v in graph.edges():
        if u in cover or v in cover:
            edge_colors.append('blue');
        else:
            edge_colors.append('red');

    pos = nx.circular_layout(graph);
    nx.draw(graph, pos=pos, node_color=node_colors, edge_color=edge_colors, with_labels=True);
    plt.show();

def start_vc(file_name, k = -1, debug = 0):
    benchmark = benchmarker(file_name);

    # Load graph.
    graph = gml.read_gml(file_name, "label", destringizer);
    benchmark.add("loading");
    vc(graph = graph, benchmark=benchmark, debug=debug, k=k);


def start_lpvc(file_name, debug = 0):
    # Todo
    pass

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
