package es.jordan.sistemaalarma;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pojos.Usuario;
import seguridad.Hashear;
import voz.TTSManager;

/**
 * * @author Jordan López Rementería
 * * @version 1.7
 */
public class MainActivity extends AppCompatActivity {
    private final String EXTRA_USUARIO = "";

    private ImageView botonIniciar,
            botonRegistrar;
    private EditText email1,
            contraseña1;
    private TTSManager ttsManager = null;
    private String secretKey = "enigma";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //a lo largo del proyecto siempre que necesitemos usar sockets deberemos escribir estas 2 lineas
        //para permitir la conexión
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        xmlToJava(); //metodo que enchufa el xml a java
        textoToVoz(); //metodo que convierte texto en voz
        activarAnimacion(); //metodo que activa una animacion
        //Hashear e = new Hashear();


        /**
         * On click del boton registrar
         */
        botonRegistrar.setOnClickListener(v -> { //on click registrar
            Intent intent = new Intent(v.getContext(), Registrarse.class);
            startActivityForResult(intent, 0);


        });

        /**
         * On click del boton iniciar
         */
        botonIniciar.setOnClickListener(v -> { //on click iniciar


            if (email1.getText().toString().trim().length() == 0 || contraseña1.getText().toString().trim().length() == 0) {
                Toast.makeText(getApplicationContext(), "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();

            }
            Hashear seguridad = new Hashear();
            //creamos un usuario con los datos introducidos en el login y los comprobamos contra la bd
            Usuario usuario1 = new Usuario();

            //1º paso mandar el email y la password hasheada al server para saber si hay algun usuario con estos datos
            //2º el server recibe el objeto y manda al componente de acceso a datos realizar la consulta
            //3º si existe el componente devolver el objeto con todos los datos y si no existe devolvera un null
            //4º el server manda ese objeto por sockets al cliente y segun el rol que tenga manda a una pantalla u otra

            //aqui meteremos todos los datos del usuario confirmado

            usuario1.setEmail(email1.getText().toString());
            usuario1.setContraseña(seguridad.encode(secretKey, contraseña1.getText().toString()));


            Usuario usuarioCompleto = new Usuario();
            usuarioCompleto = leerUsuario(usuario1);
            //ahora tenemos que mandar este usuario por sockets al servidor para que lo compruebe el con el CAD
            //y volcara sobre completo todos sus datos

            if ( usuarioCompleto.getNombre()==null)
            {
                Toast.makeText(getApplicationContext(), "Usuario no existe", Toast.LENGTH_LONG).show();
                //si llegamos aquí es porque no esta en la base de datos o no esta activa que es diferente a que no este el usuario
            }
            else
            {
            if ( usuarioCompleto.getNombre()!=null && usuarioCompleto.getNombre().equals("*")) //el * es lo que le ha mandado el server
            //cuando los credenciales contrastados cn la bd no coinciden
            {
                Toast.makeText(getApplicationContext(), "Credenciales no válidos", Toast.LENGTH_LONG).show();

            } else {
                if (usuarioCompleto.getNombre() != null && usuarioCompleto.getRol().equalsIgnoreCase("ADMIN")) {


                    Toast.makeText(getApplicationContext(), "Credenciales validos, eres administrador", Toast.LENGTH_LONG).show();
                    pantallaAdmin(usuarioCompleto);
                    finish(); // es importante matar el main o de lo contrario el usuario podria volver atras con problemas de seguridad
                    limpiar(); //limpiamos las cajas


                } else if (usuarioCompleto.getNombre() != null && usuarioCompleto.getRol().equalsIgnoreCase("USUARIO")) {


                    Toast.makeText(getApplicationContext(), "Credenciales validos, eres usuario", Toast.LENGTH_LONG).show();
                    pantallaUsuario(usuarioCompleto); //vamos a la pantalla usuario pasandole ese usu
                    finish(); // es importante matar el main o de lo contrario el usuario podria volver atras
                    limpiar(); //limpiamos datos del login


                } else if (usuarioCompleto.getNombre() != null && usuarioCompleto.getRol().equalsIgnoreCase("INVITADO")) {

                    Toast.makeText(getApplicationContext(), "Credenciales validos, eres invitado", Toast.LENGTH_LONG).show();
                    pantallaInvitado(usuarioCompleto);
                    finish(); // es importante matar el main o de lo contrario el usuario podria volver atras
                    limpiar();


                }


            }
            }


        });


    }

