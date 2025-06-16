package com.unl.music.base.controller.services;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import com.unl.music.base.controller.data_struct.list.LinkedList;

import jakarta.validation.constraints.NotEmpty;
import com.unl.music.base.controller.dao.dao_models.DaoGenero;
import com.unl.music.base.models.Genero;



@BrowserCallable
@Transactional(propagation = Propagation.REQUIRES_NEW)
@AnonymousAllowed

public class GeneroServices {
    private DaoGenero da;

    public GeneroServices() {
        da = new DaoGenero();
    }



    public List<HashMap> listAll() throws Exception{
        return Arrays.asList(da.all().toArray());
    }

   

    public List<HashMap> order(String attribute, Integer type)throws Exception{
        return Arrays.asList(da.orderByCancion(type, attribute).toArray());
    }

    public List<HashMap> search(String attribute, String text, Integer type) throws Exception {
        LinkedList<HashMap<String, Object>> lista = da.search(attribute, text, type);
        if(!lista.isEmpty())
            return Arrays.asList(lista.toArray());
        else
            return new ArrayList<>();
    }


    public void createGenero(@NotEmpty String nombre) throws Exception{
        da.getObj().setNombre(nombre);
        if(!da.save())
            throw new  Exception("No se pudo guardar los datos de Genero");
    }

    public void updateGenero(@NotEmpty Integer id, @NotEmpty String nombre) throws Exception{
        da.setObj(da.listAll().get(id));
        da.getObj().setNombre(nombre);
        if(!da.update(id))
            throw new  Exception("No se pudo modificar los datos del artista");
    }

    public List<Genero> list(Pageable pageable) {        
        return Arrays.asList(da.listAll().toArray());
    }

    public List<Genero> listAlla() {  
           
        return (List<Genero>)Arrays.asList(da.listAll().toArray());
    }

    
    
}