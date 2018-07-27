import networkx as nx
import networkx.algorithms as algorithms
import matplotlib.pyplot as plt
import sys

def get_max_degree_node(graph, debug = 0):
    max_node = 0;
    max_degree = 0;
    for node, degree in graph.degree:
        if (degree > max_degree):
            max_degree = degree;
            max_node = node;

    return (max_node, max_degree);

# Create a graph from kernel by removing vertecies with xv 0 and 1,
# we only need to decide on the 0.5 ones.
def from_lp_kernel(graph, kernel, debug = 0):
    nodes = [];
    cover = [];
    removed = [];
    for v, xv in kernel:
        if xv == 0.5:
            nodes.append(v);
        else:
            removed.append(v);
            if xv == 1:
                cover.append(v);

    new_graph = graph.copy();
    for node in removed:
        new_graph.remove_node(node);
    if (debug > 0):
        print "Prepared a graph for kernel ", kernel, "with cover", cover
    return new_graph, cover;

def bar_fight_iterative(graph, k, debug = 0):
    result = {
        "cover": [],
        "success": False
    };
    if (k <= 0):
        # failed.
        return result;

    (max_node, degree) = get_max_degree_node(graph, debug);
    cover = [];
    if degree <= 1:
        # Trivial case - choose one of each edge. The rest are discarded.
        for u,v in graph.edges():
            cover.append(u);
            k = k - 1;

        return {
            "success": k >= 0,
            "cover": cover
        }

    # Else, split into two cases: v or N(v).
    remove = [];
    for node in graph.nodes():
        if graph.degree[node] == 0:
            remove.append(node);

    base_graph = graph.copy();
    for node in remove:
        base_graph.remove_node(node);

    # N(v) branch. (First since it might take less time).
    neighbors = graph.adj[max_node];
    nv_graph = base_graph.copy();
    nv_graph.remove_nodes_from(neighbors);
    nv_result = bar_fight_iterative(nv_graph, (k - len(neighbors)), debug);
    cover_nv = list(neighbors) + nv_result["cover"];

    if (nv_result["success"]):
        #N(v) was successful.
        return {
            "success": True,
            "cover": cover_nv
        };

    # v branch.
    v_graph = base_graph.copy();
    v_graph.remove_node(max_node);
    v_result = bar_fight_iterative(v_graph, k - 1, debug);
    cover_v = [max_node] + v_result["cover"];

    if (v_result["success"]):
        # v was successful.
        return {
            "success": True,
            "cover": cover_v
        };

    # No-instance.
    return result;

def vertex_cover_from_kernel(graph, kernel, k, debug = 0):
    ready_graph, cover = from_lp_kernel(graph, kernel, debug);
    k = k - len(cover);
    print "Starting iterations for k: ", k;
    result = bar_fight_iterative(ready_graph, k, debug);

    if result["success"]:
        return cover + result["cover"];

    return [];
