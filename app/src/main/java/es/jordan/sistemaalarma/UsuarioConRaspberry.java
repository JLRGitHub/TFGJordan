package es.jordan.sistemaalarma;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

import pojos.Raspberry;
import pojos.Usuario;
import pojos.Usuras;


/**
 * pantalla Usuario con raspberry.
 */
public class UsuarioConRaspberry extends AppCompatActivity implements Serializable {
    private ListView listView;
    private ImageView button, botonCan1;
    private Toolbar toolbar;
    private ListView lv;
    private TextView textazo, textazo2;
    private final String EXTRA_USUARIO = "";
    private String email = "";
    private String nombre = "";
    private int usuarioId;
    private TextView titulo1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_con_raspberry);
        final Usuario usuarioPasado = (Usuario) getIntent().getSerializableExtra(EXTRA_USUARIO);
        toolbar = findViewById(R.id.toolbi);
        toolbar.setTitle("Administrador - " + usuarioPasado.getNombre());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        xmlToJava();

        final ArrayList<ItemAlarma> itemsCompra = obtenerItems();

        final ItemAlarmaAdapter adapter = new ItemAlarmaAdapter(this, itemsCompra);
        lv.setClickable(true); //para poder pinchar en los elementos de la lista
        lv.setAdapter(adapter);


        final ItemAlarmaAdapter adaptador = new ItemAlarmaAdapter();
        //onclick de cada elemento de la lista
        lv.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            ItemAlarma itemSeleccionado = (ItemAlarma) adapter.getItem(position);
            nombre = itemSeleccionado.nombre;
            usuarioId = (int) itemSeleccionado.id;
            email = itemSeleccionado.tipo;
            textazo.setVisibility(View.GONE);
            lv.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            textazo2.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            botonCan1.setVisibility(View.VISIBLE);


            //lo que queremos hacer al clickar

        });


        this.listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        inicializarLista();

        /**
         * boton aceptar
         */
        button.setOnClickListener(v -> {

            int idEntero = usuarioId;

            SparseBooleanArray sp = listView.getCheckedItemPositions(); //booleano para saber los checkados
            eliminarUsurasSeleccionado(idEntero); //1ºeliminamos las raspberrys q tuviera asociadas antes
            // y le ponemos las nuevas

            //2º recorremos los checkbox y vemos cuales estan marcados
            for (int i = 0; i < sp.size(); i++) {
                if (sp.valueAt(i) == true) { //solo se recorren los seleccionados

                    Raspberry r1 = (Raspberry) listView.getItemAtPosition(i);
                    String s = ((CheckedTextView) listView.getChildAt(i)).getText().toString();

                    //3º en r1 tenemos la raspberry con r.get id obtenemos las rasp marcadas y llamamos
                    //al metodo de insercion
                    insertarRaspberryEnUsuario(idEntero, r1.getRaspberryId());

                }


            }

            Intent intent = new Intent(getApplicationContext(), MenuAdmin.class); //flechita que vuelve al
            final Usuario usuarioPasado1 = (Usuario) getIntent().getSerializableExtra(EXTRA_USUARIO);
            intent.putExtra(EXTRA_USUARIO, usuarioPasado1);
            startActivityForResult(intent, 0);


        });
        /**
         * Boton cancelar volvemos a la anterior vista
         */
        botonCan1.setOnClickListener(v -> {
//LO CONTRARIO a cuando clickamos al principio
            textazo.setVisibility(View.VISIBLE);
            lv.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            textazo2.setVisibility(View.GONE);
            botonCan1.setVisibility(View.GONE);

        });


    }

    /**
     * creación del menu del toolbar
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu2, menu);
        return true;
    }

    @Override
    /**
     * OnClick de los elementos del Toolbar
     */
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
                AlertDialog.Builder alert = new AlertDialog.Builder(UsuarioConRaspberry.this);
                alert.setTitle("Advertencia");
                alert.setCancelable(true);
                alert.setIcon(R.drawable.exit);

                alert.setMessage("¿Desea desconectarse?");

                alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                alert.setPositiveButton("Si", (dialog, which) -> {

                    Intent llamada = new Intent(UsuarioConRaspberry.this, MainActivity.class);
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
     * Inicializar lista.
     */
    public void inicializarLista() {
        ArrayList<Raspberry> listaRaspberrys = new ArrayList();
        listaRaspberrys = obtenerListaRaspberry();//aqui tenemos las listas

        ArrayAdapter<Raspberry> arrayAdapter
                = new ArrayAdapter<Raspberry>(this, android.R.layout.simple_list_item_multiple_choice, listaRaspberrys);
        this.listView.setAdapter(arrayAdapter);
    }

    /**
     * Xml to java.
     */
    public void xmlToJava() {

        listView = (ListView) findViewById(R.id.listView);
        button = (ImageView) findViewById(R.id.button);
        botonCan1 = (ImageView) findViewById(R.id.botonCan1);
        textazo = (TextView) findViewById(R.id.titulo11);
        textazo2 = (TextView) findViewById(R.id.titulo12);

        lv = findViewById(R.id.lvasociar);
    }

    /**
     * Obtener lista raspberry array list.
     *
     * @return el/la array list
     */
    public ArrayList<Raspberry> obtenerListaRaspberry() {
        ArrayList<Raspberry> listaRaspberrys = new ArrayList();
        try {
            String equipoServidor = "servidorjordan.ddns.net"; //para pruebas locales , la version definitiva podrá salir de la red local
            //String equipoServidor = "servidorwebjordan.ddns.net";
            int puertoServidor = 30510;
            Socket socketCliente = new Socket(equipoServidor, puertoServidor);

            ObjectInputStream listaRecibida = new ObjectInputStream(socketCliente.getInputStream());//me preparo para recibir
            listaRaspberrys = (ArrayList) listaRecibida.readObject(); //objeto leido y metido en usuario1 /lo recibod
            socketCliente.close();
            return listaRaspberrys;

        } catch (IOException ex) {
            System.out.println(ex.getMessage());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return listaRaspberrys;

    }


    /**
     * Insertar raspberry en usuario.
     *
     * @param idUsuario   el/la id usuario
     * @param idRaspberry el/la id raspberry
     */
    public void insertarRaspberryEnUsuario(int idUsuario, int idRaspberry) { //con el for recorreremos la lista de checkbox e iremos insertando
        try {
            String equipoServidor = "servidorjordan.ddns.net"; //para pruebas locales , la version definitiva podrá salir de la red local
           // String equipoServidor = "servidorwebjordan.ddns.net";
            int puertoServidor = 30800;
            Socket socketCliente = new Socket(equipoServidor, puertoServidor);
            Usuras u = new Usuras();
            Esclavo esclavo = new Esclavo();
            esclavo.setUsuarioId(idUsuario);
            esclavo.setRaspberryId(idRaspberry);

            ObjectOutputStream objetoEntregar = new ObjectOutputStream(socketCliente.getOutputStream());
            objetoEntregar.writeObject(esclavo);//mandamos el id ras e id usuario que queremos insertar en la bd


            socketCliente.close();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }


    /**
     * Eliminar usuras seleccionado.
     *
     * @param idUsuario el/la id usuario
     */
    public void eliminarUsurasSeleccionado(int idUsuario) {
        try {
            String equipoServidor = "servidorjordan.ddns.net"; //para pruebas locales , la version definitiva podrá salir de la red local
           // String equipoServidor = "servidorwebjordan.ddns.net";
            int puertoServidor = 30799;
            Socket socketCliente = new Socket(equipoServidor, puertoServidor);

            OutputStream socketSalida = socketCliente.getOutputStream();
            DataOutputStream escribir = new DataOutputStream(socketSalida);

            // Toast toast4 = Toast.makeText(getApplicationContext(),"el usuario es "+idUsuario, Toast.LENGTH_LONG);
            //  toast4.show();

            escribir.writeInt(idUsuario);


            socketCliente.close();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

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
//si quisieramos oslo mostrar usuarios quitaremos los dos else if
            if (usuario1.getRol().equalsIgnoreCase("ADMIN")) {
                int id = usuario1.getUsuarioId();
                String nombre = usuario1.getNombre().toString();
                String correo = usuario1.getEmail().toString();
                listaDelListView.add(new ItemAlarma(id, nombre, correo, "drawable/admin"));
            } else if (usuario1.getRol().equalsIgnoreCase("USUARIO")) {
                int id = usuario1.getUsuarioId();
                String nombre = usuario1.getNombre().toString();
                String correo = usuario1.getEmail().toString();
                listaDelListView.add(new ItemAlarma(id, nombre, correo, "drawable/usuario"));
            } else {
                int id = usuario1.getUsuarioId();
                String nombre = usuario1.getNombre().toString();
                String correo = usuario1.getEmail().toString();
                listaDelListView.add(new ItemAlarma(id, nombre, correo, "drawable/invitado"));
            }


        }


        return listaDelListView;
    }


}
