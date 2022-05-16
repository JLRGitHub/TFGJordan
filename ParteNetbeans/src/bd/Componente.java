package bd;

import pojos.Incidencia;
import pojos.Raspberry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import pojos.Usuario;
import java.sql.PreparedStatement;
import java.util.ArrayList;

/**
 * En esta clase se realiza el componente de acceso a datos
 *
 * @author Jordan Lopez
 * @version 1.7
 * @since 17/04/2022
 */
public class Componente {

    Connection conexion;

    /**
     * Constructor del componente
     *
     * @throws bd.ExcepcionJordan
     */
    public Componente() throws ExcepcionJordan {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ExcepcionJordan e = new ExcepcionJordan();
            e.setMensajeUsuario("Error general en el sistema. Consulta con el administrador");
            e.setMensajeAdministrador(ex.getMessage());
            throw e;
        }
    }

    /**
     * conexion a la b
     *
     * @throws excepcionJordan
     */
    private void conectarBD() throws ExcepcionJordan {
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/jordan", "root", "");
        } catch (SQLException ex) {
            ExcepcionJordan e = new ExcepcionJordan();
            e.setMensajeUsuario("Error General en el sistema. Consulta con el administrador");
            e.setMensajeAdministrador(ex.getMessage());
            e.setCodigoError(ex.getErrorCode());
            throw e;
        }
    }

    /**
     * desconexion a la b
     *
     * @throws excepcionJordan
     */
    private void desconectarBD() throws ExcepcionJordan {
        try {
            conexion.close();
        } catch (SQLException ex) {
            ExcepcionJordan e = new ExcepcionJordan();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeAdministrador(ex.getMessage());
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
    }

    /**
     * conexion a la b
     *
     * @param email
     * @param contraseña
     * @return usuario
     * @throws bd.ExcepcionJordan
     */
    //dado un mail y contraseña vemos si existe
    public Usuario usuarioExiste(String email, String contraseña) throws ExcepcionJordan {
        Usuario usuarioComprobado = new Usuario();

        String dml = "select * from usuario where email=? and contraseña=?";
        try {
            conectarBD();
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
//realizamos la select con este email y contraseña
            sentenciaPreparada.setString(1, email);
            sentenciaPreparada.setString(2, contraseña);

            ResultSet resultado = sentenciaPreparada.executeQuery();//obtenemos usuario o null depende
            //si lo hemos encontrado
            if (resultado.next()) {

                usuarioComprobado.setUsuarioId(resultado.getInt("usuario_id"));
                usuarioComprobado.setNombre(resultado.getString("nombre"));
                usuarioComprobado.setContraseña(resultado.getString("contraseña"));
                usuarioComprobado.setEmail(resultado.getString("email"));
                usuarioComprobado.setRol(resultado.getString("rol"));

            } else {
                return null;
            }

            sentenciaPreparada.close();
            desconectarBD();
        } catch (SQLException ex) {
            ExcepcionJordan e = new ExcepcionJordan();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeAdministrador(ex.getMessage());
            e.setSentenciaSql(dml);
        }

        return usuarioComprobado;
    }

    /**
     * Este método permite insertar un registro de la tabla producto.
     *
     * @param usuarioId id del usuario
     *
     * @return un usuario
     * @throws bd.ExcepcionJordan
     */
    public Usuario leerUsuario(Integer usuarioId) throws ExcepcionJordan {
        Usuario usuario1 = new Usuario();
        String dml = "select * from usuario where usuario_id=" + usuarioId;

        try {
            conectarBD();
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(dml);

            if (resultado.next()) {

                usuario1.setUsuarioId(resultado.getInt("usuario_id"));
                usuario1.setNombre(resultado.getString("nombre"));

                usuario1.setContraseña(resultado.getString("contraseña"));
                usuario1.setEmail(resultado.getString("email"));
                usuario1.setRol(resultado.getString("rol"));

            } else {
                return null;
            }

            sentencia.close();
            desconectarBD();
        } catch (SQLException ex) {
            ExcepcionJordan e = new ExcepcionJordan();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeAdministrador(ex.getMessage());
            e.setSentenciaSql(dml);
        }
        return usuario1;
    }

    /**
     * Este método permite eliminar usuario
     *
     * @param u usuario
     *
     * @return un entero
     * @throws bd.ExcepcionJordan
     */
    public int eliminarUsuario(Usuario u) throws ExcepcionJordan {
        int registrosAfectados = 0;
        String dml = "DELETE from USUARIO where usuario_id=" + u.getUsuarioId();

        try {
            conectarBD();
            Statement sentencia = conexion.createStatement();
            registrosAfectados = sentencia.executeUpdate(dml);
            sentencia.close();
            desconectarBD();
        } catch (SQLException ex) {
            ExcepcionJordan e = new ExcepcionJordan();
            e.setCodigoError(ex.getErrorCode());
            System.out.println(ex.getErrorCode());
            e.setMensajeAdministrador(ex.getMessage());
            e.setSentenciaSql(dml);
            switch (ex.getErrorCode()) {

                case 1451:
                    e.setMensajeUsuario("Imposible eliminar usuario porque tiene raspberries asociadas");//mysql
                    break;
                case 2292:

                    e.setMensajeUsuario("Imposible eliminar usuario porque tiene x asociados"); //oracle
                    break;
                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
                    break;
            }
            throw e;
        }
        return registrosAfectados;
    }

    /**
     * Este método permite leer usuarios
     *
     * @return arraylist de usuarios
     * @throws bd.ExcepcionJordan
     */
    public ArrayList<Usuario> leerUsuarios() throws ExcepcionJordan {
        ArrayList<Usuario> listaUsuario = new ArrayList();
        String dml = "select * from usuario";

        try {
            conectarBD();
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(dml);
            while (resultado.next()) {

                Usuario usuario1 = new Usuario();
                usuario1.setUsuarioId(resultado.getInt("usuario_id"));
                usuario1.setNombre(resultado.getString("nombre"));
                usuario1.setContraseña(resultado.getString("contraseña"));
                usuario1.setEmail(resultado.getString("email"));
                usuario1.setRol(resultado.getString("rol"));

                listaUsuario.add(usuario1);
            }

            sentencia.close();
            desconectarBD();
        } catch (SQLException ex) {
            ExcepcionJordan e = new ExcepcionJordan();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeAdministrador(ex.getMessage());
            e.setSentenciaSql(dml);
        }
        return listaUsuario;
    }

    /**
     * Este método permite insertar un usuario
     *
     * @param u un entero
     * @throws bd.ExcepcionJordan
     */
    public Integer insertarUsuario(Usuario u) throws ExcepcionJordan {
        int registrosAfectados = 0;
        String dml = "insert into usuario(nombre,contraseña,email,rol) values(?,?,?,?)";

        try {
            conectarBD();
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setString(1, u.getNombre());
            sentenciaPreparada.setString(2, u.getContraseña());
            sentenciaPreparada.setString(3, u.getEmail());
            sentenciaPreparada.setString(4, u.getRol());

            registrosAfectados = sentenciaPreparada.executeUpdate();

            sentenciaPreparada.close();
            desconectarBD();

        } catch (SQLException ex) {
            ExcepcionJordan e = new ExcepcionJordan();
            e.setMensajeAdministrador(ex.getMessage());
            e.setCodigoError(ex.getErrorCode());
            e.setSentenciaSql(dml);

            switch (ex.getErrorCode()) {
                case 1062:
                    e.setMensajeUsuario("Error: El nombre o email  estan repetidos");
                    break;
                case 1400:
                    e.setMensajeUsuario("Error: El nombre, email  y el identificador del usuario son obligatorios");
                    break;
                case 2291:
                    e.setMensajeUsuario("Error: El identificador de la clase no exsiste");
                    break;
                case 1:
                    e.setMensajeUsuario("Error: El email del usuario no se puede repetir");
                    break;
                default:
                    e.setMensajeUsuario("Error en el sistema. Consulta con el administrador");
                    break;
            }
            throw e;
        }

        return registrosAfectados;
    }

    /**
     * Este método permite insertar una incidencia
     *
     * @param n incidencia
     *
     * @return un entero
     * @throws bd.ExcepcionJordan
     */
    public Integer insertarIncidencia(Incidencia n) throws ExcepcionJordan {
        int registrosAfectados = 0;
        String dml = "insert into incidencia(raspberry_id,hora) values(?,?)";

        try {
            conectarBD();
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setInt(1, n.getRaspberryId().getRaspberryId());
            sentenciaPreparada.setString(2, n.getHora());

            registrosAfectados = sentenciaPreparada.executeUpdate();

            sentenciaPreparada.close();
            desconectarBD();

        } catch (SQLException ex) {
            ExcepcionJordan e = new ExcepcionJordan();
            e.setMensajeAdministrador(ex.getMessage());
            e.setCodigoError(ex.getErrorCode());
            e.setSentenciaSql(dml);

            switch (ex.getErrorCode()) {

                case 1400:
                    e.setMensajeUsuario("Error: La hora y los identificadores son obligatorios");
                    break;
                case 2291:
                    e.setMensajeUsuario("Error: El identificador de la clase no exsiste");
                    break;

                default:
                    e.setMensajeUsuario("Error en el sistema. Consulta con el administrador");
                    break;
            }
            throw e;
        }

        return registrosAfectados;
    }

    /**
     * Este método permite leer incidencias
     *
     *
     * @return una lista de incidencias
     * @throws bd.ExcepcionJordan
     */
    public ArrayList<Incidencia> leerIncidencias() throws ExcepcionJordan {
        ArrayList<Incidencia> listaIncidencias = new ArrayList();

        String dql = "Select * from Incidencia "
                + " where incidencia.raspberry_id=raspberry.raspberry_id";
        try {
            conectarBD();
            PreparedStatement sentencia = conexion.prepareStatement(dql);
            ResultSet resultado = sentencia.executeQuery(dql);

            while (resultado.next()) {

                Incidencia p = new Incidencia();

                p.setIncidenciaId(resultado.getInt("incidencia_id"));
                p.setHora(resultado.getString("hora"));
                p.setRaspberryId(new Raspberry(resultado.getInt("raspberry_id"), resultado.getString("modelo"),
                        resultado.getString("memoria"), resultado.getString("direccion")));

                listaIncidencias.add(p);
            }
            resultado.close();
            sentencia.close();
            conexion.close();
            desconectarBD();

        } catch (SQLException ex) {
            ExcepcionJordan e = new ExcepcionJordan();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeAdministrador(ex.getMessage());
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
        return listaIncidencias;
    }

    /**
     * Este método permite leer datos relevantes
     *
     *
     * @return un usuario
     * @throws bd.ExcepcionJordan
     */
    public ArrayList<Incidencia> leerDatosRelevantesIncidencia() throws ExcepcionJordan {
        ArrayList<Incidencia> listaIncidencias = new ArrayList();

        String dql = "Select * from Incidencia i,raspberry r where i.raspberry_id=r.raspberry_id";
        try {
            conectarBD();
            PreparedStatement sentencia = conexion.prepareStatement(dql);
            ResultSet resultado = sentencia.executeQuery(dql);

            while (resultado.next()) {

                Incidencia p = new Incidencia();

                p.setIncidenciaId(resultado.getInt("incidencia_id"));
                p.setHora(resultado.getString("hora"));
                p.setRaspberryId(new Raspberry(resultado.getInt("raspberry_id"), resultado.getString("modelo"),
                        resultado.getString("memoria"), resultado.getString("direccion")));

                listaIncidencias.add(p);
            }
            resultado.close();
            sentencia.close();
            conexion.close();
            desconectarBD();

        } catch (SQLException ex) {
            ExcepcionJordan e = new ExcepcionJordan();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeAdministrador(ex.getMessage());
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
        return listaIncidencias;
    }

    /**
     * Este método permite leer todas las rasp
     *
     * @return un usuario
     * @throws bd.ExcepcionJordan
     */
    public ArrayList<Raspberry> leerRaspberrys() throws ExcepcionJordan {
        ArrayList<Raspberry> listaRaspberry = new ArrayList();
        String dml = "select * from Raspberry";

        try {
            conectarBD();
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(dml);
            while (resultado.next()) {
                Raspberry raspberry1 = new Raspberry();

                raspberry1.setRaspberryId(resultado.getInt("raspberry_id"));
                raspberry1.setModelo(resultado.getString("modelo"));
                raspberry1.setMemoria(resultado.getString("memoria"));
                raspberry1.setDireccion(resultado.getString("direccion"));

                listaRaspberry.add(raspberry1);
            }

            sentencia.close();
            desconectarBD();
        } catch (SQLException ex) {
            ExcepcionJordan e = new ExcepcionJordan();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeAdministrador(ex.getMessage());
            e.setSentenciaSql(dml);
        }
        return listaRaspberry;
    }

    /**
     * Este método permite leer una lista de raspis de un usuario
     *
     * @param id
     * @param usuarioId id del usuario
     * @return un usuario
     * @throws bd.ExcepcionJordan
     */
    public ArrayList<Raspberry> leerRaspberrysDeUnUsuario(int id) throws ExcepcionJordan {
        ArrayList<Raspberry> listaRaspberry = new ArrayList();
        //vamos a obtener la direccion y el modelo en la select
        String dml = "SELECT raspberry.raspberry_id,raspberry.modelo,raspberry.memoria,raspberry.direccion "
                + "FROM usuras,raspberry "
                + "WHERE usuras.usuario_id=" + id + " and raspberry.raspberry_id=usuras.raspberry_id";
        System.out.println(dml);
        try {
            conectarBD();
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(dml);
            while (resultado.next()) {
                Raspberry raspberry1 = new Raspberry();
                raspberry1.setRaspberryId(resultado.getInt("raspberry_id"));
                raspberry1.setModelo(resultado.getString("modelo"));
                raspberry1.setMemoria(resultado.getString("memoria"));
                raspberry1.setDireccion(resultado.getString("direccion"));

                listaRaspberry.add(raspberry1);
            }

            sentencia.close();
            desconectarBD();
        } catch (SQLException ex) {
            ExcepcionJordan e = new ExcepcionJordan();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeAdministrador(ex.getMessage());
            e.setSentenciaSql(dml);
        }
        return listaRaspberry;
    }

    public ArrayList<Incidencia> leerIncidenciasDeUnaRaspberry(int id) throws ExcepcionJordan {
        ArrayList<Incidencia> listaIncidencias = new ArrayList();

        String dql = "select * from incidencia,raspberry where incidencia.raspberry_id= " + id + " and raspberry.raspberry_id=" + id;
        // System.out.println(dql);
        try {
            conectarBD();
            PreparedStatement sentencia = conexion.prepareStatement(dql);
            ResultSet resultado = sentencia.executeQuery(dql);

            while (resultado.next()) {

                Incidencia p = new Incidencia();

                p.setIncidenciaId(resultado.getInt("incidencia_id"));
                p.setRaspberryId(new Raspberry(resultado.getInt("raspberry_id"), resultado.getString("modelo"),
                        resultado.getString("memoria"), resultado.getString("direccion")));
                p.setHora(resultado.getString("hora"));

                listaIncidencias.add(p);
            }
            resultado.close();
            sentencia.close();
            conexion.close();
            desconectarBD();

        } catch (SQLException ex) {
            ExcepcionJordan e = new ExcepcionJordan();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeAdministrador(ex.getMessage());
            e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
        return listaIncidencias;
    }

    /**
     * Este método permite modificar un usuario
     *
     * @param usuario
     * @return un entero
     * @throws bd.ExcepcionJordan
     */
    public Integer modificarUsuario(Usuario usuario) throws ExcepcionJordan {
        conectarBD();
        Integer registrosAfectados = 0;
        String dml = "update usuario set nombre = ?, email = ?, rol = ? where usuario_id = ?";
        try {
            PreparedStatement sentencia = conexion.prepareStatement(dml);

            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getEmail());
            sentencia.setString(3, usuario.getRol());
            sentencia.setInt(4, usuario.getUsuarioId());

            registrosAfectados = sentencia.executeUpdate();
            sentencia.close();
            conexion.close();
            desconectarBD();
        } catch (SQLException ex) {
            ExcepcionJordan e = new ExcepcionJordan();
            e.setMensajeAdministrador(ex.getMessage());
            e.setCodigoError(ex.getErrorCode());

            switch (ex.getErrorCode()) {
                case 1:
                    e.setMensajeUsuario("El identificador de usuario no se puede repetir.");
                    break;
                case 1407:
                    e.setMensajeUsuario("Todos los campos son obligatorios.");
                    break;

                default:
                    e.setMensajeUsuario("Error general del sistema, consulte con el administrador");
                    break;
            }
            throw e;
        }
        return registrosAfectados;
    }

    /**
     * Este método permite insertar una raspberry en un usuaro.
     *
     * @param idUsuario
     * @param idRaspberry
     * @return un entero
     * @throws bd.ExcepcionJordan
     */
    public Integer insertarRaspberryEnUsuario(int idUsuario, int idRaspberry) throws ExcepcionJordan {
        int registrosAfectados = 0;
        String dml = "insert into usuras(usuario_id,raspberry_id) values(?,?)";

        try {
            conectarBD();
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);

            sentenciaPreparada.setInt(1, idUsuario);
            sentenciaPreparada.setInt(2, idRaspberry);

            registrosAfectados = sentenciaPreparada.executeUpdate();

            sentenciaPreparada.close();
            desconectarBD();

        } catch (SQLException ex) {
            ExcepcionJordan e = new ExcepcionJordan();
            e.setMensajeAdministrador(ex.getMessage());
            e.setCodigoError(ex.getErrorCode());
            e.setSentenciaSql(dml);

            switch (ex.getErrorCode()) {

                case 1400:
                    e.setMensajeUsuario("Error: Los identificadores son obligatorios");
                    break;
                case 2291:
                    e.setMensajeUsuario("Error: El identificador de la clase no exsiste");
                    break;

                default:
                    e.setMensajeUsuario("Error en el sistema. Consulta con el administrador");
                    break;
            }
            throw e;
        }

        return registrosAfectados;
    }

    /**
     * Este método permite eliminar un usuras
     *
     * @param id
     * @return un usuario
     * @throws bd.ExcepcionJordan
     */
    public int eliminarUsuras(int id) throws ExcepcionJordan {
        int registrosAfectados = 0;
        String dml = "DELETE from Usuras where usuario_id=" + id;
        System.out.println(dml);

        try {
            conectarBD();
            Statement sentencia = conexion.createStatement();
            registrosAfectados = sentencia.executeUpdate(dml);
            sentencia.close();
            desconectarBD();
        } catch (SQLException ex) {
            ExcepcionJordan e = new ExcepcionJordan();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeAdministrador(ex.getMessage());
            e.setSentenciaSql(dml);
            switch (ex.getErrorCode()) {

                default:
                    e.setMensajeUsuario("Error general del sistema. Consulte con el administrador");
                    break;
            }
            throw e;
        }
        return registrosAfectados;
    }

}
