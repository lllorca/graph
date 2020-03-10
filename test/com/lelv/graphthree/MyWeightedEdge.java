package com.lelv.graphthree;

public class MyWeightedEdge implements WeightedEdge {

    private int value;

    public MyWeightedEdge(int value) {
        this.value = value;
    }

    @Override
    public Integer getWeight() {
        return value;
    }
}
