import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Button, ComboBox, DatePicker, Dialog, Grid, GridColumn, GridItemModel, GridSortColumn, HorizontalLayout, Icon, NumberField, Select, TextField, VerticalLayout } from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import { CancionServices, TaskService } from 'Frontend/generated/endpoints';
import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';

import Cancion from 'Frontend/generated/com/unl/music/base/models/Cancion';
import { useCallback, useEffect, useState } from 'react';

export const config: ViewConfig = {
  title: 'Cancion',
  menu: {
    icon: 'vaadin:clipboard-check',
    order: 1,
    title: 'Cancion',
  },
};


type CancionEntryFormProps = {
  onCancionCreated?: () => void;
};

/*type CancionEntryFormPropsUpdate = ()=> {
  onCancionUpdated?: () => void;
};*/

type editarCancionEntryFormUpdateProps = {
  arguments: any;
  onCancionUpdate?: () => void;
};





//GUARDAR CANCION
function CancionEntryForm(props: CancionEntryFormProps) {
  const nombre = useSignal('');
  const genero = useSignal('');
  const album = useSignal('');
  const duracion = useSignal('');
  const url = useSignal('');
  const tipo = useSignal('');
  const createCancion = async () => {
    try {
      if (nombre.value.trim().length > 0 && genero.value.trim().length > 0) {
      const id_genero = parseInt(genero.value)+1;
      const id_album= parseInt(album.value)+1;
        await CancionServices.createCancion(nombre.value, id_genero, parseInt(duracion.value), url.value, tipo.value, id_album);
        if (props.onCancionCreated) {
          props.onCancionCreated();
        }
        nombre.value = '';
        genero.value = '';
        album.value= '';
        duracion.value= '';
        url.value= '';
        tipo.value= '';

        dialogOpened.value = false;
        Notification.show('Cancion creada', { duration: 5000, position: 'bottom-end', theme: 'success' });
      } else {
        Notification.show('No se pudo crear, faltan datos', { duration: 5000, position: 'top-center', theme: 'error' });
      }

    } catch (error) {
      handleError(error);
    }
  };
  
  let listaGenero = useSignal<String[]>([]);
  useEffect(() => {
    CancionServices.listGeneroCombo().then(data =>
      listaGenero.value = data
    );
  }, []);
  let listaAlbum = useSignal<String[]>([]);
  useEffect(() => {
    CancionServices.listAlbumCombo().then(data =>
      listaAlbum.value = data
    );
  }, []);
  let listaTipo = useSignal<String[]>([]);
  useEffect(() => {
    CancionServices.listTipo().then(data =>
      listaTipo.value = data
    );
  }, []);
  
  const dialogOpened = useSignal(false);
  return (
    <>
      <Dialog
        modeless
        headerTitle="Nueva Cancion"
        opened={dialogOpened.value}
        onOpenedChanged={({ detail }) => {
          dialogOpened.value = detail.value;
        }}
        footer={
          <>
            <Button
              onClick={() => {
                dialogOpened.value = false;
              }}
            >
              Cancelar
            </Button>
            <Button onClick={createCancion} theme="primary">
              Registrar
            </Button>
            
          </>
        }
      >
        <VerticalLayout style={{ alignItems: 'stretch', width: '18rem', maxWidth: '100%' }}>
          <TextField label="Nombre del Cancion" 
            placeholder="Ingrese el nombre del Cancion"
            aria-label="Nombre del Cancion"
            value={nombre.value}
            onValueChanged={(evt) => (nombre.value = evt.detail.value)}
          />
          <NumberField label="Duracion" 
            placeholder="Ingrese la duracion de la cancion"
            aria-label="Nombre la duracion de la cancion"
            value={duracion.value}
            onValueChanged={(evt) => (duracion.value = evt.detail.value)}
          />
          <TextField label="Link de la cancion" 
            placeholder="Ingrese el link de la cancion"
            aria-label="Nombre el link de la cancion"
            value={url.value}
            onValueChanged={(evt) => (url.value = evt.detail.value)}
          />
          <ComboBox label="Tipo Archivo" 
            items={listaTipo.value}
            placeholder='Seleccione un tipo de archivo'
            aria-label='Seleccione un tipo de archivo de la lista'
            value={tipo.value}
            onValueChanged={(evt) => (tipo.value = evt.detail.value)}
            />
          <ComboBox label="Genero" 
            items={listaGenero.value}
            placeholder='Seleccione un genero'
            aria-label='Seleccione un genero de la lista'
            value={genero.value}
            onValueChanged={(evt) => (genero.value = evt.detail.value)}
            />
            <ComboBox label="Album" 
            items={listaAlbum.value}
            placeholder='Seleccione un album'
            aria-label='Seleccione un album de la lista'
            value={album.value}
            onValueChanged={(evt) => (album.value = evt.detail.value)}
            />
            
        </VerticalLayout>
      </Dialog>
      <Button
            onClick={() => {
              dialogOpened.value = true;
            }}
          >
            Agregar
          </Button>
    </>
  );
}







