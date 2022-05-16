package es.jordan.sistemaalarma;

/**
 * Item alarma.
 */
public class ItemAlarma {

    long id;
    String rutaImagen;
    String nombre;
    String tipo;


    /**
     * Instantiates a new Item alarma.
     */
    public ItemAlarma() {
        this.nombre = "";
        this.tipo = "";
        this.rutaImagen = "";
    }

    /**
     * Instantiates a new Item alarma.
     *
     * @param id     el/la id
     * @param nombre el/la nombre
     * @param tipo   el/la tipo
     */
    public ItemAlarma(long id, String nombre, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.rutaImagen = "";
    }

    /**
     * Instantiates a new Item alarma.
     *
     * @param id         el/la id
     * @param nombre     el/la nombre
     * @param tipo       el/la tipo
     * @param rutaImagen el/la ruta imagen
     */
    public ItemAlarma(long id, String nombre, String tipo, String rutaImagen) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.rutaImagen = rutaImagen;
    }

    /**
     * Gets id.
     *
     * @return el/la id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id el/la id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets ruta imagen.
     *
     * @return el/la ruta imagen
     */
    public String getRutaImagen() {
        return rutaImagen;
    }

    /**
     * Sets ruta imagen.
     *
     * @param rutaImagen el/la ruta imagen
     */
    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    /**
     * Gets nombre.
     *
     * @return el/la nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets nombre.
     *
     * @param nombre el/la nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Gets tipo.
     *
     * @return el/la tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Sets tipo.
     *
     * @param tipo el/la tipo
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
