package com.lelv.graphthree;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractGraph<V, E> {

    public static final int NON_EXISTENT_DEGREE = -1;

    private int numberEdges = 0;
    private Map<V, Vertex> vertexMap = new HashMap<>();
    private List<Vertex> vertexList = new ArrayList<>();

    /*
     * PUBLIC METHODS
     *
     * Public methods do not expose Vertex or Edge, they only deal with their representations
     * as V and E, which are nodes and connections (respectively).
     * The end user doesn't know implementation specific details
     *
     */

    // Query Graph Public

    public List<V> getNodes() {
        return vertexList.stream()
                         .map(vertex -> vertex.node)
                         .collect(Collectors.toList());
    }

    public Optional<E> getConnection(V originNode, V destinationNode) {
        return getEdge(originNode, destinationNode).map(edge -> edge.connection);
    }

    public boolean isEmpty() {
        return getNumberOfNodes() == 0;
    }

    public int getNumberOfNodes() {
        return vertexList.size();
    }

    public int getNumberOfConnections() {
        return numberEdges;
    }

    public boolean nodeExists(V node) {
        return vertexMap.containsKey(node);
    }

    public boolean connectionExists(V originNode, V destinationNode) {
        return getConnection(originNode, destinationNode).isPresent();
    }

    public List<V> getNeighbors(V sourceNode) {
        return Optional.ofNullable(vertexMap.get(sourceNode))
                       .map(vertex -> vertex.getNeighbors()
                                            .stream()
                                            .map(v -> v.node)
                                            .collect(Collectors.toList()))
                       .orElse(null);
    }

    // Modify Graph public

    public final boolean addNodes(V... nodes) {
        boolean result = true;
        for (V node : nodes) {
            result = result && addNode(node);
        }
        return result;
    }

    public boolean addNode(V node) {
        if (node == null || vertexMap.containsKey(node)) {
            return false;
        }
        Vertex newVertex = new Vertex(node);
        vertexList.add(newVertex);
        vertexMap.put(node, newVertex);
        return true;
    }

    public boolean removeNode(V node) {
        Vertex vertex = vertexMap.get(node);
        if (vertex == null) {
            return false;
        }

        vertexList.stream()
                  .filter(v -> !v.equals(vertex))
                  .forEach(v -> v.removeNeighbor(node));

        vertexMap.remove(node);
        vertexList.remove(vertex);
        return true;
    }

    public boolean connectNodes(V originNode, V destinationNode, E connection) {
        Vertex originVertex = vertexMap.get(originNode);
        Vertex destinationVertex = vertexMap.get(destinationNode);

        if (originVertex == null || destinationVertex == null) {
            return false;
        }

        boolean edgeIsNotDuplicate = originVertex.edges.stream()
                                                       .noneMatch(e -> e.neighbor.node.equals(destinationNode));

        if (edgeIsNotDuplicate) {
            originVertex.addNeighbor(connection, destinationVertex);
            numberEdges++;
        }

        return edgeIsNotDuplicate;
    }

    public boolean disconnectNodes(V originNode, V destinationNode) {
        Vertex originVertex = vertexMap.get(originNode);
        Vertex destinationVertex = vertexMap.get(destinationNode);

        if (originVertex == null || destinationVertex == null) {
            return false;
        }

        return originVertex.edges.stream()
                                 .filter(edge -> edge.neighbor.node.equals(destinationNode))
                                 .findFirst()
                                 .map(edge -> {
                                     originVertex.edges.remove(edge);
                                     numberEdges--;
                                     return true;
                                 }).orElse(false);
    }

    /*
     * PROTECTED METHODS & CLASSES
     *
     * Meant for the classes that wishes to extend this class. They expose Vertex and Edge so that
     * the programmer can exploit more the implementation.
     *
     */

    // Query Graph Protected

    protected List<Vertex> getVertices() {
        return vertexList;
    }

    protected Optional<Vertex> getVertex(V node) {
        return Optional.ofNullable(vertexMap.get(node));
    }

    protected Optional<Edge> getEdge(V originNode, V destinationNode) {
        Vertex originVertex = vertexMap.get(originNode);

        if (originVertex == null) {
            return Optional.empty();
        }

        return originVertex.edges.stream()
                                 .filter(edge -> edge.neighbor.node.equals(destinationNode))
                                 .findFirst();
    }

    // Modify graph protected

    protected void clearVisit() {
        vertexList.forEach(vertex -> vertex.visited = false);
    }

    // Classes

    protected class Vertex {
        public final V node;
        public boolean visited;
        public List<Edge> edges;

        Vertex(V node) {
            this.node = node;
            this.visited = false;
            this.edges = new ArrayList<>();
        }

        public List<Vertex> getNeighbors() {
            return edges.stream()
                        .map(edge -> edge.neighbor)
                        .collect(Collectors.toList());
        }

        private void addNeighbor(E connection, Vertex neighbor) {
            edges.add(new Edge(connection, neighbor));
        }

        private void removeNeighbor(V neighbor) {
            edges.stream()
                 .filter(e -> e.neighbor.node.equals(neighbor))
                 .findFirst()
                 .ifPresent(e -> edges.remove(e));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Vertex vertex = (Vertex) o;

            return node.equals(vertex.node);
        }

        @Override
        public int hashCode() {
            return node.hashCode();
        }
    }

    protected class Edge {
        public final E connection;
        public final Vertex neighbor;

        Edge(E connection, Vertex neighbor) {
            this.connection = connection;
            this.neighbor = neighbor;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Edge edge = (Edge) o;

            if (connection != null ? !connection.equals(edge.connection) : edge.connection != null) return false;
            return neighbor.equals(edge.neighbor);
        }

        @Override
        public int hashCode() {
            int result = connection != null ? connection.hashCode() : 0;
            result = 31 * result + neighbor.hashCode();
            return result;
        }
    }


}
