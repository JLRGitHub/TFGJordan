/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.jordan.sistemaalarma;

import bd.ExcepcionJordan;
import pojos.Incidencia;
import bd.Componente;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.DataInputStream;
import java.io.InputStream;
import static java.lang.Integer.parseInt;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Este servidor obtiene una incidencia de raspberry
 */
public class ServidorObtenerIncidenciasRaspberry extends Thread {

    @Override
    public void run() {
        try {
            int puertoServidor = 30570;
            ServerSocket socketServidor = new ServerSocket(puertoServidor);
            while (true) {
                Socket clienteConectado = socketServidor.accept();
                gestionarDialogo(clienteConectado);
                clienteConectado.close();

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ExcepcionJordan ex) {
            Logger.getLogger(ServidorObtenerIncidenciasRaspberry.class.getName()).log(Level.SEVERE, null, ex);
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
            //1º paso recibir el id de la raspberry a la que quiero ver las incidencias
            InputStream socketEntrada = clienteConectado.getInputStream();
            DataInputStream leer = new DataInputStream(socketEntrada);
            String id = leer.readUTF();
            int idNumerico = parseInt(id);
            System.out.println("El id de raspberry a leer incidencias es " + idNumerico);
            //2º paso meter el id en el metodo de listar incidencias y devolverla al cliente
            ArrayList<Incidencia> listaIncidenciasDeEsaRaspberry = new ArrayList();
            listaIncidenciasDeEsaRaspberry = c.leerIncidenciasDeUnaRaspberry(idNumerico);
            //3º paso como tenemos mezclados muchos atributos en la lista vamos a pasar solo los datos que necesitamos

            ObjectOutputStream objetoEntregar = new ObjectOutputStream(clienteConectado.getOutputStream());

            System.out.println("El objeto que mandara el cliente al servidor es: " + listaIncidenciasDeEsaRaspberry.toString());
            objetoEntregar.writeObject(listaIncidenciasDeEsaRaspberry);//el cliente manda el objeto al server

        } catch (IOException ex) {
            Logger.getLogger(ServidorObtenerIncidenciasRaspberry.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String obtenerFechaYHoraActual() {
        String formato = "dd-MM-yyyy HH:mm:ss";
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern(formato);
        LocalDateTime ahora = LocalDateTime.now();
        return formateador.format(ahora);
    }
}
