package pojos;

import java.io.Serializable;


/**
 * el/la type Incidencia.
 */
public class Incidencia implements Serializable {

    private int incidenciaId;
    private Raspberry raspberryId;
    private String hora;

    /**
     * constructor vacio
     */
    public Incidencia() {
    }

    /**
     * Instantiates a new Incidencia.
     *
     * @param incidenciaId el/la incidencia id
     * @param raspberryId  el/la raspberry id
     * @param hora         el/la hora
     */
    public Incidencia(int incidenciaId, Raspberry raspberryId, String hora) {
        this.incidenciaId = incidenciaId;
        this.raspberryId = raspberryId;
        this.hora = hora;
    }

    /**
     * Gets incidencia id.
     *
     * @return el/la incidencia id
     */
    public int getIncidenciaId() {
        return incidenciaId;
    }

    /**
     * Sets incidencia id.
     *
     * @param incidenciaId el/la incidencia id
     */
    public void setIncidenciaId(int incidenciaId) {
        this.incidenciaId = incidenciaId;
    }

    /**
     * Gets raspberry id.
     *
     * @return el/la raspberry id
     */
    public Raspberry getRaspberryId() {
        return raspberryId;
    }

    /**
     * Sets raspberry id.
     *
     * @param raspberryId el/la raspberry id
     */
    public void setRaspberryId(Raspberry raspberryId) {
        this.raspberryId = raspberryId;
    }

    /**
     * Gets hora.
     *
     * @return el/la hora
     */
    public String getHora() {
        return hora;
    }

    /**
     * Sets hora.
     *
     * @param hora el/la hora
     */
    public void setHora(String hora) {
        this.hora = hora;
    }

    /**
     * Cadena
     */
    @Override
    public String toString() {
        return "Notificacion{" + "incidenciaId=" + incidenciaId + ", raspberryId=" + raspberryId + ", hora=" + hora + '}';
    }


}
