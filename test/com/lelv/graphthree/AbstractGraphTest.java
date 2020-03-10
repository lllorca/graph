package com.lelv.graphthree;

import com.lelv.graphthree.impl.DirectedGraph;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractGraphTest {

    @Test
    public void addNodes() {
        DirectedGraph<String, Integer> graph = new DirectedGraph<>();
        graph.addNodes("a", "b", "c", "d", "e");

        assertTrue(graph.nodeExists("a"));
        assertTrue(graph.nodeExists("b"));
        assertTrue(graph.nodeExists("c"));
        assertTrue(graph.nodeExists("d"));
        assertTrue(graph.nodeExists("e"));
        assertFalse(graph.nodeExists("f"));

        assertEquals(5, graph.getNumberOfNodes());
        assertEquals("[a, b, c, d, e]", graph.getNodes().toString());

    }

    @Test
    public void removeNodes() {
        DirectedGraph<String, Integer> graph = new DirectedGraph<>();
        graph.addNodes("a", "b", "c", "d", "e");

        assertTrue(graph.nodeExists("b"));
        assertTrue(graph.nodeExists("c"));
        assertEquals(5, graph.getNumberOfNodes());

        graph.removeNode("b");
        graph.removeNode("c");

        assertFalse(graph.nodeExists("b"));
        assertFalse(graph.nodeExists("c"));
        assertEquals(3, graph.getNumberOfNodes());
    }

    @Test
    public void connectNodes() {
        DirectedGraph<String, Integer> graph = new DirectedGraph<>();
        graph.addNodes("a", "b", "c", "d", "e");

        graph.connectNodes("a", "b", 1);
        graph.connectNodes("b", "c", 2);
        graph.connectNodes("c", "d", 3);

        assertEquals(3, graph.getNumberOfConnections());

        assertTrue(graph.connectionExists("a", "b"));
        assertTrue(graph.getConnection("b", "c").isPresent());
        assertEquals(3, (int) graph.getConnection("c", "d").orElse(-1));

        assertFalse(graph.connectionExists("d", "e"));
        assertFalse(graph.getConnection("e", "f").isPresent());
    }

    @Test
    public void disconnectNodes() {
        DirectedGraph<String, Integer> graph = new DirectedGraph<>();
        graph.addNodes("a", "b", "c", "d", "e");

        graph.connectNodes("a", "b", 1);
        graph.connectNodes("b", "c", 2);
        graph.connectNodes("c", "d", 3);

        assertEquals(3, graph.getNumberOfConnections());
        assertTrue(graph.connectionExists("a", "b"));
        assertTrue(graph.connectionExists("c", "d"));

        graph.disconnectNodes("a", "b");
        graph.disconnectNodes("c", "d");

        assertEquals(1, graph.getNumberOfConnections());
        assertFalse(graph.connectionExists("a", "b"));
        assertFalse(graph.connectionExists("c", "d"));

        assertTrue(graph.connectionExists("b", "c"));
    }
}
