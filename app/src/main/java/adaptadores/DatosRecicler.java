package adaptadores;

public class DatosRecicler {
    String nombre;
    String informacion;
    int foto;
    public DatosRecicler(String nombre, String informacion, int foto) {
        this.nombre = nombre;
        this.informacion = informacion;
        this.foto = foto;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}
