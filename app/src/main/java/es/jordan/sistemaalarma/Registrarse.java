package es.jordan.sistemaalarma;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import pojos.Usuario;
import seguridad.Hashear;
import voz.TTSManager;


/**
 * pantalla Registrarse.
 */
public class Registrarse extends AppCompatActivity {
    //private final String EXTRA_USUARIO = "";
    private ImageView botonCancelar;
    private ImageView botonRegistrarse;
    private EditText editNombre, editContraseña, editEmail, respuesta;
    private TTSManager ttsManager = null;
    private Toolbar toolbar;
    private String secretKey = "enigma";
    private ImageView captchaImagen;
    private String random = "x";
    private Boolean detectorCaptcha = false; //lo pondremos a true cuando lo pase con exito
    //private GoogleApiClient googleApiClient;//necesitamos entrar en recaptcha de google y configurar


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        //lineas necesarias para permitir los sockets
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        xmlToJava();
        textoToVoz();

        toolbar = findViewById(R.id.toolbarRegistrarse);
        toolbar.setTitle("Registrar nuevo usuario");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);//quitamos el titulo del toolbar

        /**
         * onclick cancelar
         */
        //botonCancelar.setOnClickListener(new View.OnClickListener() esta es la otra forma
        botonCancelar.setOnClickListener(v -> {


            Intent intent = new Intent(v.getContext(), MainActivity.class);
            startActivityForResult(intent, 0);
        });
        /**
         * onclick sonido al darle al capcha foto
         */
        captchaImagen.setOnClickListener(v -> reproducirSonidoRandom());

        /**
         * onclick boton al registarse
         */
        botonRegistrarse.setOnClickListener(v -> {
            comprobacionCaptcha();
            if (detectorCaptcha == false) {
                Toast.makeText(getApplicationContext(), "Debes completar el formulario con éxito", Toast.LENGTH_LONG).show();

            } else {
                Hashear e = new Hashear();
                Usuario usuario1 = new Usuario();
                usuario1.setNombre(editNombre.getText().toString());
                String contraseñaCodificada = e.encode(secretKey, editContraseña.getText().toString());
                usuario1.setContraseña(contraseñaCodificada);
                usuario1.setEmail(editEmail.getText().toString());
                usuario1.setRol("invitado");
                //usuario1.setRol("invitado");
                if (usuario1.getNombre().trim().length() == 0 || usuario1.getContraseña().trim().length() == 0 || usuario1.getEmail().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();

                } else {
                    boolean detector = false;
                    ArrayList<Usuario> listaUsuarios = new ArrayList();
                    listaUsuarios = obtenerLista();


                    for (Usuario aux : listaUsuarios) {

                        if (aux.getNombre().equals(usuario1.getNombre()) || aux.getEmail().equals(usuario1.getEmail())) {
                            detector = true;//hemos detectado un usuario con esos datos
                            break;

                        }

                    }

                    if (detector) { //es decir si es true
                        Toast.makeText(getApplicationContext(), "Ya existe un usuario con ese nombre o email", Toast.LENGTH_SHORT).show();
                        limpiarCajas();
                    } else {
                        insertarUsuario(usuario1);
                        limpiarCajas();
                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                        startActivityForResult(intent, 0);
                    }
                }

            }
        });


    }

    /**
     * onclick menu de toolbar
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenusolosalida, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //metodo que se encarga del toolbar
        //para que cada icono asignarle tareas diferentes
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(getApplicationContext(), MenuAdmin.class); //flechita que vuelve al
                startActivityForResult(intent, 0);
                return true;

            case R.id.item2:
                //  Toast toast2 = Toast.makeText(getApplicationContext(),"pincha 2", Toast.LENGTH_LONG);
                //  toast2.show();
                return true;

            case R.id.item3:
                AlertDialog.Builder alert = new AlertDialog.Builder(Registrarse.this);
                alert.setTitle("Advertencia");
                alert.setCancelable(true);
                alert.setIcon(R.drawable.exit);

                alert.setMessage("¿Desea desconectarse?");

                alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                alert.setPositiveButton("Si", (dialog, which) -> {

                    Intent llamada = new Intent(Registrarse.this, MainActivity.class);
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
        botonRegistrarse = findViewById(R.id.botonAceptarXML);
        botonCancelar = findViewById(R.id.botonCancelarXML);
        editNombre = findViewById(R.id.nombre1);
        editContraseña = findViewById(R.id.contraseña1);
        editEmail = findViewById(R.id.direccion1);
        respuesta = findViewById(R.id.respuesta);
        captchaImagen = findViewById(R.id.captchaImagen);
    }


    /**
     * Texto to voz.
     */
    public void textoToVoz() {
        ttsManager = new TTSManager();
        ttsManager.init(this);
    }

    /**
     * Insertar usuario.
     *
     * @param usuario1 el/la usuario 1
     */
    public void insertarUsuario(Usuario usuario1) {
        try {
            String equipoServidor = "13.38.119.204"; //para pruebas locales , la version definitiva podrá salir de la red local
            //String equipoServidor = "servidorwebjordan.ddns.net";
            int puertoServidor = 30500;
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
            System.out.println("El objeto que mandara el cliente al servidor es: " + usuario1);
            objetoEntregar.writeObject(usuario1);//el cliente manda el objeto al server


            objetoEntregar.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());

        }


    }


    /**
     * Obtener lista array list.
     *
     * @return el array list
     */
    public ArrayList<Usuario> obtenerLista() {
        ArrayList<Usuario> listaUsuarios = new ArrayList();
        try {
            String equipoServidor = "13.38.119.204"; //para pruebas locales , la version definitiva podrá salir de la red local
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
     * textos vacios
     */
    public void limpiarCajas() {
        editNombre.setText("");
        editContraseña.setText("");
        editEmail.setText("");


    }

    /**
     * Reproducir sonido random de esta lista de palabras para el captcha
     */
    public void reproducirSonidoRandom() {
        String[] palabrasRandom = {"paludo", "palabra", "adivina", "pera",
                "rojo", "sobresaliente", "oculto", "cabra", "tigre", "barco", "rey", "reina",
                "llavero", "improvisar", "juego", "coche", "alucinante", "alumno", "profesor",
                "pescar", "anciano", "libro", "cuaderno", "remo", "internet", "trompeta", "elefante", "flauta", "manzana", "fresa", "verde"};
        int numeroGenerado = new Random().nextInt(palabrasRandom.length); //genero numero aleatorio con maximo la longitud del array
        //cada numero corresponde con 1 posicion del array y esa palabra le asocio ese id del array
        random = (palabrasRandom[numeroGenerado]);
        ttsManager.initQueue(random); //reproduzco el sonido
    }


    /**
     * Comprobacion captcha. si coincide o no
     */
    public void comprobacionCaptcha() {

        if (respuesta.getText().toString().equalsIgnoreCase(random)) //miramos si la palabra escrita por teclado coincide con la escuchada
        {
            detectorCaptcha = true;
            Toast.makeText(getApplicationContext(), "Datos correctos", Toast.LENGTH_SHORT).show();

        } else {
            detectorCaptcha = false;
            Toast.makeText(getApplicationContext(), "Captcha incorrecto o incompleto", Toast.LENGTH_SHORT).show();

        }


    }


}