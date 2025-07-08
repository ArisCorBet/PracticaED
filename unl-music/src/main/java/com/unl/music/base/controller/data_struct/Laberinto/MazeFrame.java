package com.unl.music.base.controller.data_struct.Laberinto;

import com.unl.music.base.controller.data_struct.list.LinkedList;
import com.unl.music.base.controller.data_struct.graphs.DirectedLableGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MazeFrame extends JFrame {
    private MazePanel mazePanel;
    private JTextField sizeField;

    public MazeFrame() {
        setTitle("Generador y Solucionador de Laberintos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        mazePanel = new MazePanel();
        add(new JScrollPane(mazePanel), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Tamaño (30-100):"));
        sizeField = new JTextField("30", 5);
        controlPanel.add(sizeField);

        JButton generateButton = new JButton("Generar y Resolver");
        generateButton.addActionListener(this::generateMaze);
        controlPanel.add(generateButton);

        add(controlPanel, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void generateMaze(ActionEvent e) {
        int size;
        try {
            size = Integer.parseInt(sizeField.getText());
            if (size < 30 || size > 100) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un número entre 30 y 100");
            return;
        }

        Prim2 prim = new Prim2();
        prim.generar(size, size);
        char[][] maze = prim.getMaze();
        mazePanel.setMaze(maze);

        MazeGraph mg = new MazeGraph(maze);
        DirectedLableGraph<String> graph = mg.getGraph();
        String start = mg.getLabel(prim.getStart().r, prim.getStart().c);
        String end = mg.getLabel(prim.getEnd().r, prim.getEnd().c);

        MazeSolver solver = new MazeSolver(graph, start, end);
        LinkedList<String> path = solver.solve();

        System.out.println("Camino encontrado:");
        for (int i = 0; i < path.getLength(); i++) {
            System.out.println(path.get(i));
        }


        mazePanel.setPath(path);

        mazePanel.revalidate();
        mazePanel.repaint();
        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MazeFrame::new);
    }
}
