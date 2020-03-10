package com.lelv.graphthree.impl;

import com.lelv.graphthree.AbstractGraph;

import java.util.Optional;

public class DirectedGraph<V, E> extends AbstractGraph<V, E> {

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

    public int outDegree(V node) {
        return getVertex(node).map(vertex -> vertex.getNeighbors().size())
                              .orElse(NON_EXISTENT_DEGREE);
    }
}
