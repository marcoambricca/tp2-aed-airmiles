package aed.tp2;

public class AirMiles implements SistemaAirMiles {
    /*COMPLETAR*/
    private ABB<Integer> usuarios;
    private MaxHeap<Usuario> rankingMillas;
    private Usuario masCanjeador;
    private Usuario masAcumulador;
    private Tries<String, Promocion> promociones;

    public AirMiles() {
        this.usuarios = new ABB<>();
        this.rankingMillas = new MaxHeap<>();
        this.masCanjeador = null;
        this.masAcumulador = null;
        this.promociones = new Tries<>();
    }

    public void registrarUsuario(int id) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void registrarUsuarioConReferidor(int id, int ref) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void acumularMillas(int id, double distancia, String nombrePromo) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void canjearMillas(int id, double cantidad) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public double consultarSaldo(int id) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public String consultarCategoria(int id) {
       throw new UnsupportedOperationException("Sin implementar");
    }

    public int[] rankingMillas(int n) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void iniciarPromocion(String nombre, double factor) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void iniciarPromocionConTope(String nombre, double factor, double tope) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void finalizarPromocion(String nombre) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public int elMasAcumulador() {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public int elMasCanjeador() {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public int[][] paresTransferidoresExclusivos() {
        throw new UnsupportedOperationException("Sin implementar");
    }

    private class Usuario{
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
    }

    private class Promocion{
        private String nombre;
        private double factor;
        private double tope;

        public Promocion(String nombre, double factor, double tope) {
            this.nombre = nombre;
            this.factor = factor;
            this.tope = tope;
        }
    }
}
