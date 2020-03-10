package com.lelv.graphthree;

import com.lelv.graphthree.impl.DirectedGraph;
import org.junit.Test;

import static org.junit.Assert.*;

public class DirectedGraphTest {

    @Test
    public void inDegree() {
        DirectedGraph<String, Integer> graph = testingGraph();

        assertEquals(0, graph.inDegree("a"));
        assertEquals(1, graph.inDegree("b"));
        assertEquals(2, graph.inDegree("c"));

    }

    @Test
    public void outDegree() {
        DirectedGraph<String, Integer> graph = testingGraph();

        assertEquals(2, graph.outDegree("a"));
        assertEquals(1, graph.outDegree("b"));
        assertEquals(0, graph.outDegree("c"));

    }

    private DirectedGraph<String, Integer> testingGraph() {
        DirectedGraph<String, Integer> graph = new DirectedGraph<>();
        graph.addNodes("a", "b", "c");

        graph.connectNodes("a", "b", 1);
        graph.connectNodes("a", "c", 1);
        graph.connectNodes("b", "c", 1);
        return graph;
    }
}
