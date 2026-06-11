package biblioteca;

public class Libro {

    private String codigo = "";
    private String categoria = "";
    private int paginas = 0;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getPaginas() {
        return paginas;
    }

    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    public boolean tienePrefijoValido() {
        return codigo.startsWith("LIB");
    }

    public boolean esCategoria(String categoriaComparada) {
        return categoria.equalsIgnoreCase(categoriaComparada);
    }
}
