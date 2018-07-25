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

debug = False;
graph_name = "100_0.05.gml";
# graph_name = "test_graph.gml"

def destringizer(str):
    return int(str);

def min_vc(graph_name):

    # Load graph.
    graph = gml.read_gml(graph_name, "label", destringizer);
    benchmark.add("loading");
    c = algorithms.approximation.vertex_cover.min_weighted_vertex_cover(graph);
    benchmark.add("Networkx vertex cover approximation algorithm: " + str(len(c)) + " nodes in cover.");

    # Get kernel for solution.
    kernel = get_lp_kernel(graph, benchmark, debug);
    benchmark.add("kernel found.");
    cleaned_kernel = [xv for v, xv in kernel];
    k = sum(cleaned_kernel);
    # Use the kernel: branching.
    cover = vertex_cover_from_kernel(graph, kernel, 55);
    print len(cover);
    benchmark.add("vertex cover from kernel.");

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

# FIXME Temp; remove
benchmark = benchmarker(graph_name);
min_vc(graph_name);
