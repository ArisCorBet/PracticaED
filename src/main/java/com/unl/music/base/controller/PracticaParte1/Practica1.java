package com.unl.music.base.controller.PracticaParte1;

import com.unl.music.base.controller.data_struct.list.LinkedList;

public class Practica1 {
    private Integer[] matriz;
    private LinkedList<Integer> lista;

    public void cargar() {
        // Carga manual con datos de ejemplo
        matriz = new Integer[]{3, 7, 2, 7, 5, 3, 9, 1, 3};
        lista = new LinkedList<>();

        for (Integer numero : matriz) {
            lista.add(numero);
        }
    }

    private Boolean verificar_numero_arreglo(Integer a) {
        int cont = 0;
        for (int i = 0; i < matriz.length; i++) {
            if (a.equals(matriz[i]))
                cont++;
            if (cont >= 2)
                return true;
        }
        return false;
    }

    public String[] verificar_arreglo() {
        StringBuilder resp = new StringBuilder();
        for (int i = 0; i < matriz.length; i++) {
            if (verificar_numero_arreglo(matriz[i]) && !resp.toString().contains(matriz[i] + "-")) {
                resp.append(matriz[i]).append("-");
            }
        }
        return resp.toString().split("-");
    }

    private Boolean verificar_numero_lista(Integer a) {
        int cont = 0;
        for (int i = 0; i < lista.getLength(); i++) {
            if (a.equals(lista.get(i)))
                cont++;
            if (cont >= 2)
                return true;
        }
        return false;
    }

    public LinkedList<Integer> verificar_lista() {
        LinkedList<Integer> resp = new LinkedList<>();
        for (int i = 0; i < lista.getLength(); i++) {
            Integer val = lista.get(i);
            if (verificar_numero_lista(val) && !resp.contains(val)) {
                resp.add(val);
            }
        }
        return resp;
    }

    public static void main(String[] args) {
        Practica1 p = new Practica1();
        p.cargar();

        
        long startArray = System.nanoTime();
        String[] repetidosArreglo = p.verificar_arreglo();
        long endArray = System.nanoTime();
        long tiempoArreglo = endArray - startArray;

        
        long startLista = System.nanoTime();
        LinkedList<Integer> repetidosLista = p.verificar_lista();
        long endLista = System.nanoTime();
        long tiempoLista = endLista - startLista;

        
        System.out.println("Repetidos en arreglo:");
        for (String s : repetidosArreglo) {
            System.out.println(s);
        }
        System.out.println("Cantidad: " + repetidosArreglo.length);
        System.out.println("Tiempo arreglo (ns): " + tiempoArreglo);

        System.out.println("\nRepetidos en lista:");
        for (int i = 0; i < repetidosLista.getLength(); i++) {
            System.out.println(repetidosLista.get(i));
        }
        System.out.println("Cantidad: " + repetidosLista.getLength());
        System.out.println("Tiempo lista (ns): " + tiempoLista);
    }
}