package aed.tp2;

public class Usuario implements Comparable<Usuario> {
    private int id;
    private int millasDisponibles, millasAcumuladas, millasCanjeadas;

    Usuario exclusivo;
    boolean esExclusivo;

    public Usuario(int id) {
        this.id = id;
        this.millasDisponibles = 0;
        this.millasAcumuladas = 0;
        this.millasCanjeadas = 0;
        this.exclusivo = null;
        this.esExclusivo = false;
    }

    public int compareTo(Usuario otro) {
        return Integer.compare(this.id, otro.id);
    }
}