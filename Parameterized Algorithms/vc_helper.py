import networkx as nx
import networkx.algorithms as algorithms
from vc_kernel import *
from branching import *

def vc(graph, benchmark, k, debug):

    # Get kernel for solution.
    kernel = get_lp_kernel(graph, benchmark, debug);
    benchmark.add("kernel found");
    cleaned_kernel = [xv for v, xv in kernel];
    # Use the kernel: branching.
    cover = vertex_cover_from_kernel(graph, kernel, k, debug);
    benchmark.add("vertex cover from kernel");

    return (graph, cover);
