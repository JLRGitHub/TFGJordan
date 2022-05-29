package es.jordan.sistemaalarma;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import pojos.Usuario;


public class AdministrarRespaldo extends AppCompatActivity {

    private ImageView copia,
            recuperar;
    private final String EXTRA_USUARIO = "";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_respaldo);
        final Usuario usuarioPasado = (Usuario) getIntent().getSerializableExtra(EXTRA_USUARIO);
        toolbar = findViewById(R.id.toolbarRespaldo);
        toolbar.setTitle("Administrador - " + usuarioPasado.getNombre());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);//quitamos el titulo del toolbar


        xmlToJava();
        //habilitar conexiones para los sockets
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /**
         * on click en la imagen hacer copia de seguridad
         */
        //on click de desconectar
        copia.setOnClickListener(v -> {

            avisarServer();
            Toast toast = Toast.makeText(getApplicationContext(), "Copia realizada con exito", Toast.LENGTH_LONG);
            toast.show();


        });
        /**
         * on click en la imagen importar copia de seguridad
         */
        //on click de desconectar
        recuperar.setOnClickListener(v -> {

            recuperarServer();
            Toast toast = Toast.makeText(getApplicationContext(), "Recuperación realizada con exito", Toast.LENGTH_LONG);
            toast.show();


        });


    }

    //iniciando el menu del toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuenbackup, menu);
        return true;
    }

    @Override

    /**
     * on click de cada elemento del toolbar
     */
    public boolean onOptionsItemSelected(MenuItem item) { //metodo que se encarga del toolbar
        //para que cada icono asignarle tareas diferentes
        switch (item.getItemId()) {

            //cuando clicamos en la flechita del toolbar
            case R.id.item1:
                //cuando clicamos en
            case R.id.item2:
                Intent intent = new Intent(getApplicationContext(), MenuAdmin.class); //flechita que vuelve al
                final Usuario usuarioPasado = (Usuario) getIntent().getSerializableExtra(EXTRA_USUARIO);
                intent.putExtra(EXTRA_USUARIO, usuarioPasado);
                startActivityForResult(intent, 0);
                return true;

            //volvemos de las 2 formas

            //cuando clicamos en el icono de salir del toolbar
            case R.id.item3:
                AlertDialog.Builder alert = new AlertDialog.Builder(AdministrarRespaldo.this);
                alert.setTitle("Advertencia");
                alert.setCancelable(true);
                alert.setIcon(R.drawable.exit);

                alert.setMessage("¿Desea desconectarse?");

                alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                alert.setPositiveButton("Si", (dialog, which) -> {

                    Intent llamada = new Intent(AdministrarRespaldo.this, MainActivity.class);
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
     * xml a java
     */
    public void xmlToJava() {
        copia = findViewById(R.id.botonCopia);
        recuperar = findViewById(R.id.botonRecuperar);
    }

    /**
     * Avisar server de que quiero copia de seguridad.
     */
    public void avisarServer() {

        try {
            String equipoServidor = "servidorwebjordan.ddns.net"; //para pruebas locales , la version definitiva podrá salir de la red local
            //String equipoServidor = "servidorwebjordan.ddns.net";
            int puertoServidor = 30520;
            Socket socketCliente = new Socket(equipoServidor, puertoServidor);
            OutputStream socketSalida = socketCliente.getOutputStream();
            DataOutputStream escribir = new DataOutputStream(socketSalida);
            escribir.writeUTF("Servidor insertar copia seguridad");

            escribir.flush();
            escribir.close();

            socketCliente.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Avisar al que quiero importar la copia
     */
    public void recuperarServer() {
        try {
            String equipoServidor = "servidorwebjordan.ddns.net"; //para pruebas locales , la version definitiva podrá salir de la red local
            //String equipoServidor = "servidorwebjordan.ddns.net";
            int puertoServidor = 30530;
            Socket socketCliente = new Socket(equipoServidor, puertoServidor);
            OutputStream socketSalida2 = socketCliente.getOutputStream();
            DataOutputStream escribir2 = new DataOutputStream(socketSalida2);
            escribir2.writeUTF("Servidor recuperar copia de seguridad");

            escribir2.flush();
            escribir2.close();

            socketCliente.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
