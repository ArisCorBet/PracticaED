import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Button, ComboBox, DatePicker, Dialog, Grid, GridColumn, GridItemModel, GridSortColumn, HorizontalLayout, Icon, Select, TextField, VerticalLayout } from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import { TaskService } from 'Frontend/generated/endpoints';
import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';

import { useDataProvider } from '@vaadin/hilla-react-crud';
import Genero from 'Frontend/generated/com/unl/music/base/models/Genero';
import { useCallback, useEffect, useState } from 'react';
import { GeneroServices } from 'Frontend/generated/endpoints';

export const config: ViewConfig = {
  title: 'Generos',
  menu: {
    icon: 'vaadin:clipboard-check',
    order: 1,
    title: 'Generos',
  },
};


type GeneroEntryFormProps = {
  onGeneroCreated?: () => void;
};

type GeneroEntryFormPropsUpdate = ()=> {
  onGeneroUpdated?: () => void;
};
//GUARDAR GENERO
function GeneroEntryForm(props: GeneroEntryFormProps) {
  const nombre = useSignal('');
  const createGenero = async () => {
    try {
      if (nombre.value.trim().length > 0) {
        await GeneroServices.createGenero(nombre.value);
        if (props.onGeneroCreated) {
          props.onGeneroCreated();
        }
        nombre.value = '';
        
        dialogOpened.value = false;
        Notification.show('Genero creado', { duration: 5000, position: 'bottom-end', theme: 'success' });
      } else {
        Notification.show('No se pudo crear, faltan datos', { duration: 5000, position: 'top-center', theme: 'error' });
      }

    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };
  
  
  const dialogOpened = useSignal(false);
  return (
    <>
      <Dialog
        modeless
        headerTitle="Nuevo genero"
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
              Candelar
            </Button>
            <Button onClick={createGenero} theme="primary">
              Registrar
            </Button>
            
          </>
        }
      >
        <VerticalLayout style={{ alignItems: 'stretch', width: '18rem', maxWidth: '100%' }}>
          <TextField label="Nombre del genero" 
            placeholder="Ingrese el nombre del genero"
            aria-label="Nombre del genero"
            value={nombre.value}
            onValueChanged={(evt) => (nombre.value = evt.detail.value)}
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




//LISTA DE CANCIONES
export default function GeneroView() {
  
  const [items, setItems] = useState([]);
  useEffect(() => {
    GeneroServices.listAll().then(function (data) {
      //items.values = data;
      setItems(data);
    });
  }, []);

  
 const order = (event, columnId) => {
    console.log(event);
    const direction = event.detail.value;
    console.log(`Sort direction changed for column ${columnId} to ${direction}`);
    var dir = (direction == 'asc') ? 1 : 2;
    GeneroServices.order(columnId, dir).then(function(data){
      setItems(data);
    });
  }


  const criterio = useSignal('');
    const texto = useSignal('');
    const itemSelect = [
      {
        label: 'Genero',
        value: 'nombre',
      },
      
    ];
    const search = async () => {
      try {
        console.log(criterio.value+" "+texto.value);
        GeneroServices.search(criterio.value, texto.value, 0).then(function (data) {
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


  function indexIndex({model}:{model:GridItemModel<Genero>}) {
    return (
      <span>
        {model.index + 1} 
      </span>
    );
  }

   return (
  
      <main className="w-full h-full flex flex-col box-border gap-s p-m">
  
        <ViewToolbar title="Lista de Generos">
          <Group>
            <GeneroEntryForm />
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
        </HorizontalLayout>
        <Grid items={items}>
          <GridColumn  renderer={indexIndex} header="Nro" />
          <GridSortColumn  path="nombre" header="Album" onDirectionChanged={(e) => order(e, "nombre")}/>
        </Grid>
      </main>
    );
  }