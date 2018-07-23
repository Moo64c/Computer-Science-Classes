import networkx as nx
import networkx.algorithms.approximation as app
import networkx.readwrite.gml as gml
import matplotlib.pyplot as plt
from scipy.optimize import *
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
    kernel = get_lp_kernel(graph);
    benchmark.add("get kernel");
    kernel.x = map(lambda x: round(x, 2), kernel.x);
    print kernel.x;
    print sum(kernel.x);
    return -1;

    # Start MINIMUM VERTEX COVER algorithm.
    k = 1;
    # Remove vertices according to VC1 & VC2.
    while(k < len(graph.nodes())):
        result = vc(graph, k);
        if (result["success"]):
            break;
        k += 1;
    print result;


    benchmark.display();
    # TODO Return approx result.
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
        reduction_result = reduction_vc_1_2(graph, k);
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
    result["vertex_cover"] = after_crown;
    return result;

def get_lp_kernel(graph):
    cons = [];
    bnds = [];
    x0 = [];

    for n in graph.nodes():
        bnds.append((0, 1));
        x0.append(1);

    for u,v in graph.edges():
        cons.append({'type': 'ineq', 'fun': lambda x, i=u, j=v:  x[i] + x[j] - 1});

    fun = lambda x: sum(x);
    res = minimize(fun, x0, method = 'SLSQP', bounds=bnds, constraints=cons, options={'disp':True});
    return res;



# FIXME Temp; remove
graph_name = "test_graph.gml"
min_vc(graph_name);
