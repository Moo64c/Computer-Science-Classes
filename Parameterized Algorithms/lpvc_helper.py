#@file reductions.py
# Contains functions that perform the VC1-5 reductions.
import networkx as nx
import networkx.algorithms as algorithms
import matplotlib.pyplot as plt
import vc_kernel


def _is_all_half(x_v):
    for x in x_v:
        if x != 0.5:
            return False;
    return True;

# Lemma 3.7.
def get_optimum_lpvc_solution(graph, benchmark, k, debug = 0):
    possible_half_integral = get_lp_kernel(graph, benchmark debug);
    possible_x_v = [xv for v,xv in possible_half_integral];
    # Optimum half-integral solution baseline "k";
    if not (_is_all_half(possible_x_v)):
        # Not all-1/2 solution, "good enough".
        return {"k": k, "kernel": possible_half_integral, "removed": []};
    # All 1/2 solution. For all v in V(G), check kernel for G-v.
    vc_star_g = sum(x_v);
    for node in graph.nodes():
        # Work on a sub-graph without one of the vertices.
        graph.copy();
        graph.remove_node(node);

        # Find the kernel as if we chose the node to be in the vertex.
        half_integral = get_lp_kernel(graph, benchmark debug);
        # Have the node inside the kernel.
        half_integral.append((node, 1));

        x_v = [xv for v,xv in half_integral];
        if sum(x_v) <= vc_star_g - 1:
            # Found an optimum solution.
            return {"k": k - 1, "kernel": half_integral, "removed": [node]};
    # Failed - the 1/2 solution is the optimum solution for LPVC(G).
    return {"k": k, "kernel": possible_half_integral, "removed": []}};

def exhaust_vc_5(graph, benchmark, k, debug = 0):
    k = 0;
    cover = [];

    # Apply lemma 3.7
    optimum = get_optimum_lpvc_solution(graph, benchmark, debug);
    if not optimum["removed"]:
        # Empty "removed". Either all half or one removed.
        if (_is_all_half([xv for v, xv in optimum["kernel"]])):
            # Exhausted reduction vc 5.
            return optimum;
        # We have at least one new item in cover,


# Reduction VC 4 removes all vertices in V_0 and V_1, and puts all V_1 nodes in cover.
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
        "cover": cover,
        "k": k - len(cover)
    }
