package com.lelv.graphthree;

import com.lelv.graphthree.impl.DirectedGraph;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class GraphFunctionsTest {

    @Test
    public void bfs() {
        List<String> result = GraphFunctions.bfs(testingGraph(), "a");
        assert result != null;
        assertEquals("[a, b, c, d, e, f, h, g]", result.toString());
    }

    @Test
    public void dfs() {
        List<String> result = GraphFunctions.dfs(testingGraph(), "a");
        assert result != null;
        assertEquals("[a, b, e, g, f, c, d, h]", result.toString());
    }

    private DirectedGraph<String, MyWeightedEdge> testingGraph() {
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
        DirectedGraph<String, MyWeightedEdge> graph = testingGraph();

        graph.connectNodes("a", "g", new MyWeightedEdge(1));
        graph.connectNodes("g", "d", new MyWeightedEdge(1));
        graph.connectNodes("c", "f", new MyWeightedEdge(1));
        graph.connectNodes("e", "d", new MyWeightedEdge(1));

        HashMap<String, Double> result = GraphFunctions.dijkstra(graph, "a");
        assert result != null;
        assertEquals("{a=0.0, b=1.0, c=2.0, d=2.0, e=5.0, f=3.0, g=1.0, h=9.0}", result.toString());

    }
}
