package aed.tp2;

import java.util.ArrayList;

import aed.tp2.ABB.ABB_Iterador;

public class AirMiles implements SistemaAirMiles {
    /*COMPLETAR*/
    private ABB<Usuario> usuarios;
    //private MaxHeap<Usuario> rankingMillas;
    private Usuario masCanjeador;
    private Usuario masAcumulador;
    private Trie<Promocion> promociones;

    public AirMiles() {
        this.usuarios = new ABB<>();
        //this.rankingMillas = new MaxHeap<>();
        this.masCanjeador = null;
        this.masAcumulador = null;
        this.promociones = new Trie<>();
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

    private Usuario buscarUsuario(int id){ // PREGUNTAR SI ES VALIDO
        Usuario u = new Usuario(id);
        return usuarios.obtener(u);
    }

    public void acumularMillas(int id, double distancia, String nombre) {
        Promocion promo = promociones.obtener(nombre);
        Usuario u = buscarUsuario(id);
        //si tope es -1 es que es promocion sin tope
        if (promo.getTope() == -1) {
            u.setMillasDisponibles(distancia * promo.getFactor());
        } 
        //si tope no es -1 es que es promocion con tope
        else {
            //si la cantidad de millas que se acumularian es mayor al tope, se acumula la distancia sin factor
            if (distancia * promo.getFactor() > promo.getTope()) {
                u.setMillasDisponibles(distancia);
            //si la cantidad de millas que se acumularian es menor o igual al tope, se acumula la distancia con factor y se descuenta del tope
            } else if (distancia * promo.getFactor() <= promo.getTope()) {
                u.setMillasDisponibles(distancia * promo.getFactor());
                promo.setTope(promo.getTope() - distancia * promo.getFactor());
            }

            //veo luego de acumular si no queda tope y en ese caso borro la promocion
            if (promo.getTope() == 0){
                this.finalizarPromocion(nombrePromo);
            }
        }
        //logica mas acumulador
        if (this.masAcumulador == null || u.getMillasDisponibles() > this.masAcumulador.getMillasDisponibles()) {
            this.masAcumulador = u;
        }
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
        uDestino.setMillasDisponibles(cantidad);
    }

    public double consultarSaldo(int id) {
        Usuario u = new Usuario(id);
        return u.getMillasDisponibles();
    }

    public String consultarCategoria(int id) {
       Usuario u = new Usuario(id);
       double millas = u.getMillasDisponibles();

       if (millas < 10000) return "Bronce";
        if (millas < 50000) return "Plata";
         if (millas < 1000000) return "Oro";
        return "Platino";
    }

    /*public int[] rankingMillas(int n) {
        int[] ranking = new int[n];
        for (int i = 0; i < n; i++) {
            Usuario u = rankingMillas.eliminarMax();
            ranking[i] = u.getId();
        }
        return ranking;
    }*/

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
                if (u2.esExclusivo && u2.exclusivo == u) {
                    if (u.getId() < u2.getId()) {
                            paresaux.add(new int[]{u.getId(), u2.getId()});
                        }
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
sadfasdf