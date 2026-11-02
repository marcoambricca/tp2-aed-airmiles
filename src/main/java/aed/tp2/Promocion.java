package aed.tp2;

public class Promocion{
    private String nombre;
    private double factor;
    private double tope;

    public Promocion(String nombre, double factor, double tope) {
        this.nombre = nombre;
        this.factor = factor;
        this.tope = tope;
    }

    public String getNombre() {
        return nombre;
    }

    public double getFactor() {
        return factor;
    }

    public double getTope() {
        return tope;
    }

    public void setTope(double tope) {
        this.tope = tope;
    }

    public int compareTo(Promocion otra) {
        return this.nombre.compareTo(otra.nombre);
    }
}