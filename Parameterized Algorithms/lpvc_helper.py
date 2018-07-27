import networkx as nx
import networkx.algorithms as algorithms
from vc_kernel import *
from branching import *


def vc_star_algorithm(graph, k, benchmark):
    pass


# Reduction VC 4 removes all vertices in V_0 and V_1, and puts all V_1 nodes in cover.
def lpvc_reduction_vc_4(graph, k, kernel, debug = 0):
    cover = [];
    removed = [];

    kernel_sum = sum([xv for v, xv in kernel]);
    if (kernel_sum > k):
        # Failed the vertex cover for (G,k).
        return {"success" : False};

    for node, xv in kernel:
        if xv != 0.5:
            # Need to be removed.
            removed.append(node);
            if (xv == 1):
                cover.append(node);
    return {
        "removed": removed,
        "cover": cover,
        "success": True
    }

# Helper functions for kernels.
def _is_all_half(kernel):
    for v,xv in kernel:
        if xv != 0.5:
            return False;
    return True;

def _kernel_weight(kernel):
    return sum([xv for v,xv in kerenl]);


# Lemma 3.7.
def get_optimum_solution(graph, k, benchmark, debug = 0):
    if (not graph.nodes()):
        # Empty graph.
        return {
            "kernel" : [],
        };
    # Proposition 2.24 to get half-integral solution.
    kernel = get_lp_kernel(graph, benchmark, debug);
    if not (_is_all_half(kernel)):
        # Some items are not 1/2 - we can run VC 4 on this.
        return {
            "kernel": kernel,
        }
    # All items are 1/2. We need to find a step to improve our stance.
    # Our current stance is:
    old_weight = _kernel_weight(kernel);
    for node in graph.nodes():
        # Create a test graph without the current node.
        test_graph = graph.copy();
        test_graph.remove_node(node);
        # Use Proposition 2.24 again to see the possible outcome.
        test_kernel = get_lp_kernel(test_graph, benchmark, debug);
        if (_kernel_weight(test_kernel) <= old_weight - 1):
            # This is an optimum solution for LPVC(G).
            # Have the selected node reduced in VC 4.
            test_kernel.append((node, 1));
            return {
                "kernel": test_kernel,
                "success": True
            }
    # The all-1/2 solution is the optimum solution. We have to branch from here.
    return {
        "kernel": kernel,
    }

# Find optimum solution and apply reduction vc 4 if applicable.
def lpvc_reduction_vc_5(graph, k, benchmark, debug = 0):
    kernel = get_optimum_solution(graph, k, benchmark, debug);
    if not (len(kernel) == 0 or _is_all_half(kernel)):
        # Apply reduction vc 4.
