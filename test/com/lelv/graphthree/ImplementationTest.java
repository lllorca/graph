package com.lelv.graphthree;

import org.junit.Test;

import static org.junit.Assert.*;

public class ImplementationTest extends AbstractGraph<String, Integer> {

    @Test
    public void accessVertices() {
        addNodes("a", "b", "c");

        assertEquals(3, getVertices().size());
        assertTrue(getVertex("a").isPresent());
        assertTrue(getVertex("b").isPresent());
        assertTrue(getVertex("c").isPresent());
        assertFalse(getVertex("d").isPresent());
    }

    @Test
    public void accessEdges() {
        addNodes("a", "b", "c");
        connectNodes("a", "b", 1);
        connectNodes("b", "c", 2);

        assertTrue(getEdge("a", "b").isPresent());
        assertTrue(getEdge("b", "c").isPresent());
        assertFalse(getEdge("c", "c").isPresent());
    }

    @Test
    public void toggleVertexVisit() {
        addNodes("a", "b", "c");
        getVertices().forEach(vertex -> vertex.visited = true);

        clearVisit();
        assertTrue(getVertices().stream().noneMatch(vertex -> vertex.visited));
    }

    @Test
    public void vertexEquality() {
        addNodes("a", "b");

        Vertex vertexA = getVertex("a").get();
        Vertex vertexB = getVertex("b").get();

        assertNotEquals(vertexA.hashCode(), vertexB.hashCode());
        assertNotEquals(vertexA, vertexB);

        assertEquals(vertexA.hashCode(), vertexA.hashCode());
        assertEquals(vertexA, vertexA);
    }

    @Test
    public void edgeEquality() {
        addNodes("a", "b", "c");

        connectNodes("a", "b", 1);
        connectNodes("b", "a", 1);
        connectNodes("c", "b", 1);

        Edge edgeAB = getEdge("a", "b").get();
        Edge edgeBA = getEdge("b", "a").get();
        Edge edgeCB = getEdge("c", "b").get();

        assertNotEquals(edgeAB.hashCode(), edgeBA.hashCode());
        assertNotEquals(edgeAB.hashCode(), edgeCB.hashCode());

        assertNotEquals(edgeAB, edgeBA);
        assertNotEquals(edgeAB, edgeCB);

        assertEquals(edgeAB, edgeAB);
        assertEquals(edgeBA, edgeBA);
        assertEquals(edgeCB, edgeCB);
    }
}
