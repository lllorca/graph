package com.lelv.graphone;

public class Graph<V, E> extends GraphAdjList<V, E> {

	@Override
	protected boolean isDirected() {
		return false;
	}


}
