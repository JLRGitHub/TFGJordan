
package es.jordan.sistemaalarma;

import bd.Componente;
import bd.ExcepcionJordan;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Usuario;
  /**
     * Este servidor inserta usuario
     */
public class ServidorInsertarUsuario extends Thread{
    String secretKey = "enigma";
    @Override
    public void run() {
        try {
            int puertoServidor = 30500;
            ServerSocket socketServidor = new ServerSocket(puertoServidor);
           while(true)
           {
            Socket clienteConectado = socketServidor.accept();
            gestionarDialogo(clienteConectado);
            clienteConectado.close();
           
           }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ExcepcionJordan ex) {
            Logger.getLogger(ServidorInsertarUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * Método que implementa el diálogo con el cliente
     * @param clienteConectado Socket que se usa para realizar la comunicación con el cliente
    */
    public void gestionarDialogo(Socket clienteConectado) throws ExcepcionJordan {
        try {
       
            ObjectInputStream objetoRecibido = new ObjectInputStream(clienteConectado.getInputStream());
            Usuario usuario1 = (Usuario) objetoRecibido.readObject(); //objeto leido y metido en usuario1
            System.out.println("El servidor recibe un objeto del cliente "+usuario1.toString());
            Componente c=new Componente();
        
            c.insertarUsuario(usuario1);
            System.out.println("Usuario "+usuario1.getNombre()+" insertado con exito");
            
            objetoRecibido.close();
//            objetoEntregar.close();
           
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServidorInsertarUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

