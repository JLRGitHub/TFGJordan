package es.jordan.sistemaalarma;

import bd.Componente;
import bd.ExcepcionJordan;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


import java.io.DataInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorEliminarUsuras extends Thread {
  /**
     * Este servidor elimina usuras
     */
    @Override
    public void run() {
        try {
            int puertoServidor = 30799;
            ServerSocket socketServidor = new ServerSocket(puertoServidor);
            while (true) {
                Socket clienteConectado = socketServidor.accept();
                gestionarDialogo(clienteConectado);
                clienteConectado.close();
                // socketServidor.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ExcepcionJordan ex) {
            Logger.getLogger(ServidorEliminarUsuras.class.getName()).log(Level.SEVERE, null, ex);
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

            InputStream socketEntrada = clienteConectado.getInputStream();
            DataInputStream leer = new DataInputStream(socketEntrada);
           // System.out.println(leer.readInt());
            int id=leer.readInt();
            Componente c = new Componente();
            System.out.println("Eliminando todas las raspberrys del usuario");
            c.eliminarUsuras(id);

            leer.close();
//            objetoEntregar.close();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

