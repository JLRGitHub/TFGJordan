package es.jordan.sistemaalarma;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import adaptadores.AdaptadorDatos;
import adaptadores.DatosRecicler;
import pojos.Incidencia;
import pojos.Raspberry;
import pojos.Usuario;

/**
 * el/la type Listar incidencias.
 */
public class ListarIncidencias extends AppCompatActivity {
    private final String EXTRA_USUARIO = "";
    private Toolbar toolbar;
    private ImageView ver;
    private ArrayList<DatosRecicler> miLista;
    private RecyclerView miRecycler;
    private TextView titulillo;
    private Spinner spinner1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_incidencias);
        final Usuario usuarioPasado = (Usuario) getIntent().getSerializableExtra(EXTRA_USUARIO);
        xmlToJava();
        toolbar = findViewById(R.id.toolbarListarIncidencias);
        toolbar.setTitle("Usuario VIP - " + usuarioPasado.getNombre());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        ArrayList<Raspberry> listaRaspberrysPropias = new ArrayList();
        listaRaspberrysPropias = mandarUsuarioYrecibirListaDeSusRaspberrys(usuarioPasado); //mando el usuario del
        //que quiero recibir raspberrys al servidor y este me contesta devolviendome la lista
        ArrayList<String> opciones4 = new ArrayList(); //en un array de string meto el modelo y la direccion
        //de cada raspB que he recibido

        {
            for (Raspberry r : listaRaspberrysPropias) {
                opciones4.add(r.getRaspberryId() + ":" + r.getModelo() + ":" + r.getDireccion());
            }

            spinner1 = findViewById(R.id.spinnerlistarraspberrys);
            spinner1.setPrompt("¿De cual de tus raspb quieres ver incidencias?");
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opciones4);
            spinner1.setAdapter(adapter2);


        }
        /**
         *on click en la imagen ver incidencias
         */
        ver.setOnClickListener(v -> {


            if (spinner1.getCount() == 0) {
                // Toast.makeText(getApplicationContext(), eleccion, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Sin raspberrys asignadas", Toast.LENGTH_SHORT).show();

            } else {
                String eleccion = spinner1.getSelectedItem().toString();
                obtenerIncidenciasDeLaRaspberryElegida(eleccion);

            }


        });


        miRecycler.setLayoutManager(new LinearLayoutManager(this));

        AdaptadorDatos elAdaptador = new AdaptadorDatos(miLista);
        elAdaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence texto = "Pulsado" + miLista.get(miRecycler.getChildAdapterPosition(v)).getNombre();
                Toast toast = Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_LONG);
                toast.show();
            }
        });
        miRecycler.setAdapter(elAdaptador);


    }

    /**
     * Obtener incidencias de la raspberry elegida.
     *
     * @param eleccion el/la eleccion
     */
    public void obtenerIncidenciasDeLaRaspberryElegida(String eleccion) {

        ArrayList<Incidencia> listaIncidencias = new ArrayList();
        String[] datos = eleccion.split(":");

        String id = "";
        for (String item : datos) //solo recorremos el for una vez porque tan solo queremos hasta los dos primeros :
        {
            id = item;
            break;
        }
        //  Toast toast2 = Toast.makeText(getApplicationContext(),id, Toast.LENGTH_LONG);
        // toast2.show();
        //1º paso una vez que tenemos el id de la raspberry que ya habiamos sacado de la base de datos
        // se lo mandamos al server para que me diga que incidencias tiene
        try {
            String equipoServidor = "servidorwebjordan.ddns.net"; //para pruebas locales , la version definitiva podrá salir de la red local
            //String equipoServidor = "servidorwebjordan.ddns.net";
            int puertoServidor = 30570;
            Socket socketCliente = new Socket(equipoServidor, puertoServidor);
            //2º paso mandar el usuario que esta conectado como objeto
            OutputStream socketSalida = socketCliente.getOutputStream();
            DataOutputStream escribir = new DataOutputStream(socketSalida);
            escribir.writeUTF(id);
            //3º paso recibir la lista de las incidencias de ese id de raspberry
            ObjectInputStream listaRecibida = new ObjectInputStream(socketCliente.getInputStream());//me preparo para recibir
            listaIncidencias = (ArrayList) listaRecibida.readObject(); //objeto leido y metido en usuario1 /lo recibo
            //4º paso una vez tengo la lista solo seleccionare unos datos para mostrar en el recycler asi que la
            //recorro voy poniendo los datos en la lista del recycler y luego lo vuelvo visible
            miLista = new ArrayList<DatosRecicler>();
            if (listaIncidencias.toString().equals(null) || listaIncidencias.toString() == null || listaIncidencias.toString().equals("[]")) {
                miRecycler.setVisibility(View.INVISIBLE);
                Toast toast2 = Toast.makeText(getApplicationContext(), "Sin incidencias en la raspberry seleccionada", Toast.LENGTH_LONG);
                toast2.show();
            } else {
                miRecycler.setVisibility(View.VISIBLE);
                AdaptadorDatos elAdaptador = new AdaptadorDatos(miLista);
                for (Incidencia aux : listaIncidencias) {


                    //aqui sacamos datos de 2 tablas, el modelo y la hora(raspberryId) es un objeto de Raspberry
                    miLista.add(new DatosRecicler(aux.getRaspberryId().getModelo(), aux.getHora(), R.drawable.alerta2));
                    elAdaptador = new AdaptadorDatos(miLista);
                    elAdaptador.notifyDataSetChanged();
                    miRecycler.setAdapter(elAdaptador);


                }

                miRecycler.setVisibility(View.VISIBLE);
                elAdaptador.notifyDataSetChanged();


            }

            listaRecibida.close();


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu2, menu);
        return true;
    }

    /**
     * on click del menu toolbar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //metodo que se encarga del toolbar
        //para que cada icono asignarle tareas diferentes
        final Usuario usuarioPasado = (Usuario) getIntent().getSerializableExtra(EXTRA_USUARIO);
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(getApplicationContext(), MenuVip.class); //flechita que vuelve al
                intent.putExtra(EXTRA_USUARIO, usuarioPasado);
                startActivityForResult(intent, 0);
                return true;

            case R.id.item2:
                // Toast toast2 = Toast.makeText(getApplicationContext(),"pincha 2", Toast.LENGTH_LONG);
                //    toast2.show();
                return true;

            case R.id.item3:
                AlertDialog.Builder alert = new AlertDialog.Builder(ListarIncidencias.this);
                alert.setTitle("Advertencia");
                alert.setCancelable(true);
                alert.setIcon(R.drawable.exit);

                alert.setMessage("¿Desea desconectarse?");

                alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                alert.setPositiveButton("Si", (dialog, which) -> {

                    Intent llamada = new Intent(ListarIncidencias.this, MainActivity.class);
                    startActivity(llamada);
                });
                alert.create().show();

                return true;

            default:
                // If we got here, el/la user's action was not recognized.
                // Invoke el/la superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * xml a java conversión
     */
    private void xmlToJava() {

        miLista = new ArrayList<DatosRecicler>();
        miRecycler = findViewById(R.id.recicler2);
        titulillo = findViewById(R.id.titulistar2);
        ver = findViewById(R.id.botonVer);
        spinner1 = findViewById(R.id.spinnerlistarraspberrys);
    }

    /**
     * Mandar usuario yrecibir lista de sus raspberrys array list.
     *
     * @param usuarioPasado el/la usuario pasado
     * @return el /la array list
     */
    public ArrayList<Raspberry> mandarUsuarioYrecibirListaDeSusRaspberrys(Usuario usuarioPasado) {
        ArrayList<Raspberry> listaRaspberrys = new ArrayList();
        try {

            //1º paso conectarse al servidor
            String equipoServidor = "servidorwebjordan.ddns.net"; //para pruebas locales , la version definitiva podrá salir de la red local
            //String equipoServidor = "servidorwebjordan.ddns.net";
            int puertoServidor = 30560;
            Socket socketCliente = new Socket(equipoServidor, puertoServidor);
            //2º paso mandar el usuario que esta conectado como objeto
            ObjectOutputStream objetoEntregar = new ObjectOutputStream(socketCliente.getOutputStream());
            System.out.println("El objeto que mandara el cliente al servidor es: " + usuarioPasado);
            objetoEntregar.writeObject(usuarioPasado);//el cliente manda el objeto al server
            objetoEntregar.flush();
            //3º paso recibir la lista de raspberrys que tiene ese usuario
            ObjectInputStream listaRecibida = new ObjectInputStream(socketCliente.getInputStream());//me preparo para recibir
            listaRaspberrys = (ArrayList) listaRecibida.readObject(); //objeto leido y metido en usuario1 /lo recibod


            listaRecibida.close();
            objetoEntregar.close();
            return listaRaspberrys;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return listaRaspberrys;
    }


}
