#@file reductions.py
# Contains functions that perform the VC1-5 reductions.
import networkx as nx
import networkx.algorithms as algorithms
import matplotlib.pyplot as plt

# Settings.
run_tests = False;
debug = True;

# Apply reduction VC.1 and VC.2 (page 21) to a networkx graph.
# If G contains an isolated vertex v, delete v from G. The new instance is (G - v, k).
# If there is a vertex v with degree over k, v should be in any vertex cover of size at most k.
def reduction_vc_1_2(graph, k):
    # Can not remove nodes while iterating over it.
    removed = set();
    cover = [];
    for vertice in graph:
        if (graph.degree[vertice] == 0):
            # Vertice does not have any neighbors. Can be safetly deleted from graph.
            removed.add(vertice);
        if (graph.degree[vertice] > k):
             # Vertice is of degree k+1 or more. Always in VC.
             removed.add(vertice);
             cover.append(vertice);

    for item in removed:
        if (debug):
            print("VC1 VC2 debug: Removing node " + str(item) + " from graph.");
        graph.remove_node(item);

    return {
        "removed" : removed,
        "cover": cover
    };


def reduction_vc_4(graph, kernel, k):
    nodes = [];
    cover = [];
    removed = [];

    kernel_sum = sum([xv for v,xv in kernel]);
    if (kernel_sum > k):
        # No-instance.
        return {"success": False};

    for v, xv in kernel:
        if xv == 0.5:
            nodes.append(v);
        else:
            # Either 0 or 1 - remove both.
            removed.append(v);
            if xv == 1:
                # Part of V_1 group - in cover.
                cover.append(v);
    
    new_graph = graph.copy();
    for node in removed:
        new_graph.remove_node(node);
    if (debug > 0):
        print "Prepared a graph for kernel ", kernel, "with cover", cover
    return {
        "success": True,
        "graph": new_graph,
        "cover": cover
    }

#======================================= Tests for reductions.
# Testing for Reduction VC.1
def reduction_vc_1_test():
    # Create a graph to test.
    e = [(1, 2), (2, 3), (3, 4),(6,7)];
    expected_nodes = [1, 2, 3, 4, 6, 7];
    graph = nx.Graph(e);
    # Adding a node to be removed since it won't have any edges.
    graph.add_node(5);
    k = 2;

    reduction_vc_1_2(graph, k);
    success = True;
    for index, node in enumerate(graph.nodes()):
        if (node != expected_nodes[index]):
            success = False;
            break;

    if (not(success)):
        print "Error testing VC1: expected " + str(expected_nodes) + " - got " + str(graph.nodes());
        return False;

    print("VC1 test passed successfully.");
    return True;


# Testing for Reduction VC.2
def reduction_vc_2_test():
    # Create a graph to test.
    e = [(1, 2), (2, 3), (3, 4), (3, 5), (6, 7)];
    expected_nodes = [1, 2, 4, 5, 6, 7];
    graph = nx.Graph(e);
    graph.add_node(5);

    k = 2;
    reduction_vc_1_2(graph, k);

    success = True;
    for index, node in enumerate(graph.nodes()):
        if (node != expected_nodes[index]):
            success = False;
            break;

    if (not(success)):
        print "Error testing VC2: expected " + str(expected_nodes) + " - got " + str(graph.nodes());
        return False;

    print("VC2 test passed successfully.");
    return True;

if (run_tests):
    reduction_vc_1_test();
    reduction_vc_2_test();
