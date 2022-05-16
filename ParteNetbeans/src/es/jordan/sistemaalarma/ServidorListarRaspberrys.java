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
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Este servidor lista raspis
 */
public class ServidorListarRaspberrys extends Thread {

    @Override
    public void run() {
        try {
            int puertoServidor = 30510;
            ServerSocket socketServidor = new ServerSocket(puertoServidor);
            while (true) {
                Socket clienteConectado = socketServidor.accept();
                gestionarDialogo(clienteConectado);
                clienteConectado.close();

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ExcepcionJordan ex) {
            Logger.getLogger(ServidorListarRaspberrys.class.getName()).log(Level.SEVERE, null, ex);
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

            ArrayList<Raspberry> listaRaspberrys = new ArrayList();
            listaRaspberrys = c.leerRaspberrys();

            System.out.println("La lista que mandara el server al usuario es " + listaRaspberrys.toString());
            //lo mando
            objetoEntregar.writeObject(listaRaspberrys);//el cliente manda el objeto al server
            objetoEntregar.close();

//            objetoEntregar.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static String obtenerFechaYHoraActual() {
        String formato = "dd-MM-yyyy HH:mm:ss";
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern(formato);
        LocalDateTime ahora = LocalDateTime.now();
        return formateador.format(ahora);
    }
}
