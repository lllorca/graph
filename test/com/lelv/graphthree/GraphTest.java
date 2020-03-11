package com.lelv.graphthree;

import com.lelv.graphthree.impl.Graph;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GraphTest {

    @Test
    public void degree() {
        Graph<String, Integer> graph = new Graph<>();
        graph.addNodes("a", "b", "c", "d");

        graph.connectNodes("a", "b", 1);
        graph.connectNodes("a", "c", 1);
        graph.connectNodes("b", "c", 1);
        graph.connectNodes("a", "d", 1);

        assertEquals(3, graph.degree("a"));
        assertEquals(2, graph.degree("b"));
        assertEquals(2, graph.degree("c"));
        assertEquals(1, graph.degree("d"));
        assertEquals(Graph.NON_EXISTENT_DEGREE, graph.degree("e"));
    }
}