    /**
     * Limpiar cajas de texto
     */
    public void limpiar() { //settea las cajas de texto vacias
        // nombre1.setText("");
        email1.setText("");
        contraseña1.setText("");
    }


    /**
     * Pantalla admin.
     *
     * @param usuario1 un usuario
     */
    public void pantallaAdmin(Usuario usuario1) {
//        usuario1.setNombre(nombre1.getText().toString());
        Intent intent2 = new Intent(MainActivity.this, MenuAdmin.class); //ponemos en la actividad el objeto usuario
        intent2.putExtra(EXTRA_USUARIO, usuario1); //mandamos el usuario actual a la pantalla siguiente para no perder los datos
        ttsManager.initQueue("Bienvenido " + usuario1.getNombre()); //saludo personalizado con el nombre del usuario
        finish();
        startActivity(intent2);
    }


    /**
     * Pantalla usuario.
     *
     * @param usuario1 un usuario
     */
    public void pantallaUsuario(Usuario usuario1) {
        Intent intent = new Intent(MainActivity.this, MenuVip.class); //ponemos en la actividad el objeto usuario
        intent.putExtra(EXTRA_USUARIO, usuario1); //mandamos el usuario actual a la pantalla siguiente para no perder los datos
        ttsManager.initQueue("Bienvenido " + usuario1.getNombre()); //saludo personalizado con el nombre del usuario
        finish();
        startActivity(intent);
    }

    /**
     * Pantalla invitado.
     *
     * @param usuario1 un usuario
     */
    public void pantallaInvitado(Usuario usuario1) {
        Intent intent = new Intent(MainActivity.this, MenuInvitado.class); //ponemos en la actividad el objeto usuario
        intent.putExtra(EXTRA_USUARIO, usuario1); //mandamos el usuario actual a la pantalla siguiente para no perder los datos
        ttsManager.initQueue("Bienvenido " + usuario1.getNombre()); //con este método hablamos al usuario cuando entra en la pantalla
        finish();
        startActivity(intent);
    }

    /**
     * Xml to java.
     */
    public void xmlToJava() {

        botonIniciar = findViewById(R.id.botonIniciarXML);
        botonRegistrar = findViewById(R.id.botonAceptarXML);
//        nombre1 = (EditText) findViewById(R.id.nombreXML);
        contraseña1 = findViewById(R.id.contraseñaXML);
        email1 = findViewById(R.id.emailXML);


    }


    /**
     * Activar animacion.
     */
    public void activarAnimacion() {
        ImageView miImagen = findViewById(R.id.imagenXML);
        Animation miAnim = AnimationUtils.loadAnimation(this, R.anim.anim2);
        miImagen.startAnimation(miAnim);
    }

    /**
     * Leer usuario.
     *
     * @param usuario1 un usuario
     */


    public Usuario leerUsuario(Usuario usuario1) {
        Usuario usuarioDeVuelta = new Usuario();
        try {
             String equipoServidor = "13.37.217.86"; //para pruebas locales , la version definitiva podrá salir de la red local
           // String equipoServidor = "servidorwebjordan.ddns.net"; usare un host de este estilo
             //String equipoServidor = "sistemalarma.ddns.net";
            int puertoServidor = 30566;
            Socket socketCliente = new Socket(equipoServidor, puertoServidor);
            ObjectOutputStream objetoEntregar = new ObjectOutputStream(socketCliente.getOutputStream());
            System.out.println("El objeto que mandara el cliente al servidor es: " + usuario1);
            objetoEntregar.writeObject(usuario1);//el cliente manda el objeto al server
            objetoEntregar.flush();


            //ahora recibimos el objeto de vuelta si es nulo no coincide y si no si
            ObjectInputStream objetoRecibido = new ObjectInputStream(socketCliente.getInputStream());
            usuarioDeVuelta = (Usuario) objetoRecibido.readObject(); //objeto leido y metido en usuarioDeVuelta


            objetoRecibido.close();
            socketCliente.close();
            objetoEntregar.close();
            return usuarioDeVuelta;


        } catch (IOException ex) {
            System.out.println(ex.getMessage());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return usuarioDeVuelta;


    }

    /**
     * Texto to voz.
     */
    public void textoToVoz() {
        ttsManager = new TTSManager();
        ttsManager.init(this);
    }


}