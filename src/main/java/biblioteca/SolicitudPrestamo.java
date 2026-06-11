package biblioteca;

public class SolicitudPrestamo {

    public static final double MULTA_DIARIA = 0.75;
    public static final int MAXIMO_LIBROS = 3;
    public static final int MAX_DIAS_PRESTAMO_NORMAL = 15;
    public static final int MAX_DIAS_PRESTAMO_ESTUDIANTE = 20;
    public static final int MAX_DIAS_PRESTAMO_PROFESOR = 30;

    private final Usuario usuario;
    private final Libro libro;

    private boolean permitido = false;
    private String mensaje = "Sin operaciones";
    private double multa = 0;
    private int plazo = 0;
    private double descuento = 0;
    private int prioridad = 0;

    public SolicitudPrestamo(Usuario usuario, Libro libro) {
        this.usuario = usuario;
        this.libro = libro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Libro getLibro() {
        return libro;
    }

    public boolean isPermitido() {
        return permitido;
    }

    public String getMensaje() {
        return mensaje;
    }

    public double getMulta() {
        return multa;
    }

    public int getPlazo() {
        return plazo;
    }

    public double getDescuento() {
        return descuento;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void evaluar() {
        permitido = true;
        mensaje = "Prestamo aceptado";
        multa = 0;
        plazo = 0;
        descuento = 0;
        prioridad = 0;

        if (usuario.esTipo("profesor")) {
            plazo = MAX_DIAS_PRESTAMO_PROFESOR;
            descuento = 0.20;
            prioridad = 3;
        } else if (usuario.esTipo("estudiante")) {
            plazo = MAX_DIAS_PRESTAMO_ESTUDIANTE;
            descuento = 0.10;
            prioridad = 2;
        } else {
            plazo = MAX_DIAS_PRESTAMO_NORMAL;
            descuento = 0;
            prioridad = 1;
        }

        if (usuario.isProfesorEspecial()) {
            plazo = 7;
            mensaje = "Profesor detectado con plazo especial.";
        }

        if (usuario.getEdad() < 12 && libro.esCategoria("ADULTOS")) {
            permitido = true;
            mensaje = "Menor con libro para adultos. Revisar manualmente.";
        }

        if (usuario.getEdad() >= 12 && libro.esCategoria("INFANTIL")) {
            permitido = false;
            mensaje = "Usuario demasiado mayor para libros infantiles.";
        }

        if (usuario.isSancionado()) {
            permitido = false;
            mensaje = "Usuario sancionado. Prestamo denegado.";
        }

        if (usuario.getLibrosPrestados() > MAXIMO_LIBROS) {
            permitido = false;
            mensaje = "Ha superado el numero maximo de libros prestados.";
        } else if (usuario.getLibrosPrestados() == MAXIMO_LIBROS) {
            permitido = true;
            mensaje = "Prestamo permitido en el limite maximo.";
        }

        if (usuario.getSaldoPendiente() > 0 && usuario.getSaldoPendiente() < 5) {
            permitido = true;
            mensaje = "Prestamo aceptado con deuda pequena pendiente.";
        } else if (usuario.getSaldoPendiente() >= 5) {
            permitido = false;
            mensaje = "Prestamo denegado por deuda.";
        }

        if (usuario.getDiasRetraso() > 0) {
            multa = usuario.getDiasRetraso() * MULTA_DIARIA;
        }

        if (usuario.getDiasRetraso() > 10) {
            descuento = descuento + 0.15;
        }

        if (usuario.getRenovaciones() > 2 && permitido) {
            mensaje = "Prestamo aceptado aunque supera renovaciones.";
        }

        if (libro.getPaginas() > 500 && usuario.esTipo("normal")) {
            plazo = plazo - 5;
        }

        if (libro.getPaginas() < 50) {
            plazo = plazo + 10;
        }

        if (libro.esCategoria("JUVENIL") && usuario.getEdad() < 10) {
            permitido = false;
            mensaje = "Categoria juvenil no recomendada para esa edad.";
        }
    }

    public String generarResumen() {
        return "Usuario: " + usuario.getNombre()
                + " | Edad: " + usuario.getEdad()
                + " | Tipo: " + usuario.getTipo()
                + " | Categoria: " + libro.getCategoria()
                + " | Paginas: " + libro.getPaginas()
                + " | Libros actuales: " + usuario.getLibrosPrestados()
                + " | Renovaciones: " + usuario.getRenovaciones()
                + " | Dias permitidos: " + plazo
                + " | Prioridad: " + prioridad
                + " | Multa: " + multa
                + " | Descuento: " + descuento
                + " | Codigo: " + libro.getCodigo()
                + " | Estado: " + mensaje;
    }
}
