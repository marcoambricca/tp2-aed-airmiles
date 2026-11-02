package aed.tp2;

public class Usuario implements Comparable<Usuario> {
    private int id;
    private double millasDisponibles, millasAcumuladas, millasCanjeadas;

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

    public void setMillasDisponibles(double millas) {
        this.millasDisponibles += millas;
    }

    public void setMillasAcumuladas(double millas) {
        this.millasAcumuladas += millas;
    }

    public void setMillasCanjeadas(double millas) {
        this.millasCanjeadas += millas;
    }

    public int compareTo(Usuario otro) {
        return Integer.compare(this.id, otro.id);
    }
}