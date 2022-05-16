package pojos;

import java.io.Serializable;

/**
 * The type Usuario.
 */
//pojo que representa el cliente
public class Usuario implements Serializable {

    private Integer usuarioId;
    private String nombre;
    private String contraseña;
    private String email;
    private String rol;

    /**
     * Instantiates a new Usuario.
     */
    public Usuario() {

    }

    /**
     * Instantiates a new Usuario.
     *
     * @param usuarioId  the usuario id
     * @param nombre     the nombre
     * @param contraseña the contraseña
     * @param email      the email
     * @param rol        the rol
     */
    public Usuario(Integer usuarioId, String nombre, String contraseña, String email, String rol) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.email = email;
        this.rol = rol;
    }

    /**
     * Gets usuario id.
     *
     * @return the usuario id
     */
    public Integer getUsuarioId() {
        return usuarioId;
    }

    /**
     * Sets usuario id.
     *
     * @param usuarioId the usuario id
     */
    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * Gets nombre.
     *
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets nombre.
     *
     * @param nombre the nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Gets rol.
     *
     * @return the rol
     */
    public String getRol() {
        return rol;
    }

    /**
     * Sets rol.
     *
     * @param rol the rol
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Get contraseña string.
     *
     * @return the string
     */
    public String getContraseña() {
        return contraseña;
    }

    /**
     * Set contraseña.
     *
     * @param contraseña the contraseña
     */
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Usuario{" + "usuarioId=" + usuarioId + ", nombre=" + nombre + ", contrase\u00f1a=" + contraseña + ", email=" + email + ", rol=" + rol + '}';
    }

}
