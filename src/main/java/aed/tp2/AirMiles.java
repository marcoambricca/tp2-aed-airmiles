package aed.tp2;

import java.util.ArrayList;
import java.util.Comparator;

public class AirMiles implements SistemaAirMiles {
    private DiccionarioABB<Integer, Handle<Usuario>> usuarios;
    private MaxHeap<Usuario> rankingMillas;
    private Usuario masCanjeador;
    private Usuario masAcumulador;
    private Trie<Promocion> promociones;

    public AirMiles() {
        this.usuarios = new DiccionarioABB<Integer, Handle<Usuario>>();
        this.rankingMillas = new MaxHeap<>(new PorMillasDisponibles());
        this.masCanjeador = null;
        this.masAcumulador = null;
        this.promociones = new Trie<>();
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
        Handle<Usuario> h = this.rankingMillas.encolar(nuevo);
        this.usuarios.insertar(id, h);
        if (this.masAcumulador == null) {
            this.masAcumulador = nuevo;
        }
        if (this.masCanjeador == null) {
            this.masCanjeador = nuevo;
        }
    }

    public void registrarUsuarioConReferidor(int id, int ref) {
        registrarUsuario(id);

        Handle<Usuario> nuevo = buscarUsuario(id);
        nuevo.valor().setMillasDisponibles(500);
        nuevo.valor().setMillasAcumuladas(500);
        nuevo.modificar(nuevo.valor()); //reacomodo heap

        Handle<Usuario> referidor = buscarUsuario(ref);
        referidor.valor().setMillasDisponibles(500);
        referidor.valor().setMillasAcumuladas(500);
        referidor.modificar(referidor.valor()); //reacomodo heap
    }

    private Handle<Usuario> buscarUsuario(int id) {
        return usuarios.obtener(id);
    }

    public void acumularMillas(int id, double distancia, String nombre) {
        Promocion promo = promociones.obtener(nombre);
        if (promo == null){
            promo = promociones.obtener("NoPromo"); // si no existe el nombre de la promocion o la eliminamos (y por lo tanto deja de existir)
        }                                                   // entonces le damos nopromo, que basicamente tiene factor 1 y sin tope (es como no usar ninguna promo)
        Handle<Usuario> handleUsuario = buscarUsuario(id);
        Usuario u = handleUsuario.valor(); 
        if (promo.getTope() == -1) { // si tope es -1 es que es promocion sin tope
            u.setMillasDisponibles(distancia * promo.getFactor() * categoriaFactor(id));
            u.setMillasAcumuladas(distancia * promo.getFactor() * categoriaFactor(id));
        }
        else {
            // si la cantidad de millas que se acumulan es mayor al tope, se acumula la
            // distancia sin factor
            if (distancia * promo.getFactor() > promo.getTope()) {
                u.setMillasDisponibles(distancia * categoriaFactor(id));
                u.setMillasAcumuladas(distancia * categoriaFactor(id));
            } 
            // si la cantidad de millas que se acumulan es menor o igual al tope, se
            // acumula la distancia con factor y se descuenta del tope
            else if (distancia * promo.getFactor() <= promo.getTope()) {
                u.setMillasDisponibles(distancia * promo.getFactor() * categoriaFactor(id));
                u.setMillasAcumuladas(distancia * promo.getFactor() * categoriaFactor(id));
                promo.setTope(promo.getTope() - distancia * promo.getFactor() * categoriaFactor(id));
            }
        }
        // logica mas acumulador
        if (this.masAcumulador == null || u.getMillasAcumuladas() > this.masAcumulador.getMillasAcumuladas()) {
            this.masAcumulador = u;
        }

        handleUsuario.modificar(u);
    }

    public void canjearMillas(int id, double cantidad) {
        Handle<Usuario> handleUsuario = buscarUsuario(id);
        Usuario u = handleUsuario.valor();
        u.setMillasDisponibles(-(cantidad));
        u.setMillasCanjeadas(cantidad);
        if (this.masCanjeador == null || u.getMillasCanjeadas() > this.masCanjeador.getMillasCanjeadas()) {
            this.masCanjeador = u;
        }

        handleUsuario.modificar(u); //reacomodo heap
    }

    public void transferirMillas(int origen, int destino, double cantidad) {
        Handle<Usuario> handleUOrigen = buscarUsuario(origen);
        Handle<Usuario> handleUDestino = buscarUsuario(destino);
        Usuario uOrigen = handleUOrigen.valor();
        Usuario uDestino = handleUDestino.valor();
        uOrigen.setMillasDisponibles(-cantidad);
        handleUOrigen.modificar(uOrigen); //reacomodo heap
        uOrigen.setMillasCanjeadas(cantidad);
        if (this.masCanjeador == null || uOrigen.getMillasCanjeadas() > this.masCanjeador.getMillasCanjeadas()) {
            this.masCanjeador = uOrigen;
        }
        // origen y destino no tienen exclusivos:
        if (uOrigen.exclusivo == null && uDestino.exclusivo == null){
            uOrigen.exclusivo = uDestino;
            uOrigen.esExclusivo = true;
            uDestino.exclusivo = uOrigen;
            uDestino.esExclusivo = true;
        }

        else if (uOrigen.exclusivo == uDestino && uDestino.exclusivo == uOrigen){ //no pasa nada
        } else { // se rompe
            uOrigen.esExclusivo = false;
            if (uOrigen.exclusivo != null){
                uOrigen.exclusivo.esExclusivo = false;
            } else {
                uOrigen.exclusivo = uDestino; //para registrar
            }

            uDestino.esExclusivo = false;
            if (uDestino.exclusivo != null){
                uDestino.exclusivo.esExclusivo = false;
            } else {
                uDestino.exclusivo = uOrigen; //para registrar
            }}
            
        uDestino.setMillasDisponibles(cantidad);
        handleUDestino.modificar(uDestino); //reacomodo heap
    }

    public double consultarSaldo(int id) {
        return buscarUsuario(id).valor().getMillasDisponibles();
    }

    public String consultarCategoria(int id) {
        double millas = buscarUsuario(id).valor().getMillasAcumuladas();

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
        int largo = rankingMillas.tamano() < n ? rankingMillas.tamano() : n;
        int[] ranking = new int[largo];
        Usuario[] aux = new Usuario[largo];
        // los saco para armar el ranking
        for (int i = 0; i < largo; i++) {
            Usuario u = rankingMillas.desencolarMax();
            ranking[i] = u.getId();
            aux[i] = u;
        }
        // los vuelvo a encolar para no perder el ranking
        for (int i = 0; i < largo; i++) {
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
        this.promociones.borrar(nombre); // elimina del trie y luego utiliza no promo (factor 1)
    }

    public int elMasAcumulador() {
        return this.masAcumulador.getId();
    }

    public int elMasCanjeador() {
        return this.masCanjeador.getId();
    }

    public int[][] paresTransferidoresExclusivos() {
        ArrayList<int[]> paresaux = new ArrayList<int[]>();

        DiccionarioABB<Integer, Handle<Usuario>>.DiccionarioABB_Iterador it = this.usuarios.iterador();

        while (it.haySiguiente()) {
            Handle<Usuario> handleU = it.siguiente();
            Usuario u = handleU.valor();

            if (u.esExclusivo && u.exclusivo != null && u.exclusivo.esExclusivo) {
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
