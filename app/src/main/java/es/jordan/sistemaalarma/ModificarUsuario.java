package es.jordan.sistemaalarma;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import pojos.Usuario;


/**
 * pantalla Modificar usuario.
 */
public class ModificarUsuario extends AppCompatActivity {
    private final String EXTRA_USUARIO = "";
    private String ultimoNombreSeleccionado = "";
    private String ultimoEmailSeleccionado = "";
    private EditText editNombre;
    private ListView lv;
    private ImageView botonModificar, botonCan;
    private EditText editEmail;
    private Toolbar toolbar;
    private Spinner spinner1;
    private TextView mensaje, mensaje2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_usuario);
        final Usuario usuarioPasado = (Usuario) getIntent().getSerializableExtra(EXTRA_USUARIO);
        xmlToJava();


        toolbar = findViewById(R.id.tool);
      //  toolbar.setTitle("Administrador - " + usuarioPasado.getNombre());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //iniciando spinner
        spinner1.setPrompt("Selecciona un rol");
        String[] opciones = {"Admin", "Usuario", "Invitado"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opciones);
        spinner1.setAdapter(adapter3);

        final ArrayList<ItemAlarma> itemsCompra = obtenerItems();

        final ItemAlarmaAdapter adapter = new ItemAlarmaAdapter(this, itemsCompra);
        lv.setClickable(true); //para poder pinchar en los elementos de la lista
        lv.setAdapter(adapter);

        final ItemAlarmaAdapter adaptador = new ItemAlarmaAdapter();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                editNombre.setVisibility(View.VISIBLE);
                editEmail.setVisibility(View.VISIBLE);
                spinner1.setVisibility(View.VISIBLE);
                lv.setVisibility(View.GONE);
                botonModificar.setVisibility(View.VISIBLE);
                botonCan.setVisibility(View.VISIBLE);
                mensaje.setVisibility(View.GONE);
                mensaje2.setVisibility(View.VISIBLE);

                ItemAlarma itemSeleccionado = (ItemAlarma) adapter.getItem(position);
                ultimoNombreSeleccionado = (itemSeleccionado.nombre); //metemos en variables globales el usuario elegido
                ultimoEmailSeleccionado = (itemSeleccionado.tipo); //el tipo es el email


                editNombre.setText(itemSeleccionado.nombre);
                editEmail.setText(itemSeleccionado.tipo); // corresponde al mail


                //lo que queremos hacer al clickar

            }
        });

        /**
         * onclick boton modificar
         */
        botonModificar.setOnClickListener(v -> {

            ArrayList<Usuario> listaUsuarios = new ArrayList();
            listaUsuarios = obtenerLista();

            //recorremos la lista hasta encontrar el usuario y luego lo mandamos al server para q lo modifique
            for (Usuario u : listaUsuarios) {
                if (u.getNombre().toString().equals(ultimoNombreSeleccionado) && u.getEmail().equals(ultimoEmailSeleccionado)) {
                    //Toast toast2 = Toast.makeText(getApplicationContext(),"He encontrado al usuario "+ultimoNombreSeleccionado, Toast.LENGTH_LONG);
                    //toast2.show();
                    Usuario usuarioAModificar = new Usuario(); //creamos un usuario poniendo lo que pone en las cajas
                    usuarioAModificar.setUsuarioId(u.getUsuarioId());
                    usuarioAModificar.setNombre(editNombre.getText().toString());
                    usuarioAModificar.setEmail(editEmail.getText().toString());
                    usuarioAModificar.setRol(spinner1.getSelectedItem().toString());

                    modificarUsuario(usuarioAModificar);  //mando el usuario a modificar al metodo que lo envia al server
                    break;


                }
            }


            Toast toast2 = Toast.makeText(getApplicationContext(), "Usuario modificado con éxito", Toast.LENGTH_LONG);
            toast2.show();
            Intent llamada = new Intent(ModificarUsuario.this, MenuAdmin.class);
            final Usuario usuarioPasado1 = (Usuario) getIntent().getSerializableExtra(EXTRA_USUARIO);
            llamada.putExtra(EXTRA_USUARIO, usuarioPasado1);
            startActivity(llamada);

        });
        /**
         * onclick vaciar texto cuando clicas edit nombre
         */
        editNombre.setOnClickListener(v -> editNombre.setText(""));
        /**
         * onclick vaciar texto
         */
        editEmail.setOnClickListener(v -> editEmail.setText(""));
        /**
         * volver atras al cancelar
         */
        botonCan.setOnClickListener(v -> {
//LO CONTRARIO a cuando clickamos al principio
            editNombre.setVisibility(View.GONE);
            editEmail.setVisibility(View.GONE);
            spinner1.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            botonModificar.setVisibility(View.GONE);
            botonCan.setVisibility(View.GONE);
            mensaje.setVisibility(View.VISIBLE);
            mensaje2.setVisibility(View.GONE);

        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //metodo que se encarga del toolbar
        //para que cada icono asignarle tareas diferentes
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(getApplicationContext(), MenuAdmin.class); //flechita que vuelve al
                final Usuario usuarioPasado = (Usuario) getIntent().getSerializableExtra(EXTRA_USUARIO);
                intent.putExtra(EXTRA_USUARIO, usuarioPasado);
                startActivityForResult(intent, 0);
                return true;

            case R.id.item2:
                Toast toast2 = Toast.makeText(getApplicationContext(), "pincha 2", Toast.LENGTH_LONG);
                toast2.show();
                return true;

            case R.id.item3:
                AlertDialog.Builder alert = new AlertDialog.Builder(ModificarUsuario.this);
                alert.setTitle("Advertencia");
                alert.setCancelable(true);
                alert.setIcon(R.drawable.exit);

                alert.setMessage("¿Desea desconectarse?");

                alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                alert.setPositiveButton("Si", (dialog, which) -> {

                    Intent llamada = new Intent(ModificarUsuario.this, MainActivity.class);
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
     * Xml to java.
     */
    public void xmlToJava() {

        editNombre = findViewById(R.id.nombre1);
        lv = findViewById(R.id.listView7);
        botonModificar = findViewById(R.id.botonModificar);
        botonCan = findViewById(R.id.botonCan);
        editEmail = findViewById(R.id.direccion1);
        spinner1 = findViewById(R.id.spinnerrol);
        mensaje = findViewById(R.id.mensaje);
        mensaje2 = findViewById(R.id.mensaje2);
    }

    /**
     * Obtener lista array list.
     *
     * @return el/la array list
     */
    public ArrayList<Usuario> obtenerLista() {
        ArrayList<Usuario> listaUsuarios = new ArrayList();
        try {
            String equipoServidor = "servidorjordan.ddns.net"; //para pruebas locales , la version definitiva podrá salir de la red local
            //String equipoServidor = "servidorwebjordan.ddns.net";
            int puertoServidor = 30504;
            Socket socketCliente = new Socket(equipoServidor, puertoServidor);

            ObjectInputStream listaRecibida = new ObjectInputStream(socketCliente.getInputStream());//me preparo para recibir
            listaUsuarios = (ArrayList) listaRecibida.readObject(); //objeto leido y metido en usuario1 /lo recibod
            socketCliente.close();
            return listaUsuarios;

        } catch (IOException ex) {
            System.out.println(ex.getMessage());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return listaUsuarios;

    }

    /**
     * Obtener items array list.
     *
     * @return el/la array list
     */
    public ArrayList<ItemAlarma> obtenerItems() {
        ArrayList<ItemAlarma> listaDelListView = new ArrayList<ItemAlarma>();//lista con los atributos del litview
        ArrayList<Usuario> listaUsuarios = new ArrayList();
        listaUsuarios = obtenerLista(); //recorremos la lista de usuarios y metemos la informacion que queremos
        for (Usuario usuario1 : listaUsuarios) {

            if (usuario1.getRol().equalsIgnoreCase("ADMIN")) {
                int id = usuario1.getUsuarioId();
                String nombre = usuario1.getNombre().toString();
                String correo = usuario1.getEmail().toString();
                listaDelListView.add(new ItemAlarma(id, nombre, correo, "drawable/adming3"));
            } else if (usuario1.getRol().equalsIgnoreCase("USUARIO")) {
                int id = usuario1.getUsuarioId();
                String nombre = usuario1.getNombre().toString();
                String correo = usuario1.getEmail().toString();
                listaDelListView.add(new ItemAlarma(id, nombre, correo, "drawable/usuariog3"));
            } else {
                int id = usuario1.getUsuarioId();
                String nombre = usuario1.getNombre().toString();
                String correo = usuario1.getEmail().toString();
                listaDelListView.add(new ItemAlarma(id, nombre, correo, "drawable/invitadog3"));
            }


        }


        return listaDelListView;
    }

    /**
     * Modificar usuario.
     *
     * @param usuario1 el/la usuario 1
     */
    public void modificarUsuario(Usuario usuario1) {
        try {
            String equipoServidor = "servidorjordan.ddns.net"; //para pruebas locales , la version definitiva podrá salir de la red local
            //String equipoServidor = "servidorwebjordan.ddns.net";
            int puertoServidor = 30582;
            Socket socketCliente = new Socket(equipoServidor, puertoServidor);
            gestionarComunicacion(socketCliente, usuario1);

            socketCliente.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Gestionar comunicacion.
     *
     * @param socketCliente el/la socket cliente
     * @param usuario1      el/la usuario 1
     */
    public void gestionarComunicacion(Socket socketCliente, Usuario usuario1) {

        try {

            ObjectOutputStream objetoEntregar = new ObjectOutputStream(socketCliente.getOutputStream());
            objetoEntregar.writeObject(usuario1);//el cliente manda el objeto al server para que lo modifique

            objetoEntregar.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());

        }


    }


}