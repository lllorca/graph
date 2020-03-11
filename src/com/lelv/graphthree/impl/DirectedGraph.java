package com.lelv.graphthree.impl;

import com.lelv.graphthree.AbstractGraph;

import java.util.Optional;

public class DirectedGraph<V, E> extends AbstractGraph<V, E> {

    /**
     * Calculates the in degree of a node. This equals the amount of nodes that have this node as a neighbor
     *
     * @param node the node to which calculate the in degree
     * @return the in degree of the node
     */
    public int inDegree(V node) {
        Optional<Vertex> optional = getVertex(node);
        if (!optional.isPresent()) {
            return NON_EXISTENT_DEGREE;
        }
        Vertex originVertex = optional.get();
        return (int) getVertices().stream()
                                  .filter(vertex -> !vertex.equals(originVertex))
                                  .flatMap(vertex -> vertex.getNeighbors().stream())
                                  .filter(neighbor -> neighbor.equals(originVertex))
                                  .count();
    }

    /**
     * Calculates the out degree of a node. This equals the amount of nodes that this node has as neighbors
     *
     * @param node the node to which calculate the out degree
     * @return the out degree of the node
     */
    public int outDegree(V node) {
        return getVertex(node).map(vertex -> vertex.getNeighbors().size())
                              .orElse(NON_EXISTENT_DEGREE);
    }
}
