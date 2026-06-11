package biblioteca;

import java.util.ArrayList;
import java.util.List;

public class ValidadorDatos {

    public static List<String> validar(Usuario usuario, Libro libro) {
        List<String> incidencias = new ArrayList<>();

        if (usuario.getNombre().isEmpty()) {
            incidencias.add("El nombre esta vacio.");
        }

        if (usuario.getNombre().length() < 3) {
            incidencias.add("Nombre demasiado corto.");
        }

        if (usuario.getEdad() < 0) {
            incidencias.add("Edad negativa detectada.");
        }

        if (usuario.getEdad() > 120) {
            incidencias.add("Edad poco realista.");
        }

        if (libro.getCodigo().length() < 5) {
            incidencias.add("Codigo de libro demasiado corto.");
        }

        if (!libro.tienePrefijoValido()) {
            incidencias.add("El codigo no empieza por LIB.");
        }

        if (libro.getPaginas() < 0) {
            incidencias.add("Numero de paginas negativo.");
        }

        if (usuario.getDiasRetraso() > 30) {
            incidencias.add("Retraso excesivo detectado para revisar manualmente.");
        }

        if (usuario.getRenovaciones() < 0) {
            incidencias.add("Renovaciones negativas.");
        }

        return incidencias;
    }
}
