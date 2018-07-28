import networkx as nx
import networkx.algorithms as algorithms
import matplotlib.pyplot as plt

# Display bipartite graph
# Taken from https://stackoverflow.com/questions/27084004/bipartite-graph-in-networkx
def _bipartite_draw(B, nodes):
    X = (B.nodes() - nodes);
    Y = (B.nodes() - X);
    pos = dict()
    pos.update( (n, (1, i)) for i, n in enumerate(X) ) # put nodes from X at x=1
    pos.update( (n, (2, i)) for i, n in enumerate(Y) ) # put nodes from Y at x=2
    nx.draw(B, pos=pos)
    nx.draw_networkx_labels(B, pos=pos);
    plt.show();

# Create bipartite graph for kernel construction (according to lemma 2.22)
def _create_bipartite(original_graph, benchmark, debug = 0):
    graph = nx.Graph();
    original_nodes = original_graph.nodes();
    nodes = [];
    edges = [];
    size = max(original_nodes) + 1;
    # Every vertex V split into v_1, v_2; v_2 is the number of v_1 plus the original size.
    for node in original_nodes:
        nodes.append(node);
        nodes.append(node + size);

    # Every edge uv: add u_1v_2 and v_1u_2 to bipartite graph.
    for u,v in original_graph.edges():
        edges.append((u, v + size));
        edges.append((v, u + size));

    graph.add_nodes_from(nodes);
    graph.add_edges_from(edges);

    if debug > 0:
        benchmark.add("bipartite split");
        _bipartite_draw(graph, original_nodes);

    return graph;

# Implementation of Proposition 2.24.
# This kernel can also be called a half-integral solution X if any of the results are weighted with 0.5 value.
# (For the 3.4 algorithm).
def get_lp_kernel(graph, benchmark, debug = 0):
    original_nodes = graph.nodes();
    if not (original_nodes):
        #Empty graph?
        if (debug > 0):
            print "Trying to get kernel of an empty graph.";
        return [];
    bipartite_graph = _create_bipartite(graph, benchmark, debug);
    if debug > 0:
        print "is bipartite?", algorithms.bipartite.basic.is_bipartite(bipartite_graph);
        print "sets:", algorithms.bipartite.basic.sets(bipartite_graph, original_nodes);

    # M = hk_matching(bipartite)
    # Should run at O(|E|*sqrt(|V|)) time.
    matching = algorithms.bipartite.matching.hopcroft_karp_matching(bipartite_graph, top_nodes=original_nodes);

    # X - vertex cover (bipartite graph) using the matching found with HK.
    # In bipartite graph, |M| = |X| by Konig's theorem - which shows how to get the vertex cover.
    cover = algorithms.bipartite.matching.to_vertex_cover(bipartite_graph, matching, top_nodes=original_nodes);

    if debug > 0:
        benchmark.add("Hopcroft Karp matching and coversion to vertex cover");
        print cover;

    # Kernel items added as (v, xv) tuples.
    kernel = [];
    size = len(graph.nodes());
    for node in graph.nodes():
        item = 0;
        if node in cover:
            item += 0.5;
        if node + size in cover:
            item += 0.5;
        kernel.append((node, item));

    cleaned_kernel = [xv for v, xv in kernel]
    if debug > 0:
        print "Kernel found:", kernel, "Sum:", sum(cleaned_kernel);

    return kernel;
