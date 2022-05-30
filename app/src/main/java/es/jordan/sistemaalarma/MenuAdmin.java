package es.jordan.sistemaalarma;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import pojos.Usuario;

/**
 * pantalla Menu admin.
 */
public class MenuAdmin extends AppCompatActivity {
    private ImageView añadir, eliminar, listar, modificar, botonusuarioraspberry;
    private Toolbar toolbar;
    private final String EXTRA_USUARIO = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);
        final Usuario usuarioPasado = (Usuario) getIntent().getSerializableExtra(EXTRA_USUARIO);
        toolbar = findViewById(R.id.toolbarMenuAdmin);
        toolbar.setTitle("Administrador - " + usuarioPasado.getNombre());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);//quitamos el titulo del toolbar


        xmlToJava();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /**
         * on click modificar usuario
         */
        //on click de modificar
        modificar.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ModificarUsuario.class);
            intent.putExtra(EXTRA_USUARIO, usuarioPasado);
            startActivityForResult(intent, 0);


        });
        /**
         * on click asignar raspberries a usuario.
         */
        //on click asignar raspberries
        botonusuarioraspberry.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), UsuarioConRaspberry.class);
            intent.putExtra(EXTRA_USUARIO, usuarioPasado);
            startActivityForResult(intent, 0);


        });

        /**
         * onclick añadir usuario
         */
        //on click añadir usuariosh
        añadir.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AnadirUsuarios.class);
            intent.putExtra(EXTRA_USUARIO, usuarioPasado);
            startActivityForResult(intent, 0);


        });
        /**
         * onclick listar usuarios
         */
        //on click listar usuarios
        listar.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), ListarUsuarios.class);
            intent.putExtra(EXTRA_USUARIO, usuarioPasado);
            startActivityForResult(intent, 0);

        });
        /**
         * onclick eliminar usuario
         */
        //on click de desconectar
        eliminar.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EliminarUsuario.class);
            intent.putExtra(EXTRA_USUARIO, usuarioPasado);
            startActivityForResult(intent, 0);


        });

    }

    /**
     * Inicializar menu toolbar
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenusolosalida, menu);
        return true;
    }

    /**
     * on click del toolbar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //metodo que se encarga del toolbar
        //para que cada icono asignarle tareas diferentes
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(getApplicationContext(), MenuAdmin.class); //flechita que vuelve al
                startActivityForResult(intent, 0);
                return true;


           //menu preparado por si un día se amplia el proyecto y se permite hacer copias de seguridad automaticas en EBS
            //de momento se realizaran manualmente y semanalmente. Este icono se dirigiría a administrar respaldo.

         /*   case R.id.item2: //icono disco duro
                Intent intent2 = new Intent(getApplicationContext(), AdministrarRespaldo.class);
                final Usuario usuarioPasado = (Usuario) getIntent().getSerializableExtra(EXTRA_USUARIO);
                intent2.putExtra(EXTRA_USUARIO, usuarioPasado);
                startActivityForResult(intent2, 0);

                return true;
*/
            case R.id.item3: //icono salir
                AlertDialog.Builder alert = new AlertDialog.Builder(MenuAdmin.this);
                alert.setTitle("Advertencia");
                alert.setCancelable(true);
                alert.setIcon(R.drawable.exit);

                alert.setMessage("¿Desea desconectarse?");

                alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                alert.setPositiveButton("Si", (dialog, which) -> {

                    Intent llamada = new Intent(MenuAdmin.this, MainActivity.class);
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
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        añadir = findViewById(R.id.botonAñadir);
        eliminar = findViewById(R.id.botonEliminar);
        modificar = findViewById(R.id.botonModificar);
        listar = findViewById(R.id.botonListar);
        botonusuarioraspberry = findViewById(R.id.botonusuarioraspberry);


    }


}




    

