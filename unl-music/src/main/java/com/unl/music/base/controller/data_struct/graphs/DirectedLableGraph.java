package com.unl.music.base.controller.data_struct.graphs;

import java.lang.reflect.Array;
import java.util.HashMap;
import com.unl.music.base.controller.data_struct.list.LinkedList;

public class DirectedLableGraph <E> extends DirectedGraph{
   
    protected E labels[];
    protected HashMap<E, Integer> dicVertex;
    private Class clazz;
    
    public DirectedLableGraph(Integer nro_vertex, Class clazz) {
        super(nro_vertex);
        this.clazz = clazz;
        this.labels = (E[]) Array.newInstance(clazz, nro_vertex +1);
        dicVertex= new HashMap<>(nro_vertex);
    }

    public Adjacency exists_edge_label(E o, E d) {
        if (isLabelsGraph()){
            return exists_edge(getVertex(o), getVertex(d));
        } return null;
    }

    public void insert_label(E o, E d, Float weight){
        if (isLabelsGraph()){
            insert(getVertex(o), getVertex(d), weight);
        } 
    }

    public void insert_label(E o, E d){
        if (isLabelsGraph()){
            insert(getVertex(o), getVertex(d), Float.NaN);
        } 
    }

    public LinkedList<Adjacency> adjacencies_label(E o){
        if (isLabelsGraph()){
            return adjacencies(getVertex(o));
        }  return new LinkedList<>();
    }

    public void label_vertex(Integer vertex, E data) {
        labels[vertex] = data;
        dicVertex.put(data, vertex);
    }


    public Boolean isLabelsGraph() {
        Boolean band = true;
        for(int i = 1; i <= nro_vertex(); i++){
            if (labels[i] == null){
                band = false;
                break;
            }
        }

        return band;
    }
    public Integer getVertex(E label){
        return dicVertex.get(label);
    }

    public E getLabel(Integer i) {
        return labels[i];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i=1; i <= nro_vertex(); i++){
            sb.append("Vertex = ").append(getLabel(i)).append("\n");
            LinkedList<Adjacency> list = adjacencies(i);
            if(!list.isEmpty()){
                Adjacency[] matrix = list.toArray();
                for (Adjacency ad:matrix) {
                    sb.append("\tAjacency").append("\n").append("\t Vertex = ").append(String.valueOf(getLabel(ad.getDestiny())));
                    if (!ad.getWieght().isNaN()) {
                        sb.append(" weight = " + ad.getWieght().toString()).append("\n");
                    }
                }

            }
        }

        return sb.toString();
    }
}
