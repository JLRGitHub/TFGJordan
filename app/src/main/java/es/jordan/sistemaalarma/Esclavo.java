package es.jordan.sistemaalarma;

import java.io.Serializable;

/**
 * Esclavo.
 */
public class Esclavo implements Serializable {

    private int raspberryId;
    private int usuarioId;

    /**
     * Instantiates a new Esclavo.
     */
    public Esclavo() {
    }

    /**
     * Instantiates a new Esclavo.
     *
     * @param raspberryId the raspberry id
     * @param usuarioId   the usuario id
     */
    public Esclavo(int raspberryId, int usuarioId) {
        this.raspberryId = raspberryId;
        this.usuarioId = usuarioId;
    }

    /**
     * Gets raspberry id.
     *
     * @return the raspberry id
     */
    public int getRaspberryId() {
        return raspberryId;
    }

    /**
     * Sets raspberry id.
     *
     * @param raspberryId the raspberry id
     */
    public void setRaspberryId(int raspberryId) {
        this.raspberryId = raspberryId;
    }

    /**
     * Gets usuario id.
     *
     * @return the usuario id
     */
    public int getUsuarioId() {
        return usuarioId;
    }

    /**
     * Sets usuario id.
     *
     * @param usuarioId the usuario id
     */
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * metodo toString de la clase esclavo
     */
    @Override
    public String toString() {
        return "Esclavo{" +
                "raspberryId=" + raspberryId +
                ", usuarioId=" + usuarioId +
                '}';
    }
}
