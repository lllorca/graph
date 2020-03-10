package com.lelv.graphone;

public class TestGraph {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Graph<Integer, Integer> g = new Graph<Integer, Integer>();
		
		g.addVertex(1);
		g.addVertex(2);
		g.addVertex(3);
		g.addVertex(4);
		g.addVertex(5);
		g.addVertex(6);
		g.addVertex(7);
		g.addVertex(8);
		
		g.addArc(1, 2, 12);
		g.addArc(1, 6, 16);
		g.addArc(2, 3, 23);
		g.addArc(3, 4, 34);
		g.addArc(3, 5, 35);
		g.addArc(4, 5, 45);
		g.addArc(6, 7, 67);
		g.addArc(6, 8, 68);

		System.out.println(g.arcCount());
		System.out.println(g.DFS(1));
		System.out.println(g.DFS(2));
		
		System.out.println(g.isConnected());
		g.addVertex(9);
		System.out.println(g.isConnected());
		System.out.println("DFS " + g.DFS(2));
		System.out.println("DFS " + g.DFS(9));
		System.out.println("BFS " + g.BFS(2));
		System.out.println("BFS " + g.BFS(9));
		g.addArc(5, 7, 57);
		System.out.println("DFS " + g.DFS(2));
		System.out.println("BFS " + g.BFS(2));
	}
}
