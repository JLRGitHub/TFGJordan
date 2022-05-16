/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bd;



/**
 * En esta clase se gestionan las exceptiones del componente, devolviendo un mensaje al usuario,
 * otro al administrador, el codigo de error y la sentencia SQL quese ha lanzado
 * @author Jordan Lopez
 * @version 1.7
 * @since 17/04/2022
*/
public class ExcepcionJordan extends Exception{
    private String mensajeUsuario;
    private String mensajeAdministrador;
    private Integer codigoError;
    private String sentenciaSql;

/**
    * Metodo constructor vacio de la clase:
*/ 
    public ExcepcionJordan() {
    }
/**
    * Metodo constructor de la clase con todos los atributos:
     * @param mensajeUsuario Mensaje que recibira el usuario sobre el error ocurrido
     * @param mensajeAdministrador Mensaje mas tecnico que recibira el administrador sobre el error
     * @param codigoError El codigo de error que capturamos de la BD
     * @param sentenciaSql Sentencia SQL que se ha lanzado
*/ 
    public ExcepcionJordan(String mensajeUsuario, String mensajeAdministrador, Integer codigoError, String sentenciaSql) {
        this.mensajeUsuario = mensajeUsuario;
        this.mensajeAdministrador = mensajeAdministrador;
        this.codigoError = codigoError;
        this.sentenciaSql = sentenciaSql;
    }
/**
    * Metodo GET del atributo MENSAJEUSUARIO utilizado para recoger el mensaje del usuario
     * @return Devuelve un mensaje de tipo String 
*/ 
    public String getMensajeUsuario() {
        return mensajeUsuario;
    }
/**
    * Metodo SET del atributo MENSAJEUSUARIO utilizado para insertar el mensaje del usuario 
     * @param mensajeUsuario Un String con un mensaje para el usuario
*/ 
    public void setMensajeUsuario(String mensajeUsuario) {
        this.mensajeUsuario = mensajeUsuario;
    }
/**
    * Metodo GET del atributo MENSAJEADMINISTRADOR utilizado para recoger el mensaje del administrador
     * @return Devuelve un mensaje de tipo String 
*/ 
    public String getMensajeAdministrador() {
        return mensajeAdministrador;
    }
/**
    * Metodo SET del atributo MENSAJEADMINISTRADOR utilizado para insertar el mensaje del administrador  
    * devuelto de la BD
     * @param mensajeAdministrador Un String con un mensaje para el administrador
*/
    public void setMensajeAdministrador(String mensajeAdministrador) {
        this.mensajeAdministrador = mensajeAdministrador;
    }
/**
    * Metodo GET del atributo CODIGOERROR utilizado para recoger el codigo de error
     * @return Devuelve un codigo de error de tipo Int 
*/ 
    public Integer getCodigoError() {
        return codigoError;
    }
/**
    * Metodo SET del atributo CODIGOERROR utilizado para insertar el codigo de error devuelto de la BD 
     * @param codigoError Un Entero con el codigo de error 
*/
    public void setCodigoError(Integer codigoError) {
        this.codigoError = codigoError;
    }
/**
    * Metodo GET del atributo SENTENCIASQL utilizado para recoger la sentencia SQL que se ha lanzado
     * @return Devuelve una sentencia SQL de tipo String
*/ 
    public String getSentenciaSql() {
        return sentenciaSql;
    }
/**
    * Metodo SET del atributo SENTENCIASQL utilizado para insertar la sentencia SQL que se ha lanzado 
     * @param sentenciaSql Un String con la sentencia SQL 
*/
    public void setSentenciaSql(String sentenciaSql) {
        this.sentenciaSql = sentenciaSql;
    }
/**
    * Metodo toString de la clase ExceptionClase utilizado para mostrar toda la informacion 
     * @return Un string con el mensaje para el usuario, el administrador, codigo de error y la sentencia SQL
*/
    @Override
    public String toString() {
        return "ExcepcionJordan{" + "mensajeUsuario=" + mensajeUsuario + ", mensajeAdministrador=" + mensajeAdministrador + ", codigoError=" + codigoError + ", sentenciaSql=" + sentenciaSql + '}';
    }
    
}
