package biblioteca;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * RAMA: incidencia-1-bugs-en-usuario
 *
 * 1. Comparar los String con == sustituir con .isEmpty()
 *
 * 2. El if del sancionado usaba = en vez de ==
 *
 * 3. El bucle de totalCaracteres contaba una vez de mas por el <=, se cambia a <
 *
 * 4. Cambia equals por equalsIgnoreCase para evitar errores
 *
 * 6. Si el codigo tenia menos de 3 letras, sale con excepcion añade if.
 */

/*
 * RAMA: incidencia-2-logica-y-redundancias
 *
 * 1. Se elimina cálculo redundante de totalCaracteres, uso directo de nombreUsuario.length()
 *
 * 2. Se corrige redundancia en validación de prefijo de código, se mantiene startsWith("LIB") como validación principal
 *
 * 3. El caso especial de profesor era poco intuitivo, añadido otra opcion
 *
 * 4. El if de librosPrestados era poco intuitivo, lo e unificado.
 *
 * 5. Algunos script usan + en vez de * erroneamente
 *
 * 6. El if de saldoPendiente era poco intuitivo, lo e unificado al igual que el de librosPrestados.
 *
 * 7. Se elimina la variable totalCaracteres (redundante, era nombreUsuario.length()) y se usa directamente.
 *
 * 8. Se quita la comprobacion redundante codigoLibro.length() >= 3, ya la garantiza startsWith("LIB").
 *
 * 9. Bug de logica: el if de renovaciones > 2 forzaba prestamoPermitido = true al final,
 *    revirtiendo negaciones previas. Ahora solo
 *    actualiza el mensaje si el prestamo seguia permitido.
 */

/*
 * RAMA: incidencia-3-separar-clase-dios
 *
 * 1. Se reparte el estado (nombreUsuario, edadUsuario, librosPrestados, etc.) entre las
 *    nuevas clases Usuario y Libro, con campos privados y getters/setters.
 *
 * 2. La logica de evaluacion de la solicitud (plazo, descuento, prioridad, multa,
 *    permitido/mensaje) se mueve a la nueva clase SolicitudPrestamo.
 *
 * 3. Las comprobaciones de incidencias se mueven a la nueva clase ValidadorDatos.
 *
 * 4. BibliotecaApp queda solo con el menu, la lectura por Scanner y la impresion
 *    de resultados, delegando en las clases anteriores.
 */

