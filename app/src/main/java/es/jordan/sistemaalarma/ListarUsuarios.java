package es.jordan.sistemaalarma;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import adaptadores.AdaptadorDatos;
import adaptadores.DatosRecicler;
import pojos.Usuario;


/**
 * pantalla Listar usuarios.
 */
public class ListarUsuarios extends AppCompatActivity {


    private ArrayList<DatosRecicler> miLista;
    private RecyclerView miRecycler;
    private RadioButton b1, b2, b3, b4;
    private TextView t1;
    private Toolbar toolbar;
    private final String EXTRA_USUARIO = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_usuarios);
        final Usuario usuarioPasado = (Usuario) getIntent().getSerializableExtra(EXTRA_USUARIO);
        xmlToJava();
        toolbar = findViewById(R.id.toolbarListarUsuarios);
        toolbar.setTitle("Administrador - " + usuarioPasado.getNombre());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        miRecycler.setLayoutManager(new LinearLayoutManager(this));

        AdaptadorDatos elAdaptador = new AdaptadorDatos(miLista);
        elAdaptador.setOnClickListener(v -> {
            CharSequence texto = "Pulsado" + miLista.get(miRecycler.getChildAdapterPosition(v)).getNombre();
            Toast toast = Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_LONG);
            toast.show();
        });
        miRecycler.setAdapter(elAdaptador);
        listarTodos();//metodo que recorre toda la lista y muestra a todos con iconos diferentes por rol


    }

    /**
     * Listar todos.metodo para listar todos, si es usuario imagen de usuario. Si es admin, admin de imagen. Si no, invitado
     */

    public void listarTodos() {
        miRecycler.setLayoutManager(new LinearLayoutManager(this));

        AdaptadorDatos elAdaptador = new AdaptadorDatos(miLista);
        ArrayList<Usuario> listaUsuarios = new ArrayList();
        listaUsuarios = obtenerLista();
        miLista = new ArrayList<DatosRecicler>();
        System.out.println(listaUsuarios);
        for (Usuario aux : listaUsuarios) {

            if (aux.getRol().equalsIgnoreCase("ADMIN")) { //comprobamos contra la bd que rol tiene y en funcion de eso le ponemos una foto
                //u otra en el recycler.
                miLista.add(new DatosRecicler(aux.getNombre(), aux.getEmail(), R.drawable.adming3));


            } else if (aux.getRol().equalsIgnoreCase("USUARIO")) { //comprobamos contra la bd que rol tiene y en funcion de eso le ponemos una foto
                //u otra en el recycler.
                miLista.add(new DatosRecicler(aux.getNombre(), aux.getEmail(), R.drawable.usuariog3));

            } else {
                miLista.add(new DatosRecicler(aux.getNombre(), aux.getEmail(), R.drawable.invitadog3));

            }
            elAdaptador = new AdaptadorDatos(miLista);
            elAdaptador.notifyDataSetChanged();
            miRecycler.setAdapter(elAdaptador);

        }

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
                AlertDialog.Builder alert = new AlertDialog.Builder(ListarUsuarios.this);
                alert.setTitle("Advertencia");
                alert.setCancelable(true);
                alert.setIcon(R.drawable.exit);

                alert.setMessage("¿Desea desconectarse?");

                alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                alert.setPositiveButton("Si", (dialog, which) -> {

                    Intent llamada = new Intent(ListarUsuarios.this, MainActivity.class);
                    startActivity(llamada);
                });
                alert.create().show();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Xml to java.
     */
    public void xmlToJava() {

        miLista = new ArrayList<DatosRecicler>();
        miRecycler = findViewById(R.id.recicler);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        t1 = findViewById(R.id.titulo1);

    }


    /**
     * Obtener lista array list.
     *
     * @return the array list
     */
    public ArrayList<Usuario> obtenerLista() {
        ArrayList<Usuario> listaUsuarios = new ArrayList();
        try {
            String equipoServidor = "13.37.217.86";
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
     * On radio button clicked.
     *
     * @param view the view
     */
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {

            case R.id.b1: //si clickan admin
                if (checked)
                    mostrarSoloAdmin();
                break;
            case R.id.b2: //si clickan usuario
                if (checked)
                    mostrarSoloUsuario();
                break;
            case R.id.b3: //si clickan invitado
                if (checked)
                    mostrarSoloInvitado();
                break;
            case R.id.b4: //si clickan invitado
                if (checked)
                    listarTodos();
                break;
            default:
                listarTodos();
                break;
        }
    }

    /**
     * Mostrar solo admin.
     */
    public void mostrarSoloAdmin() {
        ArrayList<Usuario> listaUsuarios = new ArrayList();


        listaUsuarios = obtenerLista();
        miRecycler.setLayoutManager(new LinearLayoutManager(this));

        AdaptadorDatos elAdaptador = new AdaptadorDatos(miLista);
        miLista = new ArrayList<DatosRecicler>();
        System.out.println(listaUsuarios);
        for (Usuario aux : listaUsuarios) {

            if (aux.getRol().equalsIgnoreCase("ADMIN")) { //comprobamos contra la bd que rol tiene y en funcion de eso le ponemos una foto
                //u otra en el recycler.
                miLista.add(new DatosRecicler(aux.getNombre(), aux.getEmail(), R.drawable.adming3));
                elAdaptador = new AdaptadorDatos(miLista);
                elAdaptador.notifyDataSetChanged();
                miRecycler.setAdapter(elAdaptador);

            }

        }


    }

    /**
     * Mostrar solo usuario.
     */
    public void mostrarSoloUsuario() {
        ArrayList<Usuario> listaUsuarios = new ArrayList();
        listaUsuarios = obtenerLista();
        miRecycler.setLayoutManager(new LinearLayoutManager(this));

        AdaptadorDatos elAdaptador = new AdaptadorDatos(miLista);
        miLista = new ArrayList<DatosRecicler>();

        for (Usuario aux : listaUsuarios) {

            if (aux.getRol().equalsIgnoreCase("USUARIO")) { //comprobamos contra la bd que rol tiene y en funcion de eso le ponemos una foto
                //u otra en el recycler.

                miLista.add(new DatosRecicler(aux.getNombre(), aux.getEmail(), R.drawable.usuariog3));
                elAdaptador = new AdaptadorDatos(miLista);
                elAdaptador.notifyDataSetChanged();
                miRecycler.setAdapter(elAdaptador);

            }

        }


    }

    /**
     * Mostrar solo invitado.
     */
    public void mostrarSoloInvitado() {
        ArrayList<Usuario> listaUsuarios = new ArrayList();
        listaUsuarios = obtenerLista();
        miRecycler.setLayoutManager(new LinearLayoutManager(this));

        AdaptadorDatos elAdaptador = new AdaptadorDatos(miLista);
        miLista = new ArrayList<DatosRecicler>();
        System.out.println(listaUsuarios);
        for (Usuario aux : listaUsuarios) {

            if (aux.getRol().equalsIgnoreCase("INVITADO")) { //comprobamos contra la bd que rol tiene y en funcion de eso le ponemos una foto
                //u otra en el recycler.
                miLista.add(new DatosRecicler(aux.getNombre(), aux.getEmail(), R.drawable.invitadog3));
                elAdaptador = new AdaptadorDatos(miLista);
                elAdaptador.notifyDataSetChanged();
                miRecycler.setAdapter(elAdaptador);

            }

        }


    }


}
