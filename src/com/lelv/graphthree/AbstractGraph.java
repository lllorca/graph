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

    // Query Graph

    /**
     * @return list of all the nodes that are in the graph
     */
    public List<V> getNodes() {
        return vertexList.stream()
                         .map(vertex -> vertex.node)
                         .collect(Collectors.toList());
    }

    /**
     * Gets a connection in the graph. A connection is a binding between two different nodes
     *
     * @param originNode      the origin of the connection
     * @param destinationNode the destination of the connection
     * @return an optional that has an element of type E if the connection exists, or empty if not
     */
    public Optional<E> getConnection(V originNode, V destinationNode) {
        return getEdge(originNode, destinationNode).map(edge -> edge.connection);
    }

    /**
     * Returns whether the graph is empty or not. A graph is empty when it doesn't have a single node in it
     *
     * @return true if the graph is empty, false if it's not
     */
    public boolean isEmpty() {
        return getNumberOfNodes() == 0;
    }

    /**
     * @return the number of nodes in the graph
     */
    public int getNumberOfNodes() {
        return vertexList.size();
    }

    /**
     * @return the number of connections in the graph
     */
    public int getNumberOfConnections() {
        return numberEdges;
    }

    /**
     * Verifies if a node is part of the graph
     *
     * @param node the node to do the check
     * @return true if the node is in the graph, or false otherwise
     */
    public boolean nodeExists(V node) {
        return vertexMap.containsKey(node);
    }

    /**
     * Verifies if a connection is part of the graph
     *
     * @param originNode      the node the connection goes from
     * @param destinationNode the node the connection goes to
     * @return true if the connection exists in the graph, or false otherwise
     */
    public boolean connectionExists(V originNode, V destinationNode) {
        return getConnection(originNode, destinationNode).isPresent();
    }

    /**
     * Gets all the neighbors of a node. A neighbor is another node to which the sourceNode has a connection with.
     *
     * @param sourceNode the node to query for its neighbors
     * @return list of nodes that are neighbors of the sourceNode, or null if the sourceNode doesn't exist
     */
    public List<V> getNeighbors(V sourceNode) {
        return Optional.ofNullable(vertexMap.get(sourceNode))
                       .map(vertex -> vertex.getNeighbors()
                                            .stream()
                                            .map(v -> v.node)
                                            .collect(Collectors.toList()))
                       .orElse(null);
    }

    // Modify Graph

    /**
     * Adds nodes to the graph. Can accept a large number of nodes in a comma-separated manner.
     * Example: addNodes("node1", "node2", "node3", "node4")
     *
     * @param nodes array of nodes to add
     * @return true if all the nodes were added, or false if at least one wasn't added
     */
    public final boolean addNodes(V... nodes) {
        boolean result = true;
        for (V node : nodes) {
            result = result && addNode(node);
        }
        return result;
    }

    /**
     * Adds a node to the graph
     *
     * @param node node to add
     * @return true if it was added successfully, or false if the node is null or if it already exists in the graph
     */
    public boolean addNode(V node) {
        if (node == null || vertexMap.containsKey(node)) {
            return false;
        }
        Vertex newVertex = new Vertex(node);
        vertexList.add(newVertex);
        vertexMap.put(node, newVertex);
        return true;
    }

    /** Removes a node from the graph
     * @param node node to remove
     * @return true if it was removed successfully, or false i
     */
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

    /** Connects two nodes together, and stores an object in the connection between them.
     *  Example: if nodeA and nodeB exist, in order for nodeA to reach nodeB
     *  there must be a connection between them, and calling connectNodes(nodeA, nodeB, xxx) does that.
     *  Afterwards, nodeB will be a neighbor of nodeA.
     * @param originNode node that serves as origin of the connection
     * @param destinationNode node that serves as destination of the connection
     * @param connection information to store in the connection
     * @return true if the nodes are connected successfully, false if either the origin or destination nodes are
     * null, or if the connection already exists
     */
    public boolean connectNodes(V originNode, V destinationNode, E connection) {
        Vertex originVertex = vertexMap.get(originNode);
        Vertex destinationVertex = vertexMap.get(destinationNode);

        if (originVertex == null || destinationVertex == null) {
            return false;
        }

        boolean edgeIsNotDuplicate = originVertex.edges.stream()
                                                       .noneMatch(e -> e.destination.node.equals(destinationNode));

        if (edgeIsNotDuplicate) {
            originVertex.addNeighbor(connection, destinationVertex);
            numberEdges++;
        }

        return edgeIsNotDuplicate;
    }

    /** Disconnects two nodes. After invoking this function, the two nodes will no longer be neighbors.
     * @param originNode node that serves as origin of the connection
     * @param destinationNode node that serves as destination of the connection
     * @return true if the nodes are disconnected successfully, false if either the origin or destination nodes are
     * null, or if the connection does not exist
     */
    public boolean disconnectNodes(V originNode, V destinationNode) {
        Vertex originVertex = vertexMap.get(originNode);
        Vertex destinationVertex = vertexMap.get(destinationNode);

        if (originVertex == null || destinationVertex == null) {
            return false;
        }

        return originVertex.edges.stream()
                                 .filter(edge -> edge.destination.node.equals(destinationNode))
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
     * Meant for the classes that extend this class, unlocking more features. They expose Vertex and Edge
     * so that the programmer can exploit more the implementation.
     *
     */

    // Query Graph

    /**
     * @return a list of the vertices in the graph
     */
    protected List<Vertex> getVertices() {
        return vertexList;
    }

    /**
     * @param node node associated to the vertex
     * @return optional with the vertex associated to the node if it exists, or empty if not
     */
    protected Optional<Vertex> getVertex(V node) {
        return Optional.ofNullable(vertexMap.get(node));
    }

    /**
     * @param originNode node that originates the connection
     * @param destinationNode destination node of the connection
     * @return optional with the edge associated to the connection if it exists, or empty if not
     */
    protected Optional<Edge> getEdge(V originNode, V destinationNode) {
        Vertex originVertex = vertexMap.get(originNode);

        if (originVertex == null) {
            return Optional.empty();
        }

        return originVertex.edges.stream()
                                 .filter(edge -> edge.destination.node.equals(destinationNode))
                                 .findFirst();
    }

    // Modify Graph

    /**
     * The Vertex class has a boolean named "visited" used for various algorithms.
     * This method turns this boolean to false on every vertex in the graph
     */
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
                        .map(edge -> edge.destination)
                        .collect(Collectors.toList());
        }

        private void addNeighbor(E connection, Vertex neighbor) {
            edges.add(new Edge(connection, this, neighbor));
        }

        private void removeNeighbor(V neighbor) {
            edges.stream()
                 .filter(e -> e.destination.node.equals(neighbor))
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
        public final Vertex origin;
        public final Vertex destination;

        Edge(E connection, Vertex origin, Vertex destination) {
            this.connection = connection;
            this.origin = origin;
            this.destination = destination;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Edge edge = (Edge) o;

            if (connection != null ? !connection.equals(edge.connection) : edge.connection != null) return false;
            if (!origin.equals(edge.origin)) return false;
            return destination.equals(edge.destination);
        }

        @Override
        public int hashCode() {
            int result = connection != null ? connection.hashCode() : 0;
            result = 31 * result + origin.hashCode();
            result = 31 * result + destination.hashCode();
            return result;
        }
    }


}
