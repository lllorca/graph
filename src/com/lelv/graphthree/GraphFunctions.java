package com.lelv.graphthree;

import java.util.*;

public abstract class GraphFunctions {

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
            result.add(vertex.node);
            for (AbstractGraph<V, E>.Edge e : vertex.edges) {
                if (!e.neighbor.visited) {
                    q.add(e.neighbor);
                    e.neighbor.visited = true;
                }
            }
        }
        return result;
    }

    public static <V, E extends WeightedEdge> HashMap<V, Double> dijkstra(AbstractGraph<V, E> graph, V originNode) {
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
                    if (!e.neighbor.visited
                            && (min == null || e.connection.getWeight().doubleValue() + distToNode < minDist)) {
                        min = e;
                        minDist = e.connection.getWeight().doubleValue() + distToNode;
                    }
                }
            }
            if (min != null) {
                visited.add(min.neighbor);
                min.neighbor.visited = true;
                distance.put(min.neighbor.node, minDist);
            }
        } while (min != null);

        return distance;
    }

    public static <V, E> boolean hasPath(AbstractGraph<V, E> graph, V originNode, V destinationNode) {
        List<V> result = dfs(graph, originNode);
        if (result == null)
            return false;
        return result.contains(destinationNode);
    }

    public static <V, E> boolean isConnected(AbstractGraph<V, E> graph) {
        if (graph.isEmpty()) {
            return true;
        }
        graph.clearVisit();

        List<AbstractGraph<V, E>.Vertex> vertices = graph.getVertices();
        dfs(vertices.get(0), new ArrayList<>());

        return vertices.stream().allMatch(vertex -> vertex.visited);
    }

    public static <V, E> int connectedComponents(AbstractGraph<V, E> graph) {
        graph.clearVisit();
        return pathCount(graph);
    }

    private static <V, E> int pathCount(AbstractGraph<V, E> graph) {
        int count = 0;
        AbstractGraph<V, E>.Vertex vertex;
        while ((vertex = unvisited(graph)) != null) {
            count++;
            dfs(vertex, new ArrayList<>());
        }
        return count;
    }

    private static <V, E> AbstractGraph<V, E>.Vertex unvisited(AbstractGraph<V, E> graph) {
        for (AbstractGraph<V, E>.Vertex vertex : graph.getVertices()) {
            if (!vertex.visited)
                return vertex;
        }
        return null;
    }

    public static <V, E> boolean isCutVertex(AbstractGraph<V, E> graph, V node) {
        Optional<AbstractGraph<V, E>.Vertex> vertex = graph.getVertex(node).filter(v -> v.edges.size() > 0);

        if (!vertex.isPresent())
            return false;
        graph.clearVisit();
        int components = pathCount(graph);
        graph.clearVisit();
        vertex.get().visited = true;
        return components != pathCount(graph);
    }

    public static <V, E> boolean isBridge(AbstractGraph<V, E> graph, V nodeA, V nodeB) {
        Optional<AbstractGraph<V, E>.Edge> edge = graph.getEdge(nodeA, nodeB);

        if (!edge.isPresent()) {
            return false;
        }
        int components = connectedComponents(graph);
        graph.disconnectNodes(nodeA, nodeB);
        int newComponents = connectedComponents(graph);
        graph.connectNodes(nodeA, nodeB, edge.get().connection);
        return components != newComponents;
    }
}
