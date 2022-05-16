package es.jordan.sistemaalarma;

import bd.Componente;
import bd.ExcepcionJordan;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Usuario;
import java.util.ArrayList;

public class ServidorListarUsuarios extends Thread {
   
    @Override
    public void run() {
        try {
            int puertoServidor = 30504;
            ServerSocket socketServidor = new ServerSocket(puertoServidor);
            while (true) {
                Socket clienteConectado = socketServidor.accept();
                gestionarDialogo(clienteConectado);
                clienteConectado.close();
          
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ExcepcionJordan ex) {
            Logger.getLogger(ServidorListarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método que implementa el diálogo con el cliente
     *
     * @param clienteConectado Socket que se usa para realizar la comunicación
     * con el cliente
     */
    public void gestionarDialogo(Socket clienteConectado) throws ExcepcionJordan {
        try {
            Componente c = new Componente();
       
            ObjectOutputStream objetoEntregar = new ObjectOutputStream(clienteConectado.getOutputStream());
            //me preparo para entregar

            ArrayList<Usuario> listaUsuarios = new ArrayList();
            listaUsuarios = c.leerUsuarios();
            //desencriptamos la contraseña antes de mandar la lista
          
            System.out.println("La lista que mandara el server al usuario es " + listaUsuarios.toString());
            //lo mando
            objetoEntregar.writeObject(listaUsuarios);//el cliente manda el objeto al server
            objetoEntregar.close();

//            objetoEntregar.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
