package aed.tp2;

public class AirMiles implements SistemaAirMiles {
    /*COMPLETAR*/
    private ABB<Usuario> usuarios;
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
        Usuario nuevo = new Usuario(id);
        this.usuarios.insertar(nuevo);
    }

    public void registrarUsuarioConReferidor(int id, int ref) {
        Usuario nuevo = new Usuario(id);
        Usuario referidor = buscarUsuario(ref);
        if (this.usuarios.minimo().compareTo(referidor) <= 0 && this.usuarios.maximo().compareTo(referidor) >= 0) {
            nuevo.exclusivo = referidor;
            nuevo.esExclusivo = true;
        }
        this.usuarios.insertar(nuevo);
    }

    private Usuario buscarUsuario(int id){
        Usuario u = new Usuario(id);
        return usuarios.obtener(u);
    }

    public void acumularMillas(int id, double distancia, String nombrePromo) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public void canjearMillas(int id, double cantidad) {
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        throw new UnsupportedOperationException("Sin implementar");
    }

    public double consultarSaldo(int id) {
        Usuario u = new Usuario(id);
        return u.getMillasDisponibles;
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
}
