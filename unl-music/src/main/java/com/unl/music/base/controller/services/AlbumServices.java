package com.unl.music.base.controller.services;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.github.javaparser.quality.NotNull;
import com.unl.music.base.controller.dao.dao_models.DaoAlbum;

import com.unl.music.base.models.Album;
import com.unl.music.base.models.Banda;
import com.unl.music.base.models.Cancion;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import io.micrometer.common.lang.NonNull;
import com.unl.music.base.controller.data_struct.list.LinkedList;
import jakarta.validation.constraints.NotEmpty;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@BrowserCallable
@AnonymousAllowed
@Transactional(propagation =Propagation.REQUIRES_NEW)

public class AlbumServices {
    private DaoAlbum da;
    public AlbumServices(){
        da = new DaoAlbum();
    }



    public List<HashMap> listAll() throws Exception{
        return Arrays.asList(da.all().toArray());
    }
    

    

    public List<HashMap> order(String attribute, Integer type)throws Exception{
        return Arrays.asList(da.orderByAlbum(type, attribute).toArray());
    }

    public List<HashMap> search(String attribute, String text, Integer type) throws Exception {
        LinkedList<HashMap<String, Object>> lista = da.search(attribute, text, type);
        if(!lista.isEmpty())
            return Arrays.asList(lista.toArray());
        else
            return new ArrayList<>();
    }





    public void createAlbum(@NotEmpty String nombre, Date fecha) throws Exception {
        if(nombre.trim().length() > 0) {
            da.getObj().setNombre(nombre);
            da.getObj().setFecha(fecha);
            
            if(!da.save())
                throw new  Exception("No se pudo guardar los datos de album");
        }
    }

    public void updateAlbum(@NotEmpty Integer id, @NotEmpty String nombre, Date fecha) throws Exception {
        if(nombre.trim().length() > 0 ) {
            da.setObj(da.listAll().get(id - 1));
            da.getObj().setNombre(nombre);
            da.getObj().setFecha(fecha);

            if(!da.update(id - 1))
                throw new  Exception("No se pudo modifcar los datos de la Album");
        }
    }


    public List<Album> listAlla() {  
        // System.out.println("**********Entro aqui");  
         //System.out.println("lengthy "+Arrays.asList(da.listAll().toArray()).size());    
         return (List<Album>)Arrays.asList(da.listAll().toArray());
     }
   
    
    /*public List<HashMap> listAlbum(){
        List<HashMap> lista = new ArrayList<>();
        if(!da.listAll().isEmpty()) {
            Album [] arreglo = da.listAll().toArray();
           
            for(int i = 0; i < arreglo.length; i++) {
                
                HashMap<String, String> aux = new HashMap<>();
                aux.put("id", arreglo[i].getId().toString(i));                
                aux.put("nombre", arreglo[i].getNombre());
                aux.put("fecha", String.valueOf(arreglo[i].getFecha()));
                lista.add(aux);
            }
        }
        return lista;
    }*/


    }

