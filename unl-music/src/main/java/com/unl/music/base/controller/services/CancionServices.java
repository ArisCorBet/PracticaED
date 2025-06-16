package com.unl.music.base.controller.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.unl.music.base.controller.dao.dao_models.DaoGenero;
import com.github.javaparser.quality.NotNull;
import com.unl.music.base.controller.dao.dao_models.DaoAlbum;

import com.unl.music.base.controller.dao.dao_models.DaoCancion;
import com.unl.music.base.models.Album;
import com.unl.music.base.models.Cancion;
import com.unl.music.base.models.Genero;
import com.unl.music.base.models.TipoArchivoEnum;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import com.unl.music.base.controller.data_struct.list.LinkedList;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@BrowserCallable
@Transactional(propagation = Propagation.REQUIRES_NEW)
@AnonymousAllowed

public class CancionServices {
    private DaoCancion db;

    public CancionServices() {
        db = new DaoCancion();
    }

    public List<HashMap> listAll() throws Exception{
        return Arrays.asList(db.all().toArray());
    }

   

    public List<HashMap> order(String attribute, Integer type)throws Exception{
        return Arrays.asList(db.orderByCancion(type, attribute).toArray());
    }

    public List<HashMap> search(String attribute, String text, Integer type) throws Exception {
        LinkedList<HashMap<String, Object>> lista = db.search(attribute, text, type);
        if(!lista.isEmpty())
            return Arrays.asList(lista.toArray());
        else
            return new ArrayList<>();
    }



    public void createCancion(@NotEmpty String nombre, Integer id_genero, Integer duracion, @NotEmpty String url,
            @NotEmpty String tipo, Integer id_album) throws Exception {
        if (nombre.trim().length() > 0 && url.toString().length() > 0 && tipo.trim().length() > 0 && duracion > 0
                && id_genero > 0 && id_album > 0) {
            db.getObj().setNombre(nombre);
            db.getObj().setDuracion(duracion);
            db.getObj().setId_album(id_album);
            db.getObj().setId_genero(id_genero);
            db.getObj().setTipo(TipoArchivoEnum.valueOf(tipo));
            db.getObj().setUrl(url);
            if (!db.save())
                throw new Exception("No se pudo guardar los datos de la Cancion");
        }
    }

    public void updateCancion(
    @NotNull Integer id, 
    @NotBlank String nombre, 
    @NotNull @Min(1) Integer id_genero, 
    @NotNull @Min(1) Integer duracion,
    @NotBlank String url, 
    @NotBlank String tipo, 
    @NotNull @Min(1) Integer id_album) throws Exception {
    
    // Validación adicional (opcional, ya que las anotaciones hacen lo mismo)
    if (nombre.trim().isEmpty() || url.trim().isEmpty() || tipo.trim().isEmpty()) {
        throw new IllegalArgumentException("Campos requeridos no pueden estar vacíos");
    }

    db.setObj(db.listAll().get(id - 1));
    db.getObj().setNombre(nombre);
    db.getObj().setDuracion(duracion);
    db.getObj().setId_album(id_album);
    db.getObj().setTipo(TipoArchivoEnum.valueOf(tipo));
    db.getObj().setUrl(url);
    db.getObj().setId_genero(id_genero);
    
    if (!db.update(id - 1)) {
        throw new Exception("No se pudo guardar los datos de Cancion");
    }
}

    public List<HashMap> listAlbumCombo() {
        List<HashMap> lista = new ArrayList<>();
        DaoAlbum da = new DaoAlbum();
        if (!db.listAll().isEmpty()) {
            Album[] arreglo = da.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {
                HashMap<String, String> aux = new HashMap<>();
                aux.put("value", arreglo[i].getId().toString(i));
                aux.put("label", arreglo[i].getNombre());
                lista.add(aux);
            }
        }
        return lista;
    }


    public List<HashMap> listGeneroCombo() {
        List<HashMap> lista = new ArrayList<>();
        DaoGenero da = new DaoGenero();
        if (!db.listAll().isEmpty()) {
            Genero[] arreglo = da.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {
                HashMap<String, String> aux = new HashMap<>();
                aux.put("value", arreglo[i].getId().toString(i));
                aux.put("label", arreglo[i].getNombre());
                lista.add(aux);
            }
        }
        return lista;
    }


    public List<Cancion> listAlla() {  
        // System.out.println("**********Entro aqui");  
         //System.out.println("lengthy "+Arrays.asList(da.listAll().toArray()).size());    
         return (List<Cancion>)Arrays.asList(db.listAll().toArray());
     }

    public List<String> listTipo() {
        List<String> lista = new ArrayList<>();
        for (TipoArchivoEnum r : TipoArchivoEnum.values()) {
            lista.add(r.toString());
        }
        return lista;
    }
}