import networkx as nx
import networkx.algorithms.approximation as app
import networkx.readwrite.gml as gml
import matplotlib.pyplot as plt
from reductions import *
from benchmark import *

def destringizer(str):
    return int(str);

def min_vc(graph_name):
    # Benchmarking start.
    benchmark = benchmarker(graph_name);

    # Load graph.
    graph = gml.read_gml(graph_name, "label", destringizer);
    benchmark.add("loading");

    # Start MINIMUM VERTEX COVER algorithm.
    k = 1;
    # Remove vertices according to VC1 & VC2.
    while(k < len(graph.nodes())):
        result = vc(graph, k);
        if (result["success"]):
            break;
        print result;
        k += 1;


    benchmark.display();
    # Return approx result.
    # TODO
    return -1;


def vc(original_graph, k):
    result = {
        "vertex_cover": [],
        "success": False,
        "k": k
    };
    graph = original_graph;

    # Apply VC 1 & 2.
    removed = 1;
    solution = [];
    while (removed > 0):
        reduction_result = reduction_vc_1_2(graph, k);
        removed = len(reduction_result["removed"]);
        solution += reduction_result["in_vertex_cover"];
        k = k - len(reduction_result["in_vertex_cover"]);

    # Check if a solution is possible according to VC.3:
    if (k < 0 and (len(graph.nodes()) > (k*k + k)) or (len(graph.edges()) > k*k)):
        # Failed.
        return result;

    # Apply the Crown Lemma to the graph.
    after_crown = crown_lemma(graph, k);
    result["vertex_cover"] = after_crown;
    return result;


# FIXME Temp; remove
graph_name = "test_graph.gml"
min_vc(graph_name);
