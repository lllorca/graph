package com.lelv.graphone;

public class DiGraph<V,E> extends GraphAdjList<V,E> {

	@Override
	protected boolean isDirected() {
		return true;
	}

}