//EDITAR CANCION
function EditarCancionEntryFormUpdate(props: editarCancionEntryFormUpdateProps) {
  //console.log(props);

  const dialogOpened = useSignal(false);
  useEffect(() => {
    console.log("Editando canción con datos:", props.arguments);

    nombre.value = props.arguments.nombre ?? '';
    duracion.value = props.arguments.duracion?.toString() ?? '';
    url.value = props.arguments.url ?? '';
    tipo.value = props.arguments.tipo ?? '';
    id_genero.value = props.arguments.id_genero?.toString() ?? '';
    id_album.value = props.arguments.id_album?.toString() ?? '';
  }, []);
  
  const idCancion = useSignal(props.arguments.idCancion);
  const nombre = useSignal('');
  const id_genero = useSignal('');
  const id_album = useSignal('');
  const duracion = useSignal('');
  const url = useSignal('');
  const tipo = useSignal('');
  
  const open = () => {
    dialogOpened.value = true;
  };

  const close = () => {
    dialogOpened.value = false;
  };


  const updateCancion = async () => {
    try {
      const id = idCancion.value;
      const dur = parseInt(duracion.value);
      const idGen = parseInt(id_genero.value);
      const idAlb = parseInt(id_album.value);
  
      // Validación mejorada
      if (
        nombre.value.trim().length > 0 &&
        url.value.trim().length > 0 &&
        tipo.value.trim().length > 0 &&
        !isNaN(dur) && dur > 0 &&
        !isNaN(idGen) && idGen > 0 &&
        !isNaN(idAlb) && idAlb > 0
      ) {
        await CancionServices.updateCancion(
          id,
          nombre.value,
          idGen,
          dur,
          url.value,
          tipo.value,
          idAlb
        );
        if (props.onCancionUpdate) {
          props.onCancionUpdate();
        }

        // Limpiar valores
        nombre.value = '';
        id_genero.value = '';
        id_album.value = '';
        duracion.value = '';
        url.value = '';
        tipo.value = '';
          
        close();

          dialogOpened.value = false;
          Notification.show('Cancion actualizada exitosamente', { duration: 5000, position: 'bottom-end', theme: 'success' });
        } else {
          Notification.show('Por favor complete todos los campos correctamente', { 
            duration: 5000, 
            position: 'top-center', 
            theme: 'error' 
          });
        }
      } catch (error) {
        console.error('Error al actualizar:', error);
        Notification.show('Error al actualizar la canción', { 
          duration: 5000, 
          position: 'top-center', 
          theme: 'error' 
        });
      }
    };
    let listaGenero = useSignal<String[]>([]);
    useEffect(() => {
      CancionServices.listGeneroCombo().then(data =>
        listaGenero.value = data
      );
    }, []);
    let listaAlbum = useSignal<String[]>([]);
    useEffect(() => {
      CancionServices.listAlbumCombo().then(data =>
        listaAlbum.value = data
      );
    }, []);
    let listaTipo = useSignal<String[]>([]);
    useEffect(() => {
      CancionServices.listTipo().then(data =>
        listaTipo.value = data
      );
    }, []);
    
  



  return (
    <>
      <Dialog
        aria-label="Editar Cancion"
        draggable
        modeless
        opened={dialogOpened.value}
        onOpenedChanged={(event) => {
          dialogOpened.value = event.detail.value;
        }}
        header={
          <h2
            className="draggable"
            style={{
              flex: 1,
              cursor: 'move',
              margin: 0,
              fontSize: '1.5em',
              fontWeight: 'bold',
              padding: 'var(--lumo-space-m) 0',
            }}
          >
            Editar Cancion
          </h2>
        }
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={updateCancion}>
              Actualizar
            </Button>
          </>
        )}
      >
        <VerticalLayout
          theme="spacing"
          style={{ width: '300px', maxWidth: '100%', alignItems: 'stretch' }}
        >
          <VerticalLayout style={{ alignItems: 'stretch' }}>
            <TextField label="Nombre"
              placeholder='Ingrese el nombre de la cancion'
              aria-label='Ingrese el nombre de la cancion'
              value={nombre.value}
              onValueChanged={(evt) => (nombre.value = evt.detail.value)}
            />
            <TextField label="duracion"
              placeholder='Ingresar la sinopsis de la pelicula'
              aria-label='Ingresar la sinopsis de la pelicula'
              value={duracion.value}
              onValueChanged={(evt) => (duracion.value = evt.detail.value)}
            />
            <TextField label="url"
              placeholder='Ingresar la sinopsis de la pelicula'
              aria-label='Ingresar la sinopsis de la pelicula'
              value={url.value}
              onValueChanged={(evt) => (url.value = evt.detail.value)}
            />
            <ComboBox label="Genero" 
            items={listaGenero.value}
            placeholder='Seleccione un genero'
            aria-label='Seleccione un genero de la lista'
            value={id_genero.value}
            onValueChanged={(evt) => (id_genero.value = evt.detail.value)}
            />
            <ComboBox label="Album" 
            items={listaAlbum.value}
            placeholder='Seleccione un album'
            aria-label='Seleccione un album de la lista'
            value={id_album.value}
            onValueChanged={(evt) => (id_album.value = evt.detail.value)}
            />
            <ComboBox label="Tipo Archivo" 
            items={listaTipo.value}
            placeholder='Seleccione un tipo de archivo'
            aria-label='Seleccione un tipo de archivo de la lista'
            value={tipo.value}
            onValueChanged={(evt) => (tipo.value = evt.detail.value)}
            />
            
            
          </VerticalLayout>
        </VerticalLayout>
      </Dialog>
      <Button onClick={open}>Editar</Button>
    </>
  );
}





