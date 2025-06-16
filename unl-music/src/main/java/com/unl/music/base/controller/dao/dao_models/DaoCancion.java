package com.unl.music.base.controller.dao.dao_models;

import com.unl.music.base.models.Cancion;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Map;

import org.checkerframework.checker.units.qual.t;

import com.unl.music.base.controller.dao.AdapterDao;
import com.unl.music.base.controller.services.Utiles;
import com.unl.music.base.controller.data_struct.list.LinkedList;
import com.unl.music.base.models.Genero;
import com.unl.music.base.models.TipoArchivoEnum;

public class DaoCancion extends AdapterDao<Cancion> {
    private Cancion obj;

    public DaoCancion() {
        super(Cancion.class);
    }
    public Cancion getObj() {
        if (obj == null)
            this.obj = new Cancion();
        return this.obj;
    }
    public void setObj(Cancion obj) {
        this.obj = obj;
    }
    public Boolean save() {
        try {
            obj.setId(listAll().getLength() + 1);
            this.persist(obj);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return false;
        }
    }

    public Boolean listar() {
        try {
            this.listAll();
            for (int i = 0; i > this.listAll().getLength(); i++) {
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return false;
        }
    }

    public LinkedList<HashMap<String, Object>> all() throws Exception {
        LinkedList<HashMap<String, Object>> lista = new LinkedList<>();
        if (!this.listAll().isEmpty()) {
            Cancion[] arreglo = this.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {
                lista.add(toDict(arreglo[i], i));
            }
        }
        return lista;
    }

    private HashMap<String, Object> toDict(Cancion arreglo, Integer i) {
        DaoGenero dg = new DaoGenero();
        DaoAlbum da = new DaoAlbum();
        HashMap<String, Object> aux = new HashMap<>();
        aux.put("id", arreglo.getId().toString());
        aux.put("duracion", arreglo.getDuracion());
        aux.put("nombre", arreglo.getNombre());
        aux.put("url", arreglo.getUrl());
        aux.put("genero", dg.listAll().get(arreglo.getId_genero() - 1).getNombre());
        aux.put("tipo", arreglo.getTipo().toString());
        aux.put("album", da.listAll().get(arreglo.getId_album() - 1).getNombre());
        
        return aux;
    }

    public LinkedList<HashMap<String, Object>> orderByCancion(Integer type, String attribute) throws Exception {
        LinkedList<HashMap<String, Object>> lista = all();
        if (!listAll().isEmpty()) {
            HashMap arr[] = lista.toArray();
            quickSort(arr, 0, arr.length - 1, type, attribute);
            lista.toList(arr);
        }
        return lista;
    }

    //IMplementar metodo de ordeenacion
    //1. Quiksort

    //METODO DE ORDENACION QUICKSORT

    public void quickSort(HashMap arr[], int begin, int end, Integer type, String attribute) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end, type, attribute);

            quickSort(arr, begin, partitionIndex - 1, type, attribute);
            quickSort(arr, partitionIndex + 1, end, type, attribute);
        }
    }

    /*
     * public HashMap<String, Object> BinarySearchRecursive(HashMap<String, Object>
     * arr[], int a, int b, String attribute){
     * if(b < 1){
     * return null;
     * }
     * int n = a + (b=1)/2;
     * if(arr[].ge)
     * }*/
     

    private int partition(HashMap<String, Object> arr[], int begin, int end, Integer type, String attribute) {
        HashMap<String, Object> pivot = arr[end];
        int i = (begin - 1);
        if (type == Utiles.ASCENDENTE) {
            for (int j = begin; j < end; j++) {
                if (arr[j].get(attribute).toString().compareTo(pivot.get(attribute).toString()) < 0) {
                    // if (arr[j] <= pivot) {
                    i++;
                    HashMap<String, Object> swapTemp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = swapTemp;
                }
            }
        } else {
            for (int j = begin; j < end; j++) {
                if (arr[j].get(attribute).toString().compareTo(pivot.get(attribute).toString()) > 0) {
                    i++;
                    HashMap<String, Object> swapTemp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = swapTemp;
                }
            }
        }
        HashMap<String, Object> swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

    //metodos de busqueda
    //1. bsuqueda binaria
    //2. busqueda lineal

    //METODO DE BUSQUEDA LINEAL SECUENCIAL
     public LinkedList<HashMap<String, Object>> search(String attribute, String text, Integer type) throws Exception {
        LinkedList<HashMap<String, Object>> lista = all();
        LinkedList<HashMap<String, Object>> resp = new LinkedList<>();
        
        if (!lista.isEmpty()) {
            HashMap<String, Object>[] arr = lista.toArray();
            System.out.println(attribute+" "+text+" ** *** * * ** * * * *");
            switch (type) {
                case 1:
                System.out.println(attribute+" "+text+" UNO");
                    for (HashMap m : arr) {
                        if (m.get(attribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                            resp.add(m);
                        }
                    }
                    break;
                case 2:
                System.out.println(attribute+" "+text+" DOS");
                    for (HashMap m : arr) {
                        if (m.get(attribute).toString().toLowerCase().endsWith(text.toLowerCase())) {
                            resp.add(m);
                        }
                    }
                    break;
                default:
                System.out.println(attribute+" "+text+" TRES");
                    for (HashMap m : arr) {
                        System.out.println("***** "+m.get(attribute)+"   "+attribute);
                        if (m.get(attribute).toString().toLowerCase().contains(text.toLowerCase())) {
                            resp.add(m);
                        }
                    }
                    break;
            }
        }
        return resp;
    }

    public static void main(String[] args) {
        DaoCancion dc = new DaoCancion();
    }

}