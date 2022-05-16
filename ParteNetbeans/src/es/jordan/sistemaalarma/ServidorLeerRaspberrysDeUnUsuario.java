/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.jordan.sistemaalarma;

import pojos.Raspberry;
import bd.Componente;
import bd.ExcepcionJordan;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Usuario;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Este servidor leer raspis de un usuario
 */
public class ServidorLeerRaspberrysDeUnUsuario extends Thread {

    @Override
    public void run() {
        try {
            int puertoServidor = 30560;
            ServerSocket socketServidor = new ServerSocket(puertoServidor);
            while (true) {
                Socket clienteConectado = socketServidor.accept();
                gestionarDialogo(clienteConectado);
                clienteConectado.close();

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ExcepcionJordan ex) {
            Logger.getLogger(ServidorLeerRaspberrysDeUnUsuario.class.getName()).log(Level.SEVERE, null, ex);
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

            //hay q leer de un usuario
            ObjectInputStream objetoRecibido = new ObjectInputStream(clienteConectado.getInputStream());
            Usuario usuarioRecibido = (Usuario) objetoRecibido.readObject();
            System.out.println("Usuario obtenido " + usuarioRecibido);
//casteamos el objeto a usuario
            //ahora necesitamos el id del usuario para aplicar el metodo leerraspberrysdeunusuario

            ArrayList<Raspberry> listaRaspberrysDeUnUsuario = new ArrayList();
            listaRaspberrysDeUnUsuario = c.leerRaspberrysDeUnUsuario(usuarioRecibido.getUsuarioId());

            //ahora que ya tenemos la lista de raspberrys de ese usuario se la mandamos al cliente
            ObjectOutputStream listaEntregar = new ObjectOutputStream(clienteConectado.getOutputStream());
            System.out.println("La lista que mandara el cliente al servidor ess: " + listaRaspberrysDeUnUsuario);
            listaEntregar.writeObject(listaRaspberrysDeUnUsuario);//el cliente manda el objeto al server
            listaEntregar.flush();

            listaEntregar.close();
            objetoRecibido.close();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServidorLeerRaspberrysDeUnUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String obtenerFechaYHoraActual() {
        String formato = "dd-MM-yyyy HH:mm:ss";
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern(formato);
        LocalDateTime ahora = LocalDateTime.now();
        return formateador.format(ahora);
    }
}