//LISTA DE CANCIONES
export default function CancionView() {
  
  const [items, setItems] = useState([]);
  useEffect(() => {
    CancionServices.listAll().then(function (data) {
      //items.values = data;
      setItems(data);
    });
  }, []);


  const order = (event, columnId) => {
    console.log(event);
    const direction = event.detail.value;
    console.log(`Sort direction changed for column ${columnId} to ${direction}`);
    var dir = (direction == 'asc') ? 1 : 2;
    CancionServices.order(columnId, dir).then(function(data){
      setItems(data);
    });
  }


  const callData = () => {
    CancionServices.listAll().then(function (data) {
     
      setItems(data);
    });
  }

  function indexLink({ model }: { model: GridItemModel<Cancion> }) {
    return (
      <span>
        <EditarCancionEntryFormUpdate  arguments={model.item} onCancionUpdate={callData} />
      </span>
    );
  }




  const criterio = useSignal('');
  const texto = useSignal('');
  const itemSelect = [
    {
      label: 'Cancion',
      value: 'nombre',
    },
    {
      label: 'Album',
      value: 'album',
    },
    {
      label: 'Genero',
      value: 'genero',
    },
    {
      label: 'Tipooo de Archivo',
      value: 'tipo',
    },
  ];
  const search = async () => {
    try {
      console.log(criterio.value+" "+texto.value);
      CancionServices.search(criterio.value, texto.value, 0).then(function (data) {
        setItems(data);
      });

      criterio.value = '';
      texto.value = '';

      Notification.show('Busqueda realizada', { duration: 5000, position: 'bottom-end', theme: 'success' });


    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };





  function indexIndex({model}:{model:GridItemModel<Cancion>}) {
    return (
      <span>
        {model.index + 1} 
      </span>
    );
  }




  
  function EditarBoton({ item }: { item: Cancion }) {
    return (
      <span>
      <EditarCancionEntryFormUpdate arguments={item} onCancionUpdate={callData} 
/>
</span>
    );
  }





  return (

    <main className="w-full h-full flex flex-col box-border gap-s p-m">

      <ViewToolbar title="Lista de Canciones">
        <Group>
          <CancionEntryForm onCancionCreated={callData}/>
        </Group>
      </ViewToolbar>
      <HorizontalLayout theme="spacing">
        <Select items={itemSelect}
          value={criterio.value}
          onValueChanged={(evt) => (criterio.value = evt.detail.value)}
          placeholder="Selecione un cirterio">
        </Select>

        <TextField
          placeholder="Search"
          style={{ width: '50%' }}
          value={texto.value}
          onValueChanged={(evt) => (texto.value = evt.detail.value)}
        >
          <Icon slot="prefix" icon="vaadin:search" />
        </TextField>
        <Button onClick={search} theme="primary">
          Buscar
        </Button>
        <Button onClick={callData} theme="secondary">
          Refrescar
        </Button>


      </HorizontalLayout>
      <Grid items={items}>
        <GridColumn  renderer={indexIndex} header="Nro" />
        <GridSortColumn  path="nombre" header="Cancion" onDirectionChanged={(e) => order(e, "nombre")}/>
        <GridSortColumn path="genero" header="Genero" onDirectionChanged={(e) => order(e, 'genero')}  />
        <GridSortColumn path="album" header="Album"  onDirectionChanged={(e) => order(e, "album")} />
        <GridSortColumn path="tipo" header="Tipo Archivo"  onDirectionChanged={(e) => order(e, "tipo")}/>
        
        <GridSortColumn path="duracion" header="Duracion"  onDirectionChanged={(e) => order(e, "duracion")}/>
        <GridColumn path="url" header="Link"/> 
        
        <GridColumn header="Acciones" renderer={ EditarBoton}/>
    
      </Grid>
    </main>
  );
}