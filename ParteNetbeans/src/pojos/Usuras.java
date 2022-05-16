
package pojos;


import java.io.Serializable;

/**
 * el/la type Usuras.
 */
public class Usuras implements Serializable {

    private int usurasId;
    private Usuario usuarioId;
    private Raspberry raspberryId;


    /**
     * Constructor vacio de usuras
     */
    public Usuras() {
    }

    /**
     * Constructor no vacio de usuras
     *
     * @param usurasId    el/la usuras id
     * @param usuarioId   el/la usuario id
     * @param raspberryId el/la raspberry id
     */
    public Usuras(int usurasId, Usuario usuarioId, Raspberry raspberryId) {
        this.usurasId = usurasId;
        this.usuarioId = usuarioId;
        this.raspberryId = raspberryId;
    }


    /**
     * Gets usuras id.
     *
     * @return el/la usuras id
     */
    public int getUsurasId() {
        return usurasId;
    }

    /**
     * Sets usuras id.
     *
     * @param usurasId el/la usuras id
     */
    public void setUsurasId(int usurasId) {
        this.usurasId = usurasId;
    }

    /**
     * Gets usuario id.
     *
     * @return el/la usuario id
     */
    public Usuario getUsuarioId() {
        return usuarioId;
    }

    /**
     * Sets usuario id.
     *
     * @param usuarioId el/la usuario id
     */
    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
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
     * Cadena
     */
    @Override
    public String toString() {
        return "Usuras{" + "usurasId=" + usurasId + ", usuarioId=" + usuarioId + ", raspberryId=" + raspberryId + '}';
    }


}
