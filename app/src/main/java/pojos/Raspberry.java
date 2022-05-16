/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open el/la template in el/la editor.
 */
package pojos;

import java.io.Serializable;

/**
 * el/la type Raspberry.
 *
 * @author jordan
 */
public class Raspberry implements Serializable {

    private Integer raspberryId;
    private String modelo;
    private String memoria;
    private String direccion;

    /**
     * constructor vacio de raspberry
     */
    public Raspberry() {
    }

    /**
     * constructor no vacio de raspberry.
     *
     * @param raspberryId el/la raspberry id
     * @param modelo      el/la modelo
     * @param memoria     el/la memoria
     * @param direccion   el/la direccion
     */
    public Raspberry(Integer raspberryId, String modelo, String memoria, String direccion) {
        this.raspberryId = raspberryId;
        this.modelo = modelo;
        this.memoria = memoria;
        this.direccion = direccion;
    }

    /**
     * Gets direccion.
     *
     * @return el/la direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Sets direccion.
     *
     * @param direccion el/la direccion
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }


    /**
     * Gets raspberry id.
     *
     * @return el/la raspberry id
     */
    public Integer getRaspberryId() {
        return raspberryId;
    }

    /**
     * Sets raspberry id.
     *
     * @param raspberryId el/la raspberry id
     */
    public void setRaspberryId(Integer raspberryId) {
        this.raspberryId = raspberryId;
    }

    /**
     * Gets modelo.
     *
     * @return el/la modelo
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Sets modelo.
     *
     * @param modelo el/la modelo
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * Gets memoria.
     *
     * @return el/la memoria
     */
    public String getMemoria() {
        return memoria;
    }

    /**
     * Sets memoria.
     *
     * @param memoria el/la memoria
     */
    public void setMemoria(String memoria) {
        this.memoria = memoria;
    }

    /**
     * Cadena
     */
    @Override
    public String toString() {
        return raspberryId + "," + modelo + "," + direccion;
    }

    /**
     * To string 2 string.
     *
     * @return el/la string
     */
    public String toString2() {
        return "Raspberry{" + "raspberryId=" + raspberryId + ", modelo=" + modelo + ", memoria=" + memoria + ", direccion=" + direccion + '}';

    }


}
