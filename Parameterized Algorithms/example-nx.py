import networkx as nx
import networkx.algorithms.approximation as app
import matplotlib.pyplot as plt

G = nx.fast_gnp_random_graph(50, 0.1);
P = app.min_weighted_vertex_cover(G);
nx.draw(G, with_labels=True, font_weight='bold');
print P;

# nx.draw_shell(G, nlist=[range(5, 10), range(5)], with_labels=True, font_weight='bold');

plt.show();

# plt.savefig("path.png");
