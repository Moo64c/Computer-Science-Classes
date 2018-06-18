#@file reductions.py
# Contains functions that perform the VC1-5 reductions.
import networkx as nx
import networkx.algorithms as algorithms

# Settings.
run_tests = False;
debug = False;

# Apply reduction VC.1 and VC.2 (page 21) to a networkx graph.
# If G contains an isolated vertex v, delete v from G. The new instance is (G - v, k).
# If there is a vertex v with degree over k, v should be in any vertex cover of size at most k.
def reduction_vc_1_2(graph, k):
    # Can not remove nodes while iterating over it.
    removed = set();
    in_vertex_cover = [];
    for vertice in graph:
        if (graph.degree[vertice] == 0):
            # Vertice does not have any neighbors. Can be safetly deleted from graph.
            removed.add(vertice);
        if (graph.degree[vertice] > k):
             # Vertice is of degree k+1 or more. Always in VC.
             removed.add(vertice);
             in_vertex_cover.append(vertice);

    for item in removed:
        if (debug):
            print("VC1 VC2 debug: Removing node " + str(item) + " from graph.");
        graph.remove_node(item);

    return {
        "removed" : removed,
        "in_vertex_cover": in_vertex_cover
    };


def crown_lemma(graph, k):
    result = {
        "result" : [],
        "k" : k,
        "success": False
    };
    # Get a maximal matching for the graph.
    # Built-in greedy algorithm from networkx. Runs in O(|E|).
    matching = algorithms.maximal_matching(graph);
    if (len(matching) > k):
        # Nothing to be done.
        result["result"] = matching;
        return result;
    # |matching| <= k.
    # Generate bipartite graph between Vm (Vertices of the matching set) and
    # the remainder of the vertices in the Graph (G\Vm).
    Vm = set();
    for edge in matching:
        for v in edge:
            Vm.add(v);
    remainder = graph.nodes() - list(Vm);
    # Filter edges to the remainder and Vm node sets.
    edges = set();
    for edge in graph.edges():
        if (edge[0] in remainder or
            edge[1] in remainder or
            edge[0] in Vm or
            edge[1] in Vm):
            edges.add(edge);

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
