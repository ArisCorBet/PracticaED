package com.unl.music.base.controller.data_struct.Laberinto;

import com.unl.music.base.controller.data_struct.list.LinkedList;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

public class MazePanel extends JPanel {
    private char[][] maze;
    private LinkedList<String> path;
    private int cellSize = 10;

    public void setMaze(char[][] maze) {
        this.maze = maze;
        repaint();
    }

    public void setPath(LinkedList<String> path) {
        this.path = path;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (maze == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));

        HashSet<String> pathSet = new HashSet<>();
        if (path != null) {
            for (int i = 0; i < path.getLength(); i++) {
                pathSet.add(path.get(i));  // formato: "r_c"
            }
        }

        for (int r = 0; r < maze.length; r++) {
            for (int c = 0; c < maze[0].length; c++) {
                char ch = maze[r][c];
                int x = c * cellSize;
                int y = r * cellSize;

                if (ch == '0') {
                    g2.setColor(Color.BLACK);
                } else if (ch == 'S') {
                    g2.setColor(Color.GREEN);
                } else if (ch == 'E') {
                    g2.setColor(Color.RED);
                } else if (pathSet.contains(r + "_" + c)) {  // corregido aquí
                    g2.setColor(Color.BLUE);
                } else {
                    g2.setColor(Color.WHITE);
                }

                g2.fillRect(x, y, cellSize, cellSize);
                g2.setColor(Color.GRAY);
                g2.drawRect(x, y, cellSize, cellSize);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (maze == null) return new Dimension(400, 400);
        return new Dimension(maze[0].length * cellSize, maze.length * cellSize);
    }
}
