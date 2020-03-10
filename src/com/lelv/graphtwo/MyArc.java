package com.lelv.graphtwo;

public class MyArc implements ArcGraph {

    private final int value;

    public MyArc(int value) {
        this.value = value;
    }

    @Override
    public Double getValue() {
        return (double) value;
    }
}
