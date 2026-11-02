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
        nuevo.setMillasDisponibles(500);
        Usuario referidor = buscarUsuario(ref);
        referidor.setMillasDisponibles(500);
        this.usuarios.insertar(nuevo);
    }

    private Usuario buscarUsuario(int id){
        Usuario u = new Usuario(id);
        return usuarios.obtener(u);
    }

    public void acumularMillas(int id, double distancia, String nombrePromo) {
        Promocion promo = promociones.obtener(nombrePromo);
        Usuario u = buscarUsuario(id);
        if (promo.getTope() == 0) {
            u.setMillasDisponibles(distancia * promo.getFactor());
        } else {
            //logica de promocionConTope
        }
        u.setMillasDisponibles(distancia * promo.getFactor());
        if (u.getMillasDisponibles() > this.masAcumulador.getMillasDisponibles()) {
            this.masAcumulador = u;
        }
    }

    public void canjearMillas(int id, double cantidad) {
        Usuario u = buscarUsuario(id);
        u.setMillasDisponibles(-(cantidad));
        u.setMillasCanjeadas(cantidad);
        if (u.getMillasCanjeadas() > this.masCanjeador.getMillasCanjeadas()) {
            this.masCanjeador = u;
        }
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        Usuario uOrigen = buscarUsuario(origen);
        Usuario uDestino = buscarUsuario(destino);
        uOrigen.setMillasDisponibles(-cantidad);
        uOrigen.setMillasCanjeadas(cantidad);
        if (uOrigen.getMillasCanjeadas() > this.masCanjeador.getMillasCanjeadas()) {
            this.masCanjeador = uOrigen;
        }
        uDestino.setMillasDisponibles(cantidad);
    }

    public double consultarSaldo(int id) {
        Usuario u = new Usuario(id);
        return u.getMillasDisponibles;
    }

    public String consultarCategoria(int id) {
       Usuario u = new Usuario(id);
       millas u.getMillasDisponibles;

       if (millas < 10000) return "Bronce";
        if (millas < 50000) return "Plata";
         if (millas < 1000000) return "Oro";
        return "Platino";
    }

    public int[] rankingMillas(int n) {
        int[] ranking = new int[n];
        for (int i = 0; i < n; i++) {
            Usuario u = rankingMillas.eliminarMax();
            ranking[i] = u.getId();
        }
        return ranking;
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
