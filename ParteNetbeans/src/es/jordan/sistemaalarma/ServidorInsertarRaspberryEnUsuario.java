package es.jordan.sistemaalarma;

import bd.Componente;
import bd.ExcepcionJordan;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServidorInsertarRaspberryEnUsuario extends Thread {
/**
 * Servidor que usaremos cuando asignemos raspberrys a usuario
 */
    @Override
    public void run() {
        try {
            int puertoServidor = 30800;
            ServerSocket socketServidor = new ServerSocket(puertoServidor);
            while (true) {
                Socket clienteConectado = socketServidor.accept();
                gestionarDialogo(clienteConectado);
                clienteConectado.close();
               
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ExcepcionJordan ex) {
            Logger.getLogger(ServidorInsertarRaspberryEnUsuario.class.getName()).log(Level.SEVERE, null, ex);
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

            int idUsuario = 0;
            int idRaspberry = 0;
            ObjectInputStream objetoRecibido = new ObjectInputStream(clienteConectado.getInputStream());
            //el esclavo solo tiene 2 atributos el id de la raspberry y el id del usuario
            Esclavo esclavo = (Esclavo) objetoRecibido.readObject(); //objeto leido y metido en esclavo
            idUsuario = esclavo.getUsuarioId();
            idRaspberry = esclavo.getRaspberryId();

            Componente c = new Componente();

            System.out.println("Insertamos en usuras la raspberry y el usuario");
            c.insertarRaspberryEnUsuario(idUsuario, idRaspberry);

            objetoRecibido.close();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServidorInsertarRaspberryEnUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String obtenerFechaYHoraActual() {
        String formato = "dd-MM-yyyy HH:mm:ss";
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern(formato);
        LocalDateTime ahora = LocalDateTime.now();
        return formateador.format(ahora);
    }
}
