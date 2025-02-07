package com.example.controller.dao.implement;


import com.example.controller.tda.list.LinkedList;
import com.example.controller.tda.list.graph.Adycencia;

public class GraphData<E> {
    public E[] labels;
    public LinkedList<Adycencia>[] adjacencyList; 

    public GraphData(E[] labels, LinkedList<Adycencia>[] adjacencyList) {
        this.labels = labels;
        this.adjacencyList = adjacencyList;
    }
}
