package com.unl.music.base.controller.PracticaParte1;


import com.unl.music.base.controller.data_struct.graphs.DirectedGraph;
import com.unl.music.base.controller.data_struct.graphs.DirectedLableGraph;
import com.unl.music.base.controller.data_struct.graphs.Graph;
import com.unl.music.base.controller.data_struct.list.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;



public class Practica3 {

    public LinkedList<Integer> cargarDatos(String fileName) {
    LinkedList<Integer> list = new LinkedList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            if (!linea.trim().isEmpty()) {
                list.add(Integer.parseInt(linea.trim()));
            }
        }
        System.out.println("Archivo cargado correctamente");
    } catch (Exception e) {
        System.out.println("Error cargando archivo: " + e.getMessage());
    }
    return list;
}


    // Metodo Quick Sort
    public void QSort(LinkedList<Integer> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            QSort(list, low, pi - 1);
            QSort(list, pi + 1, high);
        }
    }
    private int partition(LinkedList<Integer> list, int low, int high) {
        int pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (list.get(j) < pivot) {
                i++;
                swap(list, i, j);
            }
        }
        swap(list, i + 1, high);
        return i + 1;
    }
    private void swap(LinkedList<Integer> list, int i, int j) {
        int temp = list.get(i);
        list.update(list.get(j), i);
        list.update(temp, j);
    }





    
    // Metodo ShellSort
    public void sSort(LinkedList<Integer> list) {
        int n = list.getLength();
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = list.get(i);
                int j;
                for (j = i; j >= gap && list.get(j - gap) > temp; j -= gap) {
                    list.update(list.get(j - gap), j);
                }
                list.update(temp, j);
            }
        }
    }

    public void printList(LinkedList<Integer> list) {
        for (int i = 0; i < list.getLength(); i++) {
            System.out.print(list.get(i));
            if (i < list.getLength() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }



    public static void main(String[] args) {
        /*Practica3 app = new Practica3();
        LinkedList<Integer> listaQuick = app.cargarDatos("data.txt");
        LinkedList<Integer> listaShell = app.cargarDatos("data.txt");

        // Quick Sort
        long inicioQuick = System.nanoTime();
        app.QSort(listaQuick, 0, listaQuick.getLength() - 1);
        long finQuick = System.nanoTime();
        //System.out.println("\n La lista ordenada con  el metodo Quick Sort es:");
        //app.printList(listaQuick);
        System.out.println("Tiempo del metodo Quick Sort: " + (finQuick - inicioQuick) + " ns");

        // Shell Sort
        long inicioShell = System.nanoTime();
        app.sSort(listaShell);
        long finShell = System.nanoTime();
        //System.out.println("\nLista ordenada con Shell Sort:");
        //app.printList(listaShell);
        System.out.println("Tiempo del metodo Shell Sort: " + (finShell - inicioShell) + " ns");/* */
        
        /*Graph grafo = new DirectedGraph(5);
        grafo.insert(1, 5, 0.65f);
        grafo.insert(2, 4, 0.85f);
        System.out.println(grafo.toString()); */

        DirectedLableGraph gd = new DirectedLableGraph<>(5, String.class);
        gd.label_vertex(1, "Nole");
        gd.label_vertex(2, "Anahi");
        gd.label_vertex(3, "Douglas");
        gd.label_vertex(4, "Ariana");
        gd.label_vertex(5, "Maria");
        gd.insert_label("Maria", "Douglas", 10.56f);
        gd.insert_label("Maria", "Ariana", 1.67f);
        gd.insert_label("Nole", "Anahi", 5.67f);
        System.out.println(gd.toString());
    
    
    }
    
}
