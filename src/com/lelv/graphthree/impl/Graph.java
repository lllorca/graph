package com.lelv.graphthree.impl;

import com.lelv.graphthree.AbstractGraph;

public class Graph<V, E> extends AbstractGraph<V, E> {

    /**
     * Connects two nodes. After invoking this function, the two nodes will be neighbors of each other.
     *
     * @param nodeA      one of the nodes to connect
     * @param nodeB      the other node to connect
     * @param connection information to store in the connection
     * @return true if the nodes are connected successfully, false if either nodeA or nodeB are
     * null, or if the connection already exists
     */
    @Override
    public boolean connectNodes(V nodeA, V nodeB, E connection) {
        boolean result = super.connectNodes(nodeA, nodeB, connection);
        return result && super.connectNodes(nodeB, nodeA, connection);
    }

    /**
     * Disconnects two nodes. After invoking this function, the two nodes will no longer be neighbors from each other.
     *
     * @param nodeA one of the nodes to disconnect
     * @param nodeB the other node to disconnect
     * @return true if the nodes are disconnected successfully, false if either nodeA or nodeB are
     * null, or if the connection does not exist
     */
    @Override
    public boolean disconnectNodes(V nodeA, V nodeB) {
        boolean result = super.disconnectNodes(nodeA, nodeB);
        return result && super.disconnectNodes(nodeB, nodeA);
    }

    /**
     * Calculates the amount of connections the node has
     *
     * @param node node to use for the calculation
     * @return the number of connections the node has
     */
    public int degree(V node) {
        return getVertex(node).map(vertex -> vertex.edges.size())
                              .orElse(NON_EXISTENT_DEGREE);
    }
}
