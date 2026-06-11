package biblioteca;

public class Usuario {

    private String nombre = "";
    private int edad = 0;
    private int librosPrestados = 0;
    private int diasRetraso = 0;
    private boolean sancionado = false;
    private String tipo = "normal";
    private double saldoPendiente = 0;
    private int renovaciones = 0;
    private boolean profesorEspecial = false;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getLibrosPrestados() {
        return librosPrestados;
    }

    public void setLibrosPrestados(int librosPrestados) {
        this.librosPrestados = librosPrestados;
    }

    public int getDiasRetraso() {
        return diasRetraso;
    }

    public void setDiasRetraso(int diasRetraso) {
        this.diasRetraso = diasRetraso;
    }

    public boolean isSancionado() {
        return sancionado;
    }

    public void setSancionado(boolean sancionado) {
        this.sancionado = sancionado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getSaldoPendiente() {
        return saldoPendiente;
    }

    public void setSaldoPendiente(double saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }

    public int getRenovaciones() {
        return renovaciones;
    }

    public void setRenovaciones(int renovaciones) {
        this.renovaciones = renovaciones;
    }

    public boolean isProfesorEspecial() {
        return profesorEspecial;
    }

    public void setProfesorEspecial(boolean profesorEspecial) {
        this.profesorEspecial = profesorEspecial;
    }

    public boolean esTipo(String tipoComparado) {
        return tipo.equalsIgnoreCase(tipoComparado);
    }

    public boolean nombreLargo() {
        return nombre.length() > 20;
    }
}
