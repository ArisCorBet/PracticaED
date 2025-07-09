package com.unl.music.base.controller.data_struct.Laberinto;

import com.unl.music.base.controller.data_struct.graphs.DirectedLableGraph;
import com.unl.music.base.controller.data_struct.graphs.Adjacency;
import com.unl.music.base.controller.data_struct.list.LinkedList;

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
        HashMap<String, Float> distance = new HashMap<>();
        HashMap<String, String> previous = new HashMap<>();
        LinkedList<String> visited = new LinkedList<>();
        LinkedList<String> unvisited = new LinkedList<>();

        // Inicializa distancias
        for (int i = 1; i <= graph.nro_vertex(); i++) {
            String label = graph.getLabel(i);
            distance.put(label, Float.MAX_VALUE);
            unvisited.add(label);
        }

        distance.put(startLabel, 0f);

        while (unvisited.getLength() > 0) {
            // Buscar el nodo no visitado con menor distancia
            String current = null;
            float minDist = Float.MAX_VALUE;
            for (int i = 0; i < unvisited.getLength(); i++) {
                String node = unvisited.get(i);
                if (distance.get(node) < minDist) {
                    minDist = distance.get(node);
                    current = node;
                }
            }

            if (current == null) break; // no hay caminos restantes

try {
    unvisited.delete(findIndex(unvisited, current));
} catch (Exception e) {
    e.printStackTrace();
}
visited.add(current);


            if (current.equals(endLabel)) break;

            LinkedList<Adjacency> adj = graph.adjacencies_label(current);
            for (int i = 0; i < adj.getLength(); i++) {
                Adjacency edge = adj.get(i);
                String neighbor = graph.getLabel(edge.getDestiny());
                float weight = edge.getWieght().isNaN() ? 1 : edge.getWieght();
                float newDist = distance.get(current) + weight;

                if (newDist < distance.get(neighbor)) {
                    distance.put(neighbor, newDist);
                    previous.put(neighbor, current);
                }
            }
        }

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

    private int findIndex(LinkedList<String> list, String value) {
        for (int i = 0; i < list.getLength(); i++) {
            if (list.get(i).equals(value)) return i;
        }
        return -1;
    }
}
