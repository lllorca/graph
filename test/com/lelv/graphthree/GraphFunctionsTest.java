package com.lelv.graphthree;

import com.lelv.graphthree.impl.DirectedGraph;
import com.lelv.graphthree.impl.Graph;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class GraphFunctionsTest {

    @Test
    public void bfs() {
        List<String> result = GraphFunctions.bfs(testGraph(), "a");
        assert result != null;
        assertEquals("[a, b, c, d, e, f, h, g]", result.toString());
    }

    @Test
    public void dfs() {
        List<String> result = GraphFunctions.dfs(testGraph(), "a");
        assert result != null;
        assertEquals("[a, b, e, g, f, c, d, h]", result.toString());
    }

    private DirectedGraph<String, MyWeightedEdge> testGraph() {
        DirectedGraph<String, MyWeightedEdge> graph = new DirectedGraph<>();
        graph.addNodes("a", "b", "c", "d", "e", "f", "g", "h");

        graph.connectNodes("a", "b", new MyWeightedEdge(1));
        graph.connectNodes("a", "c", new MyWeightedEdge(2));
        graph.connectNodes("a", "d", new MyWeightedEdge(3));
        graph.connectNodes("b", "e", new MyWeightedEdge(4));
        graph.connectNodes("b", "f", new MyWeightedEdge(5));
        graph.connectNodes("e", "g", new MyWeightedEdge(6));
        graph.connectNodes("d", "h", new MyWeightedEdge(7));
        return graph;
    }

    @Test
    public void dijkstra() {
        DirectedGraph<String, MyWeightedEdge> graph = testGraph();

        graph.connectNodes("a", "g", new MyWeightedEdge(1));
        graph.connectNodes("g", "d", new MyWeightedEdge(1));
        graph.connectNodes("c", "f", new MyWeightedEdge(1));
        graph.connectNodes("e", "d", new MyWeightedEdge(1));

        HashMap<String, Double> result = GraphFunctions.dijkstra(graph, "a");
        assert result != null;
        assertEquals("{a=0.0, b=1.0, c=2.0, d=2.0, e=5.0, f=3.0, g=1.0, h=9.0}", result.toString());
    }

    @Test
    public void hasPath() {
        DirectedGraph<String, MyWeightedEdge> graph = testGraph();

        assertTrue(GraphFunctions.hasPath(graph, "a", "g"));
        assertFalse(GraphFunctions.hasPath(graph, "h", "g"));
    }

    @Test
    public void isConnected() {
        DirectedGraph<String, MyWeightedEdge> graph = testGraph();
        assertTrue(GraphFunctions.isConnected(graph));

        graph.addNode("z");
        assertFalse(GraphFunctions.isConnected(graph));
    }

    @Test
    public void numberOfComponents() {
        DirectedGraph<String, MyWeightedEdge> graph = testGraph();
        assertEquals(1, GraphFunctions.numberOfComponents(graph));

        graph.addNodes("x");
        assertEquals(2, GraphFunctions.numberOfComponents(graph));

        graph.addNodes("y");
        assertEquals(3, GraphFunctions.numberOfComponents(graph));

        graph.connectNodes("x", "y", new MyWeightedEdge(3));
        assertEquals(2, GraphFunctions.numberOfComponents(graph));

        graph.connectNodes("a", "x", new MyWeightedEdge(3));
        assertEquals(1, GraphFunctions.numberOfComponents(graph));
    }

    @Test
    public void isCutVertex() {
        DirectedGraph<String, Integer> graph = new DirectedGraph<>();

        graph.addNodes("a", "b", "c", "d", "e");
        graph.connectNodes("a", "b", 1);
        graph.connectNodes("b", "c", 2);
        graph.connectNodes("c", "d", 3);
        graph.connectNodes("c", "e", 4);

        assertFalse(GraphFunctions.isCutVertex(graph, "a"));
        assertTrue(GraphFunctions.isCutVertex(graph, "b"));
        assertTrue(GraphFunctions.isCutVertex(graph, "c"));
        assertFalse(GraphFunctions.isCutVertex(graph, "d"));
        assertFalse(GraphFunctions.isCutVertex(graph, "e"));
    }

    @Test
    public void isBridge() {
        Graph<String, Integer> graph = new Graph<>();

        graph.addNodes("a", "b", "c", "d");
        graph.connectNodes("a", "b", 1);
        graph.connectNodes("a", "c", 2);
        graph.connectNodes("b", "d", 3);
        graph.connectNodes("d", "a", 4);

        assertFalse(GraphFunctions.isBridge(graph, "a", "b"));
        assertTrue(GraphFunctions.isBridge(graph, "a", "c"));
        assertFalse(GraphFunctions.isBridge(graph, "b", "d"));
        assertFalse(GraphFunctions.isBridge(graph, "d", "a"));
    }
}
