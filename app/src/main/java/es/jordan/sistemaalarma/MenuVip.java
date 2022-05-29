package es.jordan.sistemaalarma;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import pojos.Usuario;


/**
 * pantalla Menu vip.
 */
public class MenuVip extends AppCompatActivity {
    private ImageView BotonVerCamara, BotonVerFotos, BotonSos, botonListarIncidencias;
    private final String EXTRA_USUARIO = "";
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_vip);
        final Usuario usuarioPasado = (Usuario) getIntent().getSerializableExtra(EXTRA_USUARIO);
        toolbar = findViewById(R.id.toolbarMenuVip);
        toolbar.setTitle("Usuario VIP - " + usuarioPasado.getNombre());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        xmlToJava();

        /**
         * onclick ver streaming
         */
        BotonVerCamara.setOnClickListener(v -> {


            Intent intent = new Intent(v.getContext(), Streaming.class);
            intent.putExtra(EXTRA_USUARIO, usuarioPasado);
            //  Toast toast = Toast.makeText(getApplicationContext(),usuarioPasado.toString(), Toast.LENGTH_LONG);
            //  toast.show();

            startActivityForResult(intent, 0);
        });
        /**
         * onclick ver galeria de fotos
         */


        BotonVerFotos.setOnClickListener(v -> {

                //esto abre el email que tengamos configurado por defecto en android que sera el que utilicemos
                //para recibir las fotos


               // Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.email");
               // startActivity(intent);

            try {// atrapamos con un try catch si hubiera un problema de permisos
                // y no se pudiera abrir el correo no se cierra la app

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            startActivity(intent);
            } catch(Exception e) {}




        });
        /**
         * onclick boton auxilio
         */
        BotonSos.setOnClickListener(v -> {
            //la diferencia que radica entre un action_dial y un action_call es que en el call
            //esq dial puedes editar el numero y el call llama directamente
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:112"));
            startActivity(intent);
        });
        /**
         * onclick listar incidencia
         */
        botonListarIncidencias.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), ListarIncidencias.class);
            intent.putExtra(EXTRA_USUARIO, usuarioPasado);
            startActivityForResult(intent, 0);
        });
    }


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
                // Toast toast2 = Toast.makeText(getApplicationContext(),"pincha 2", Toast.LENGTH_LONG);
                //toast2.show();
                return true;

            case R.id.item3:
                AlertDialog.Builder alert = new AlertDialog.Builder(MenuVip.this);
                alert.setTitle("Advertencia");
                alert.setCancelable(true);
                alert.setIcon(R.drawable.exit);

                alert.setMessage("¿Desea desconectarse?");

                alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                alert.setPositiveButton("Si", (dialog, which) -> {

                    Intent llamada = new Intent(MenuVip.this, MainActivity.class);
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

        BotonVerCamara = findViewById(R.id.botonVerCamara);
        BotonVerFotos = findViewById(R.id.botonVerFotos);
        BotonSos = findViewById(R.id.botonSos);
        botonListarIncidencias = findViewById(R.id.botonListarIncidencias);
    }


}
