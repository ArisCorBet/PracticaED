package com.unl.music.base.controller.data_struct.Laberinto;

import com.unl.music.base.controller.data_struct.graphs.DirectedLableGraph;
import com.unl.music.base.controller.data_struct.list.LinkedList;

import java.util.HashMap;

public class MazeGraph {
    private DirectedLableGraph<String> graph;
    private int rows;
    private int cols;
    private char[][] maze;
    private HashMap<String, Integer> labelToIndex;

    public MazeGraph(char[][] maze) {
        this.maze = maze;
        this.rows = maze.length;
        this.cols = maze[0].length;

        int totalNodes = countWalkableCells(maze);
        this.graph = new DirectedLableGraph<>(totalNodes, String.class);
        this.labelToIndex = new HashMap<>();

        int index = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isWalkable(i, j)) {
                    String label = label(i, j);
                    graph.label_vertex(index, label);
                    labelToIndex.put(label, index);
                    index++;
                }
            }
        }

        // Agregar conexiones
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isWalkable(i, j)) {
                    addEdges(i, j);
                }
            }
        }
    }

    private int countWalkableCells(char[][] maze) {
        int count = 0;
        for (char[] row : maze) {
            for (char c : row) {
                if (c == '1' || c == 'S' || c == 'E') count++;
            }
        }
        return count;
    }

    private void addEdges(int r, int c) {
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        String fromLabel = label(r, c);
        for (int[] dir : dirs) {
            int nr = r + dir[0];
            int nc = c + dir[1];
            if (isWalkable(nr, nc)) {
                String toLabel = label(nr, nc);
                graph.insert_label(fromLabel, toLabel, 1.0f);
                graph.insert_label(toLabel, fromLabel, 1.0f);
            }
        }
    }

    private boolean isWalkable(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols &&
               (maze[r][c] == '1' || maze[r][c] == 'S' || maze[r][c] == 'E');
    }

    private String label(int r, int c) {
        return r + "_" + c;
    }

    public DirectedLableGraph<String> getGraph() {
        return graph;
    }

    public String getLabel(int r, int c) {
        return label(r, c);
    }
}
