package com.lelv.graphthree;

import com.lelv.graphthree.impl.Graph;

import java.util.*;

public abstract class GraphFunctions {


    /**
     * Applies a Depth First Search on a graph, taking the originNode as the root
     *
     * @param graph      the graph on which to apply the algorithm
     * @param originNode the node to take as root of the search
     * @return list of nodes that result from doing DFS
     */
    public static <V, E> List<V> dfs(AbstractGraph<V, E> graph, V originNode) {
        Optional<AbstractGraph<V, E>.Vertex> vertex = graph.getVertex(originNode);
        if (!vertex.isPresent())
            return null;
        graph.clearVisit();
        List<V> result = new ArrayList<>();
        dfs(vertex.get(), result);
        return result;
    }

    private static <V, E> void dfs(AbstractGraph<V, E>.Vertex originNode, List<V> result) {
        if (originNode.visited)
            return;
        result.add(originNode.node);
        originNode.visited = true;
        for (AbstractGraph<V, E>.Vertex neighbor : originNode.getNeighbors())
            dfs(neighbor, result);
    }

    /**
     * Applies a Breadth First Search on a graph, taking the originNode as the root
     *
     * @param graph      the graph on which to apply the algorithm
     * @param originNode the node to take as root of the search
     * @return list of nodes that result from doing BFS
     */
    public static <V, E> List<V> bfs(AbstractGraph<V, E> graph, V originNode) {
        Optional<AbstractGraph<V, E>.Vertex> optional = graph.getVertex(originNode);
        if (!optional.isPresent())
            return null;

        AbstractGraph<V, E>.Vertex vertex = optional.get();
        graph.clearVisit();
        List<V> result = new ArrayList<>();

        Queue<AbstractGraph<V, E>.Vertex> q = new LinkedList<>();
        q.add(vertex);
        while (!q.isEmpty()) {
            vertex = q.poll();
            vertex.visited = true;
            result.add(vertex.node);
            for (AbstractGraph<V, E>.Edge e : vertex.edges) {
                if (!e.destination.visited) {
                    q.add(e.destination);
                }
            }
        }
        return result;
    }

    /**
     * Applies Dijkstra's Shortest Path First algorithm on the graph
     *
     * @param graph      the graph on which to apply the algorithm
     * @param originNode the node to take as root of the search
     * @return a map whose keys are the nodes, and whose values are the weight of the shortest distance
     * to the node from the root.
     */
    public static <V, E extends WeightedEdge> Map<V, Double> dijkstra(AbstractGraph<V, E> graph, V originNode) {
        Optional<AbstractGraph<V, E>.Vertex> optional = graph.getVertex(originNode);
        if (!optional.isPresent())
            return null;

        AbstractGraph<V, E>.Vertex vertexOrigin = optional.get();

        graph.clearVisit();
        // Para cada nodo guardar cuál es la distancia acumulada
        HashMap<V, Double> distance = new HashMap<>(graph.getNumberOfNodes());

        // Creamos una lista de nodos visitados
        List<AbstractGraph<V, E>.Vertex> visited = new ArrayList<>(graph.getNumberOfNodes());
        distance.put(originNode, 0.0);
        vertexOrigin.visited = true;
        visited.add(vertexOrigin);

        AbstractGraph<V, E>.Edge min;
        do {
            // Tomar el nodo sin visitar más cercano al origen
            min = null;
            Double minDist = 0.0;
            for (AbstractGraph<V, E>.Vertex vertex : visited) {
                Double distToNode = distance.get((V) vertex.node);
                for (AbstractGraph<V, E>.Edge e : vertex.edges) {
                    if (!e.destination.visited
                            && (min == null || e.connection.getWeight().doubleValue() + distToNode < minDist)) {
                        min = e;
                        minDist = e.connection.getWeight().doubleValue() + distToNode;
                    }
                }
            }
            if (min != null) {
                visited.add(min.destination);
                min.destination.visited = true;
                distance.put(min.destination.node, minDist);
            }
        } while (min != null);

        return distance;
    }

    /**
     * Verifies if a path exists between the origin node and the destination one
     *
     * @param graph           the graph on which to apply the algorithm
     * @param originNode      node that sets the beginning point of the path
     * @param destinationNode node that sets the end point of the path
     * @return true if the path exists, or false if it does not
     */
    public static <V, E> boolean hasPath(AbstractGraph<V, E> graph, V originNode, V destinationNode) {
        List<V> result = dfs(graph, originNode);
        if (result == null)
            return false;
        return result.contains(destinationNode);
    }

    /**
     * Verifies if the graph is a connected graph. A connected graph is a graph in which it's possible to get from
     * every vertex in the graph to every other vertex through a series of edges
     *
     * @param graph the graph on which to apply the algorithm
     * @return true if it is connected, false if not
     */
    public static <V, E> boolean isConnected(Graph<V, E> graph) {
        if (graph.isEmpty()) {
            return true;
        }
        graph.clearVisit();

        List<AbstractGraph<V, E>.Vertex> vertices = graph.getVertices();
        dfs(vertices.get(0), new ArrayList<>());

        return vertices.stream().allMatch(vertex -> vertex.visited);
    }

    /**
     * Calculates the number of components in the graph. A component is a subgraph in which any two vertices are
     * connected to each other by paths, and which is connected to no additional vertices in the supergraph
     *
     * @param graph
     * @return
     */
    public static <V, E> int numberOfComponents(Graph<V, E> graph) {
        graph.clearVisit();
        return pathCount(graph);
    }

    private static <V, E> int pathCount(Graph<V, E> graph) {
        int count = 0;
        AbstractGraph<V, E>.Vertex vertex;
        while ((vertex = unvisited(graph)) != null) {
            count++;
            dfs(vertex, new ArrayList<>());
        }
        return count;
    }

    private static <V, E> AbstractGraph<V, E>.Vertex unvisited(Graph<V, E> graph) {
        for (AbstractGraph<V, E>.Vertex vertex : graph.getVertices()) {
            if (!vertex.visited)
                return vertex;
        }
        return null;
    }

    /**
     * Verifies if a vertex is a cut vertex in a graph. A cut vertex is any vertex whose removal increases the
     * number of connected components.
     *
     * @param graph the graph on which to apply the algorithm
     * @param node  node to verify if it is a cut vertex
     * @return true if it is a cut vertex, false if not
     */
    public static <V, E> boolean isCutVertex(Graph<V, E> graph, V node) {
        Optional<AbstractGraph<V, E>.Vertex> vertex = graph.getVertex(node).filter(v -> v.edges.size() > 0);

        if (!vertex.isPresent())
            return false;
        graph.clearVisit();
        int components = pathCount(graph);
        graph.clearVisit();
        vertex.get().visited = true;
        return components != pathCount(graph);
    }

    /**
     * Verifies if an edge is a bridge. A bridge is an edge of a graph whose deletion increases its number of
     * connected components.
     *
     * @param graph the graph on which to apply the algorithm
     * @param nodeA one end of the edge
     * @param nodeB another end of the edge
     * @return true if the edge is a bridge, false if not
     */
    public static <V, E> boolean isBridge(Graph<V, E> graph, V nodeA, V nodeB) {
        Optional<AbstractGraph<V, E>.Edge> edge = graph.getEdge(nodeA, nodeB);

        if (!edge.isPresent()) {
            return false;
        }
        int components = numberOfComponents(graph);
        graph.disconnectNodes(nodeA, nodeB);
        int newComponents = numberOfComponents(graph);
        graph.connectNodes(nodeA, nodeB, edge.get().connection);
        return components != newComponents;
    }
}
