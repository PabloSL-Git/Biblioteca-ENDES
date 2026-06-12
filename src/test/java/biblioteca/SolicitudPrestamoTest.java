package biblioteca;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class SolicitudPrestamoTest {

    // ----- Pruebas validas -----

    @Test
    public void testPrestamoEstudianteConDeudaPequenaPermitido() {
        boolean resultado = false;

        Usuario usuario = new Usuario();
        usuario.setNombre("Pablo");
        usuario.setEdad(17);
        usuario.setTipo("estudiante");
        usuario.setLibrosPrestados(1);
        usuario.setSaldoPendiente(2.5);

        Libro libro = new Libro();
        libro.setCodigo("LIB1234");
        libro.setCategoria("JUVENIL");
        libro.setPaginas(200);

        SolicitudPrestamo solicitud = new SolicitudPrestamo(usuario, libro);
        solicitud.evaluar();

        if (solicitud.isPermitido() && solicitud.getMensaje().equals("Prestamo aceptado con deuda pequena pendiente.")) {
            resultado = true;
        }

        assertTrue(resultado, "Estudiante con deuda de 2.5 debe tener el prestamo permitido con el mensaje de deuda pequena.");
    }

    @Test
    public void testCodigoLibroConPrefijoValido() {
        boolean resultado = false;

        Libro libro = new Libro();
        libro.setCodigo("LIB1234");

        if (libro.tienePrefijoValido()) {
            resultado = true;
        }

        assertTrue(resultado, "El codigo LIB1234 empieza por LIB, debe ser valido.");
    }

    @Test
    public void testCalcularPlazoProfesorNormal() {
        boolean resultado = false;

        Usuario usuario = new Usuario();
        usuario.setTipo("profesor");
        usuario.setProfesorEspecial(false);

        Libro libro = new Libro();
        libro.setPaginas(200);

        SolicitudPrestamo solicitud = new SolicitudPrestamo(usuario, libro);
        solicitud.evaluar();

        if (solicitud.getPlazo() == SolicitudPrestamo.MAX_DIAS_PRESTAMO_PROFESOR) {
            resultado = true;
        }

        assertTrue(resultado, "Un profesor sin caso especial y con un libro de 200 paginas debe tener el plazo normal de profesor (30 dias).");
    }

    @Test
    public void testCalcularMultaConRetraso() {
        boolean resultado = false;

        Usuario usuario = new Usuario();
        usuario.setDiasRetraso(12);

        Libro libro = new Libro();

        SolicitudPrestamo solicitud = new SolicitudPrestamo(usuario, libro);
        solicitud.evaluar();

        if (solicitud.getMulta() == 9.0) {
            resultado = true;
        }

        assertTrue(resultado, "Con 12 dias de retraso la multa debe ser 12 * 0.75 = 9.0.");
    }

    // ----- Pruebas invalidas -----

    @Test
    public void testPrestamoSancionadoDenegado() {
        boolean resultado = false;

        Usuario usuario = new Usuario();
        usuario.setSancionado(true);

        Libro libro = new Libro();

        SolicitudPrestamo solicitud = new SolicitudPrestamo(usuario, libro);
        solicitud.evaluar();

        if (!solicitud.isPermitido() && solicitud.getMensaje().equals("Usuario sancionado. Prestamo denegado.")) {
            resultado = true;
        }

        assertTrue(resultado, "Un usuario sancionado debe tener el prestamo denegado con el mensaje correspondiente.");
    }

    @Test
    public void testCodigoLibroSinPrefijoValido() {
        boolean resultado = false;

        Libro libro = new Libro();
        libro.setCodigo("ABC1234");

        if (libro.tienePrefijoValido()) {
            resultado = true;
        }

        assertFalse(resultado, "El codigo ABC1234 no empieza por LIB, debe ser invalido.");
    }

    @Test
    public void testValidarDatosNombreVacio() {
        boolean resultado = false;

        Usuario usuario = new Usuario();
        usuario.setNombre("");

        Libro libro = new Libro();
        libro.setCodigo("LIB1234");

        List<String> incidencias = ValidadorDatos.validar(usuario, libro);

        if (incidencias.contains("El nombre esta vacio.")) {
            resultado = true;
        }

        assertTrue(resultado, "Un nombre vacio debe generar la incidencia 'El nombre esta vacio.'");
    }

    @Test
    public void testPrestamoSaldoPendienteAltoDenegado() {
        boolean resultado = false;

        Usuario usuario = new Usuario();
        usuario.setSaldoPendiente(6);

        Libro libro = new Libro();

        SolicitudPrestamo solicitud = new SolicitudPrestamo(usuario, libro);
        solicitud.evaluar();

        if (!solicitud.isPermitido() && solicitud.getMensaje().equals("Prestamo denegado por deuda.")) {
            resultado = true;
        }

        assertTrue(resultado, "Con un saldo pendiente de 6 (>= 5) el prestamo debe quedar denegado por deuda con el mensaje correspondiente.");
    }

    // ----- Pruebas de valor limite -----

    @Test
    public void testLibrosPrestadosEnLimiteMaximo() {
        boolean resultado = false;

        Usuario usuario = new Usuario();
        usuario.setLibrosPrestados(SolicitudPrestamo.MAXIMO_LIBROS);

        Libro libro = new Libro();

        SolicitudPrestamo solicitud = new SolicitudPrestamo(usuario, libro);
        solicitud.evaluar();

        if (solicitud.isPermitido() && solicitud.getMensaje().equals("Prestamo permitido en el limite maximo.")) {
            resultado = true;
        }

        assertTrue(resultado, "Con librosPrestados igual al maximo (3) el prestamo debe quedar permitido en el limite.");
    }

    @Test
    public void testEdadLimiteNoGeneraIncidencia() {
        boolean resultado = false;

        Usuario usuario = new Usuario();
        usuario.setNombre("Pablo");
        usuario.setEdad(120);

        Libro libro = new Libro();
        libro.setCodigo("LIB1234");

        List<String> incidencias = ValidadorDatos.validar(usuario, libro);

        if (incidencias.contains("Edad poco realista.")) {
            resultado = true;
        }

        assertFalse(resultado, "Una edad de 120 (limite) no debe generar la incidencia 'Edad poco realista.'");
    }

    // ----- Pruebas de regresion: renovaciones > 2 no debe revertir una denegacion previa -----

    @Test
    public void testRegresionRenovacionesNoRevierteSancion() {
        boolean resultado = false;

        Usuario usuario = new Usuario();
        usuario.setSancionado(true);
        usuario.setRenovaciones(5);

        Libro libro = new Libro();

        SolicitudPrestamo solicitud = new SolicitudPrestamo(usuario, libro);
        solicitud.evaluar();

        if (!solicitud.isPermitido() && solicitud.getMensaje().equals("Usuario sancionado. Prestamo denegado.")) {
            resultado = true;
        }

        assertTrue(resultado, "Un usuario sancionado con renovaciones > 2 debe seguir denegado (regresion del bug de incidencia-2).");
    }

    @Test
    public void testRegresionRenovacionesNoRevierteDeuda() {
        boolean resultado = false;

        Usuario usuario = new Usuario();
        usuario.setSaldoPendiente(10);
        usuario.setRenovaciones(5);

        Libro libro = new Libro();

        SolicitudPrestamo solicitud = new SolicitudPrestamo(usuario, libro);
        solicitud.evaluar();

        if (!solicitud.isPermitido() && solicitud.getMensaje().equals("Prestamo denegado por deuda.")) {
            resultado = true;
        }

        assertTrue(resultado, "Un usuario con deuda >= 5 y renovaciones > 2 debe seguir denegado por deuda (regresion del bug de incidencia-2).");
    }
}
