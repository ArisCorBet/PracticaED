package com.unl.music.base.controller.services;


import java.util.Arrays;


import java.util.List;

import com.unl.music.base.controller.dao.dao_models.DaoAlbum;

import com.unl.music.base.models.Album;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import io.micrometer.common.lang.NonNull;
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

    public void createAlbum(@NotEmpty String nombre) throws Exception {
        if(nombre.trim().length() > 0 ) {
            da.getObj().setNombre(nombre);
            
            if(!da.save())
                throw new  Exception("No se pudo guardar los datos de album");
        }
    }

    public void updateBanda(Integer id, @NotEmpty String nombre) throws Exception {
        if(id != null && id > 0 && nombre.trim().length() > 0 ) {
            da.setObj(da.listAll().get(id - 1));
            da.getObj().setNombre(nombre);

            if(!da.update(id - 1))
                throw new  Exception("No se pudo modifcar los datos de la Album");
        }
    }

    public List<Album> listAllBanda(){
        return Arrays.asList(da.listAll().toArray());
    }
    public List<Album> listAll() {  
        // System.out.println("**********Entro aqui");  
         //System.out.println("lengthy "+Arrays.asList(da.listAll().toArray()).size());    
         return (List<Album>)Arrays.asList(da.listAll().toArray());
     }
    
}

