package com.unl.music.base.controller.data_struct.Laberinto;

import com.unl.music.base.controller.data_struct.graphs.DirectedLableGraph;
import com.unl.music.base.controller.data_struct.graphs.Adjacency;
import com.unl.music.base.controller.data_struct.list.LinkedList;
import com.unl.music.base.controller.data_struct.queque.Queue;

import java.util.HashMap;

public class MazeSolver {
    private DirectedLableGraph<String> graph;
    private String startLabel;
    private String endLabel;

    public MazeSolver(DirectedLableGraph<String> graph, String startLabel, String endLabel) {
        this.graph = graph;
        this.startLabel = startLabel;
        this.endLabel = endLabel;
    }

    public LinkedList<String> solve() {
        System.out.println("Start label: " + startLabel);
        System.out.println("End label: " + endLabel);
        System.out.println("Label start existe en grafo: " + (graph.getVertex(startLabel) != null));
        System.out.println("Label end existe en grafo: " + (graph.getVertex(endLabel) != null));
    
        HashMap<String, Float> distance = new HashMap<>();
    
        HashMap<String, String> previous = new HashMap<>();
        LinkedList<String> visited = new LinkedList<>();
        Queue<String> queue = new Queue<>(graph.nro_vertex());
    
        for (int i = 1; i <= graph.nro_vertex(); i++) {
            String label = graph.getLabel(i);
            distance.put(label, Float.MAX_VALUE);
        }
    
        distance.put(startLabel, 0f);
        queue.queue(startLabel);
    
        System.out.println("----- INICIANDO DIJKSTRA -----");
    
        while (queue.size() > 0) {
            String current = queue.dequeue();
            visited.add(current);
            Float distActual = distance.get(current);
            System.out.println("Nodo actual: " + current);
            System.out.println("Distancia actual: " + distance.get(current));
        
    
            System.out.println("🟢 Nodo actual: " + current + " (distancia acumulada: " + distActual + ")");
    
            LinkedList<Adjacency> adj = graph.adjacencies_label(current);
            for (int i = 0; i < adj.getLength(); i++) {
                Adjacency edge = adj.get(i);
                String neighbor = graph.getLabel(edge.getDestiny());
                float weight = edge.getWieght().isNaN() ? 1 : edge.getWieght();
                float newDist = distActual + weight;
    
                System.out.println("  Vecino: " + neighbor + " con peso: " + weight);
                System.out.println("  Nueva distancia calculada: " + newDist);
                System.out.println("  Distancia actual guardada: " + distance.get(neighbor));
    
                if (newDist < distance.get(neighbor)) {
                    distance.put(neighbor, newDist);
                    previous.put(neighbor, current);
                    queue.queue(neighbor);
                    System.out.println("      ✅ Distancia actualizada: " + newDist + ", viene de: " + current);
                    System.out.println("  → Actualizado y encolado");
                }    
            }
        }
    
        System.out.println("----- FINALIZA DIJKSTRA -----");
    
        // Reconstruir el camino
        LinkedList<String> path = new LinkedList<>();
        String step = endLabel;
        while (step != null && !step.equals(startLabel)) {
            try {
                path.add(step, 0); // insert at front
            } catch (Exception e) {
                e.printStackTrace();
            }
            step = previous.get(step);
        }
    
        if (step != null) {
            try {
                path.add(startLabel, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
        return path;
    }
    
}