public class BibliotecaApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> incidencias = new ArrayList<>();
        SolicitudPrestamo ultimaSolicitud = new SolicitudPrestamo(new Usuario(), new Libro());
        boolean seguir = true;

        System.out.println("========================================");
        System.out.println("  SISTEMA DE PRESTAMOS - LECTURA VIVA");
        System.out.println("========================================");

        while (seguir) {
            mostrarMenu();

            int opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    Usuario usuario = leerUsuario(sc);
                    Libro libro = leerLibro(sc);

                    incidencias.addAll(ValidadorDatos.validar(usuario, libro));

                    ultimaSolicitud = new SolicitudPrestamo(usuario, libro);
                    ultimaSolicitud.evaluar();

                    mostrarResultadoSolicitud(ultimaSolicitud);
                    break;

                case 2:
                    mostrarUltimoResumen(ultimaSolicitud);
                    break;

                case 3:
                    mostrarIncidencias(incidencias);
                    break;

                case 4:
                    seguir = false;
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }
        }

        System.out.println("Programa finalizado.");
        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("1. Registrar solicitud de prestamo");
        System.out.println("2. Mostrar ultimo resumen");
        System.out.println("3. Mostrar incidencias detectadas en ejecucion");
        System.out.println("4. Salir");
        System.out.print("Selecciona una opcion: ");
    }

    private static Usuario leerUsuario(Scanner sc) {
        Usuario usuario = new Usuario();

        System.out.print("Nombre de usuario: ");
        usuario.setNombre(sc.nextLine());

        System.out.print("Edad del usuario: ");
        usuario.setEdad(sc.nextInt());

        System.out.print("Numero de libros ya prestados: ");
        usuario.setLibrosPrestados(sc.nextInt());

        System.out.print("Dias de retraso acumulados: ");
        usuario.setDiasRetraso(sc.nextInt());

        System.out.print("Numero de renovaciones del libro: ");
        usuario.setRenovaciones(sc.nextInt());
        sc.nextLine();

        System.out.print("Esta sancionado? (true/false): ");
        usuario.setSancionado(sc.nextBoolean());
        sc.nextLine();

        System.out.print("Tipo de usuario (normal/estudiante/profesor): ");
        usuario.setTipo(sc.nextLine());

        System.out.print("¿Caso de profesor especial? ");
        usuario.setProfesorEspecial(sc.nextBoolean());
        sc.nextLine();

        System.out.print("Saldo pendiente: ");
        usuario.setSaldoPendiente(sc.nextDouble());
        sc.nextLine();

        return usuario;
    }

    private static Libro leerLibro(Scanner sc) {
        Libro libro = new Libro();

        System.out.print("Codigo del libro: ");
        libro.setCodigo(sc.nextLine());

        System.out.print("Categoria del libro (INFANTIL/JUVENIL/ADULTOS): ");
        libro.setCategoria(sc.nextLine());

        System.out.print("Numero de paginas del libro: ");
        libro.setPaginas(sc.nextInt());
        sc.nextLine();

        return libro;
    }

    private static void mostrarResultadoSolicitud(SolicitudPrestamo solicitud) {
        Usuario usuario = solicitud.getUsuario();
        Libro libro = solicitud.getLibro();

        System.out.println();
        System.out.println("----- RESULTADO DE LA SOLICITUD -----");
        System.out.println(solicitud.generarResumen());

        if (solicitud.isPermitido()) {
            System.out.println("Operacion finalizada correctamente.");
        } else {
            System.out.println("Operacion rechazada.");
        }

        if (usuario.nombreLargo()) {
            System.out.println("Nombre largo detectado.");
        }

        if (libro.tienePrefijoValido()) {
            System.out.println("Codigo con prefijo correcto.");
        }

        if (libro.esCategoria("ADULTOS") && usuario.getEdad() < 18) {
            System.out.println("Aviso: contenido para adultos.");
        }
    }

    private static void mostrarUltimoResumen(SolicitudPrestamo solicitud) {
        Usuario usuario = solicitud.getUsuario();
        Libro libro = solicitud.getLibro();

        System.out.println();
        System.out.println("----- ULTIMO RESUMEN -----");
        System.out.println("Usuario: " + usuario.getNombre());
        System.out.println("Edad: " + usuario.getEdad());
        System.out.println("Tipo de usuario: " + usuario.getTipo().toUpperCase());
        System.out.println("Libros prestados: " + usuario.getLibrosPrestados());
        System.out.println("Dias de retraso: " + usuario.getDiasRetraso());
        System.out.println("Renovaciones: " + usuario.getRenovaciones());
        System.out.println("Sancionado: " + usuario.isSancionado());
        System.out.println("Saldo pendiente: " + usuario.getSaldoPendiente());
        System.out.println("Codigo del libro: " + libro.getCodigo());
        System.out.println("Categoria: " + libro.getCategoria());
        System.out.println("Paginas: " + libro.getPaginas());
        System.out.println("Plazo asignado: " + solicitud.getPlazo());
        System.out.println("Multa aplicada: " + solicitud.getMulta());
        System.out.println("Estado final: " + solicitud.getMensaje());
    }

    private static void mostrarIncidencias(List<String> incidencias) {
        System.out.println();
        System.out.println("----- INCIDENCIAS EN MEMORIA -----");

        for (int i = 0; i < incidencias.size(); i++) {
            System.out.println((i + 1) + ". " + incidencias.get(i));
        }
        if (incidencias.isEmpty()) {
            System.out.println("No hay incidencias registradas.");
        }
    }
}
