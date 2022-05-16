/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backup;


import bd.ExcepcionJordan;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class ServidorCopiaSeguridad extends Thread {
/**
 * Servidor que usaremos cuando queramos una copia de seguridad
 */
    @Override
    public void run() {
        try {
            int puertoServidor = 30520;
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
            Logger.getLogger(ServidorCopiaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
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

            InputStream ClienteEntrante = clienteConectado.getInputStream();
            DataInputStream leer = new DataInputStream(ClienteEntrante);
            System.out.println("Servidor.Consola - Mensaje recibido del Cliente: " + leer.readUTF());
            System.out.println("El servidor recibe que quieres hacer una copia de seguridad ");
            //ejecutamos un archivo batch donde se genera la copia de seguridad
            //con un runtime abriendolo usando la consola

            try {
                System.out.println("Iniciando copia de seguridad");
                Process p = Runtime.getRuntime().exec("cmd.exe /c start C:\\copiaconpermisos\\copiaSeguridad.bat");
                p.waitFor();
                subirDeLocalANube();

            } catch (IOException ex) {
                //Validate the case the file can't be accesed (not enought permissions)

            } catch (InterruptedException ex) {
                //Validate the case the process is being stopped by some external situation     

            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());

        }

    }

    public void subirDeLocalANube() {
        {
            try {
            System.out.println("Estableciendo conexión con el Servidor FTP");
            FTPClient clienteFTP = new FTPClient();
            String servidorFTP = "192.168.1.36";
            clienteFTP.connect(servidorFTP);
            mostrarRespuesta(clienteFTP);

            System.out.println("Autenticándose ante el Servidor FTP");
            String usuario = "jordan";
            String contrasena = "jordan";
            boolean login = clienteFTP.login(usuario, contrasena);
            mostrarRespuesta(clienteFTP);


            System.out.println("Nos ponemos en el escriterio");
            //clienteFTP.changeWorkingDirectory("nube");
            //mostrarRespuesta(clienteFTP);

            
            
            //RESTAURAR BACKUP
            
            System.out.println("subiendo el fichero de local a la carpeta de la nube");
            clienteFTP.setFileType(FTP.BINARY_FILE_TYPE);
            FileInputStream fis = new FileInputStream("C:\\copiaconpermisos\\dataBaseBackup.sql");
            BufferedInputStream bis = new BufferedInputStream(fis);
            boolean subida = clienteFTP.storeFile("dataBaseBackup.sql", bis);
            mostrarRespuesta(clienteFTP);

            System.out.println("Haciendo logout del Servidor FTP");
            boolean logout = clienteFTP.logout();
            mostrarRespuesta(clienteFTP);

            System.out.println("Desconectando del Servidor FTP");
            
            
            
           
            clienteFTP.disconnect();
            mostrarRespuesta(clienteFTP);
        } catch (IOException ex) {
            System.out.println("Excepción: " + ex);
        }}}

    public static String convertirUTF8(String mensaje) {
        if (mensaje == null) {
            return null;
        } else {
            byte[] apoyo = mensaje.getBytes(ISO_8859_1);
            return (new String(apoyo, UTF_8));
        }
    }

    /**
     * Método que muestra la respuesta enviada por el servidor FTP al cliente
     *
     * @param clienteFTP String en formato ISO-8859-1
     */
    public static void mostrarRespuesta(FTPClient clienteFTP) {
        String mensajeRespuesta = convertirUTF8(clienteFTP.getReplyString());
        System.out.println("Respuesta:\n" + mensajeRespuesta);
        int codigoRespuesta = clienteFTP.getReplyCode();
        if (!FTPReply.isPositiveCompletion(codigoRespuesta)) {
            System.out.println("ERROR de Conexión - Código de Error: " + codigoRespuesta);
        }
    }

    /**
     * Método que muestra algunos de los datos de un objeto FTPFile
     *
     * @param fichero Fichero del que obtener los datos
     */
    public static void mostrarFichero(FTPFile fichero) {
        String tipos[] = {"Fichero", "Directorio", "Enlace Simbólico"};
        System.out.printf("%30s %20d %25s\n", fichero.getName(), fichero.getSize(), tipos[fichero.getType()]);
    }

}
