package es.jordan.sistemaalarma;

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

public class ServidorComprobarUsuario extends Thread {

    @Override
    public void run() {
        try {
            int puertoServidor = 30566;
            ServerSocket socketServidor = new ServerSocket(puertoServidor);
            while (true) {
                Socket clienteConectado = socketServidor.accept();
                gestionarDialogo(clienteConectado);
                clienteConectado.close();

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ExcepcionJordan ex) {
            Logger.getLogger(ServidorComprobarUsuario.class.getName()).log(Level.SEVERE, null, ex);
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

            ObjectInputStream objetoRecibido = new ObjectInputStream(clienteConectado.getInputStream());
            Usuario usuario1 = (Usuario) objetoRecibido.readObject(); //objeto leido y metido en usuario1
            System.out.println("El servidor recibe un objeto del cliente " + usuario1.toString());
            Componente c = new Componente();

            Usuario aux = new Usuario();
            aux = c.usuarioExiste(usuario1.getEmail(), usuario1.getContraseña());

            if (aux == null) { //aqui solo entra si no hay usuario con esos credenciales
                //ya que en el componente devolvemos nulo cuando no coincide
                System.out.println("Usuario no existe con ese email y contraseña");
                Usuario aux1 = new Usuario();
                aux1.setNombre("*");
                aux1.setEmail("*");
                ObjectOutputStream objetoEntregar = new ObjectOutputStream(clienteConectado.getOutputStream());
                System.out.println("Cliente manda " + aux1.toString());
                objetoEntregar.writeObject(aux1);//el server manda el * para avisar al cliente
                // que esos credenciales son invalidos
                objetoEntregar.flush();
                objetoEntregar.close();

            } else {
                System.out.println("El usuario existe y se llama " + aux.getNombre());
                ObjectOutputStream objetoEntregar = new ObjectOutputStream(clienteConectado.getOutputStream());

                System.out.println("Cliente manda " + aux.toString());
                objetoEntregar.writeObject(aux);//el cliente manda el objeto al server
                objetoEntregar.flush();
                objetoEntregar.close();
            }

            objetoRecibido.close();
        } catch (ExcepcionJordan ex) {

            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(ServidorComprobarUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServidorComprobarUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
