package es.jordan.sistemaalarma;

import pojos.Raspberry;
import pojos.Incidencia;
import bd.Componente;
import bd.ExcepcionJordan;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.DataInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServidorInsertarIncidencia extends Thread {
    /**
     * Este servidor inserta incidencias
     */
    @Override
    public void run() {
        try {
            int puertoServidor = 30508;
            ServerSocket socketServidor = new ServerSocket(puertoServidor);
            while (true) {
                Socket clienteConectado = socketServidor.accept();
                gestionarDialogo(clienteConectado);
                clienteConectado.close();
      
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ExcepcionJordan ex) {
            Logger.getLogger(ServidorInsertarIncidencia.class.getName()).log(Level.SEVERE, null, ex);
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

            DataInputStream recibir = new DataInputStream(clienteConectado.getInputStream());
            int idRaspberryMensajera = recibir.readInt(); 
           //recibimos el id de la raspberry por socket y asi podremos saber todas las incidencias de esa 
           //raspberry

            System.out.println("Una vez recibimos la señal sabemos "
                    + "que ha habido algun problema y lo registramos en la bd con la hora actual y el id de la raspberry ");
            Componente c = new Componente();

            Incidencia incidencia1 = new Incidencia();
            Raspberry raspberry1 = new Raspberry();
            raspberry1.setRaspberryId(idRaspberryMensajera);

            incidencia1.setRaspberryId(raspberry1);
            //incidencia1.setHora(java.time.LocalDateTime.now().toString());
            incidencia1.setHora(obtenerFechaYHoraActual());
            c.insertarIncidencia(incidencia1);
            System.out.println("incidencia de hora " + incidencia1.getHora() + " insertada con exito");

            recibir.close();

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
