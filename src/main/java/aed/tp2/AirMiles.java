package aed.tp2;

import java.util.ArrayList;
import java.util.Comparator;

public class AirMiles implements SistemaAirMiles {
    /* COMPLETAR */
    private ABB<Usuario> usuarios;
    private MaxHeap<Usuario> rankingMillas;
    private Usuario masCanjeador;
    private Usuario masAcumulador;
    private Trie<Promocion> promociones;

    public AirMiles() {
        this.usuarios = new ABB<>();
        this.rankingMillas = new MaxHeap<>(new PorMillasDisponibles());
        this.masCanjeador = null;
        this.masAcumulador = null;
        this.promociones = new Trie<>(); // hay que agregar la promocion NoPromo, fact 1.
        promociones.definir("NoPromo", new Promocion("NoPromo", 1, -1));
    }

    // esto va aca?
    private class PorMillasDisponibles implements Comparator<Usuario> {
        @Override
        public int compare(Usuario a, Usuario b) {
            return Double.compare(a.getMillasDisponibles(), b.getMillasDisponibles());
        }
    }

    public void registrarUsuario(int id) {
        Usuario nuevo = new Usuario(id);
        this.usuarios.insertar(nuevo);
        this.rankingMillas.encolar(nuevo);
        if (this.masAcumulador == null) {
            this.masAcumulador = nuevo;
        }
        if (this.masCanjeador == null) {
            this.masCanjeador = nuevo;
        }
    }

    public void registrarUsuarioConReferidor(int id, int ref) {
        Usuario nuevo = new Usuario(id);
        nuevo.setMillasDisponibles(500);
        Usuario referidor = buscarUsuario(ref);
        referidor.setMillasDisponibles(500);
        this.usuarios.insertar(nuevo);
        this.rankingMillas.encolar(nuevo);
    }

    private Usuario buscarUsuario(int id) { // PREGUNTAR SI ES VALIDO
        Usuario u = new Usuario(id);
        return usuarios.obtener(u);
    }

    public void acumularMillas(int id, double distancia, String nombre) {
        Promocion promo = promociones.obtener(nombre);
        Usuario u = buscarUsuario(id);
        // si tope es -1 es que es promocion sin tope
        if (promo.getTope() == -1) {
            u.setMillasDisponibles(distancia * promo.getFactor() * categoriaFactor(id));
        }
        // si tope no es -1 es que es promocion con tope
        else {
            // si la cantidad de millas que se acumularian es mayor al tope, se acumula la
            // distancia sin factor
            if (distancia * promo.getFactor() > promo.getTope()) {
                u.setMillasDisponibles(distancia * categoriaFactor(id));
                // si la cantidad de millas que se acumularian es menor o igual al tope, se
                // acumula la distancia con factor y se descuenta del tope
            } else if (distancia * promo.getFactor() <= promo.getTope()) {
                u.setMillasDisponibles(distancia * promo.getFactor() * categoriaFactor(id));
                promo.setTope(promo.getTope() - distancia * promo.getFactor() * categoriaFactor(id));
            }

            // veo luego de acumular si no queda tope y en ese caso borro la promocion
            if (promo.getTope() == 0) {
                this.finalizarPromocion(nombre);
            }
        }
        // logica mas acumulador
        if (this.masAcumulador == null || u.getMillasDisponibles() > this.masAcumulador.getMillasDisponibles()) {
            this.masAcumulador = u;
        }
        // esto es asi o deberia haber una funcion de rebalanceo universal que uso
        // cuando actualizo algun elemento?
        rankingMillas.actualizar();
    }

    public void canjearMillas(int id, double cantidad) {
        Usuario u = buscarUsuario(id);
        u.setMillasDisponibles(-(cantidad));
        u.setMillasCanjeadas(cantidad);
        if (this.masCanjeador == null || u.getMillasCanjeadas() > this.masCanjeador.getMillasCanjeadas()) {
            this.masCanjeador = u;
        }
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        Usuario uOrigen = buscarUsuario(origen);
        Usuario uDestino = buscarUsuario(destino);
        uOrigen.setMillasDisponibles(-cantidad);
        uOrigen.setMillasCanjeadas(cantidad);
        if (this.masCanjeador == null || uOrigen.getMillasCanjeadas() > this.masCanjeador.getMillasCanjeadas()) {
            this.masCanjeador = uOrigen;
        }

        if (uOrigen.esExclusivo == false) {
            if (uOrigen.exclusivo == null) {
                uOrigen.exclusivo = uDestino;
                uOrigen.esExclusivo = true;
            }
        } else {
            if (uOrigen.exclusivo != uDestino) {
                uOrigen.esExclusivo = false;
            }
        }
        uDestino.setMillasDisponibles(cantidad);
    }

    public double consultarSaldo(int id) {
        return buscarUsuario(id).getMillasDisponibles();
    }

    public String consultarCategoria(int id) {
        double millas = buscarUsuario(id).getMillasDisponibles();

        if (millas < 10000)
            return "Bronce";
        if (millas < 50000)
            return "Plata";
        if (millas < 1000000)
            return "Oro";
        return "Platino";
    }

    public double categoriaFactor(int id) {
        String categoria = consultarCategoria(id);
        if (categoria == "Bronce")
            return 1;
        if (categoria == "Plata")
            return 1.5;
        if (categoria == "Oro")
            return 2;
        return 0;
    }

    public int[] rankingMillas(int n) {
        int[] ranking = new int[n];
        Usuario[] aux = new Usuario[n];
        // los saco para armar el ranking
        for (int i = 0; i < n; i++) {
            Usuario u = rankingMillas.desencolarMax();
            ranking[i] = u.getId();
            aux[i] = u;
        }
        // los vuelvo a encolar para no perder el ranking
        for (int i = 0; i < n; i++) {
            rankingMillas.encolar(aux[i]);
        }
        return ranking;
    }

    public void iniciarPromocion(String nombre, double factor) {
        Promocion promo = new Promocion(nombre, factor, -1);
        promociones.definir(nombre, promo);
    }

    public void iniciarPromocionConTope(String nombre, double factor, double tope) {
        Promocion promo = new Promocion(nombre, factor, tope);
        promociones.definir(nombre, promo);
    }

    public void finalizarPromocion(String nombre) {
        this.promociones.borrar(nombre);
    }

    public int elMasAcumulador() {
        return this.masAcumulador.getId();
    }

    public int elMasCanjeador() {
        return this.masCanjeador.getId();
    }

    public int[][] paresTransferidoresExclusivos() {
        ArrayList<int[]> paresaux = new ArrayList<int[]>();

        ABB<Usuario>.ABB_Iterador it = this.usuarios.iterador();

        while (it.haySiguiente()) {
            Usuario u = it.siguiente();

            if (u.esExclusivo) {
                Usuario u2 = u.exclusivo;
                if (u.getId() < u2.getId()) {
                    paresaux.add(new int[] { u.getId(), u2.getId() });
                }
            }
        }

        int[][] res = new int[paresaux.size()][2];
        for (int i = 0; i < paresaux.size(); i++) {
            res[i] = paresaux.get(i);
        }

        return res;
    }
}
