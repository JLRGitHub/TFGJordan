package es.jordan.sistemaalarma;

import bd.ExcepcionJordan;
import backup.ServidorRecuperarSeguridad;
import backup.ServidorCopiaSeguridad;
import java.io.IOException;
import java.sql.SQLException;

    /**
     * Este programa tendra numerosos systems para ayudar al administrador principal, es decir yo
     * ver que esta pasando en todo momento de forma comoda 
     */
public class Main {

    public static void main(String[] args) throws ExcepcionJordan, SQLException, ClassNotFoundException, IOException {

        Thread s1 = new ServidorInsertarUsuario();
        s1.start();
        Thread s2 = new ServidorEliminar();
        s2.start();
        Thread s3 = new ServidorListarUsuarios();
        s3.start();
        Thread s4 = new ServidorInsertarIncidencia();
        s4.start();
        Thread s5 = new ServidorListarRaspberrys();
        s5.start();
        Thread s6 = new ServidorCopiaSeguridad();
        s6.start();
        Thread s7 = new ServidorRecuperarSeguridad();
        s7.start();
        Thread s8 = new ServidorLeerRaspberrysDeUnUsuario();
        s8.start();
        Thread s9 = new ServidorObtenerIncidenciasRaspberry();
        s9.start();
        Thread s10 = new ServidorModificarUsuario();
        s10.start();
        Thread s11 = new ServidorInsertarRaspberryEnUsuario();
        s11.start();
        Thread s12 = new ServidorComprobarUsuario();
        s12.start();
        Thread s13 = new ServidorEliminarUsuras();
        s13.start();

      

    }

}
