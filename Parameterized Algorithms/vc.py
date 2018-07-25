import networkx as nx
import networkx.algorithms.approximation as app
import networkx.readwrite.gml as gml
import matplotlib.pyplot as plt
from scipy.optimize import *
from reductions import *
from vc_kernel import *
from benchmark import *

debug = False;
graph_name = "100_0.05.gml";

def destringizer(str):
    return int(str);

def min_vc(graph_name):
    # Load graph.
    graph = gml.read_gml(graph_name, "label", destringizer);
    benchmark.add("loading");
    # Get kernel for solution.
    kernel = get_lp_kernel(graph, benchmark, debug);
    benchmark.add("kernel found.");
    cleaned_kernel = [xv for v, xv in kernel]
    k = sum(cleaned_kernel)
    # result = vc(graph, k);
    print cleaned_kernel, k;
    benchmark.display();
    return -1;


def vc(original_graph, k):
    result = {
        "vertex_cover": [],
        "success": False,
        "k": k
    };
    graph = original_graph.copy();

    # Apply VC 1 & 2.
    removed = 1;
    solution = [];
    while (removed > 0):
        reduction_result = reduction_vc_1_2(graph , k);
        removed = len(reduction_result["removed"]);
        solution += reduction_result["in_vertex_cover"];
        k = k - len(reduction_result["in_vertex_cover"]);

    # Check if a solution is possible according to VC.3:
    if (k < 0 and (len(graph.nodes()) > (k*k + k)) or (len(graph.edges()) > k*k)):
        # Failed.
        result["vertex_cover"] = solution;
        return result;

    # Apply the Crown Lemma to the graph.
    after_crown = crown_lemma(graph, k);
    benchmark.add("crown_lemma");
    result["vertex_cover"] = after_crown;
    return result;

# FIXME Temp; remove
benchmark = benchmarker(graph_name);
min_vc(graph_name);
