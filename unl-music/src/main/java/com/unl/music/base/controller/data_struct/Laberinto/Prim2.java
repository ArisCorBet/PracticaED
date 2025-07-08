package com.unl.music.base.controller.data_struct.Laberinto;

import com.unl.music.base.controller.data_struct.list.LinkedList;

public class Prim2 {

    public static class Point {
        public int r, c;
        public Point parent;

        public Point(int r, int c, Point parent) {
            this.r = r;
            this.c = c;
            this.parent = parent;
        }

        public Point opposite() {
            if (parent == null) return null;
            if (r != parent.r) return new Point(r + (r - parent.r), c, this);
            if (c != parent.c) return new Point(r, c + (c - parent.c), this);
            return null;
        }
    }

    private char[][] maze;
    private Point start;
    private Point end;

    public char[][] getMaze() {
        return maze;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public void generar(int rows, int cols) {
        maze = new char[rows][cols];

        // Inicializa todas las celdas como pared ('0')
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = '0';
            }
        }

        // Punto de inicio aleatorio
        start = new Point((int) (Math.random() * rows), (int) (Math.random() * cols), null);
        maze[start.r][start.c] = 'S';

        LinkedList<Point> frontier = new LinkedList<>();
        addNeighbors(start, frontier);

        Point last = null;

        while (frontier.getLength() > 0) {
            int randIndex = (int) (Math.random() * frontier.getLength());
            Point current = frontier.get(randIndex);

            try {
                frontier.delete(randIndex);
            } catch (Exception e) {
                continue;
            }

            Point opposite = current.opposite();
            if (opposite != null && isInBounds(opposite.r, opposite.c, rows, cols)) {
                if (maze[current.r][current.c] == '0' && maze[opposite.r][opposite.c] == '0') {
                    maze[current.r][current.c] = '1';
                    maze[opposite.r][opposite.c] = '1';
                    last = opposite;
                    addNeighbors(opposite, frontier);
                }
            }
        }

        if (last != null) {
            maze[last.r][last.c] = 'E';
            end = last;
        }
    }

    private void addNeighbors(Point p, LinkedList<Point> list) {
        int[][] dirs = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1} // arriba, abajo, izquierda, derecha
        };

        for (int[] dir : dirs) {
            int nr = p.r + dir[0];
            int nc = p.c + dir[1];
            if (isInBounds(nr, nc, maze.length, maze[0].length) && maze[nr][nc] == '0') {
                list.add(new Point(nr, nc, p));
            }
        }
    }

    private boolean isInBounds(int r, int c, int rows, int cols) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    public static void main(String[] args) {
        Prim2 p = new Prim2();
        p.generar(30, 30);
        char[][] lab = p.getMaze();
        for (char[] row : lab) {
            for (char cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
    }
}
