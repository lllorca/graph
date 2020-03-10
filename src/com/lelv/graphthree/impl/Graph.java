package com.lelv.graphthree.impl;

import com.lelv.graphthree.AbstractGraph;

public class Graph<V, E> extends AbstractGraph<V, E> {

    @Override
    public boolean connectNodes(V originNode, V destinationNode, E connection) {
        boolean result = super.connectNodes(originNode, destinationNode, connection);
        return result && super.connectNodes(destinationNode, originNode, connection);
    }

    @Override
    public boolean disconnectNodes(V originNode, V destinationNode) {
        boolean result = super.disconnectNodes(originNode, destinationNode);
        return result && super.disconnectNodes(destinationNode, originNode);
    }

    public int degree(V node) {
        return getVertex(node).map(vertex -> vertex.edges.size())
                              .orElse(NON_EXISTENT_DEGREE);
    }
}
