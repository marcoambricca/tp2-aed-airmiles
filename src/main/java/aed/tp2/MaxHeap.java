package aed.tp2;

import java.util.ArrayList;
import java.util.Comparator;

public class MaxHeap<T extends Comparable<T>> {
    private ArrayList<HandleMH> _heap;
    private Comparator<T> _cmp;

    public MaxHeap(Comparator<T> cmp) {
        this._heap = new ArrayList<>();
        this._cmp = cmp;
    }

    private class HandleMH implements Handle<T>{
        /*Completar con los atributos privados*/
        private T _ref;
        private int _indice;

        private HandleMH(T n, int i){
            this._ref = n;
            this._indice = i;
        }

        public T valor(){
            return _ref;
        }

        public void modificar(T v){
            int nuevoIndice = reordenarHeapUp(this._indice); // paso indice xq estaba mirando solo el ultimo elemento del heap. quiero ver el indice del elemento que se modifico, para reordenar el heap desde ahi.
            bajarIesiemo(nuevoIndice);
        }

        public void eliminar(){}

        public boolean equals(Handle<T> otro){
            return _ref == otro.valor();
        }
        
        public int compareTo(Handle<T> otro){
            return _ref.compareTo(otro.valor());
        }

        public String toString(){
            return _ref.toString();
        }
    }

    public HandleMH encolar(T elem) {
        HandleMH nuevo = new HandleMH(elem, _heap.size());
        _heap.add(nuevo);
        int indice = _heap.size()-1;
        reordenarHeapUp(indice);
        // bajarIesiemo(indice); !! acá saco esa xq ya lo agregué al final y no hace falta bajar, solo subir si necesario. (además, si lo reordeno, el ultimo elemento puede cambiar a otro, pero igual no hay que bajarlo a este otro...)

        return nuevo;
    }

    private int reordenarHeapUp(int i) {
        while (i > 0) {
            int padreIndex = (i - 1) / 2;

            if (_cmp.compare(_heap.get(i).valor(), _heap.get(padreIndex).valor()) > 0) {
                HandleMH hijo = _heap.get(i);
                HandleMH padre = _heap.get(padreIndex);

                hijo._indice = padreIndex;
                padre._indice = i;

                _heap.set(i, padre);
                _heap.set(padreIndex, hijo);

                i = padreIndex;
            } else {
                break;
            }
        }
    return i;
    }

    private void bajarIesiemo(int i) {
        int n = _heap.size();
        while (true) {
            int izq = 2 * i + 1, der = 2 * i + 2, mayor = i;
            if (izq < n && _cmp.compare(_heap.get(izq).valor(), _heap.get(mayor).valor()) > 0)
                mayor = izq;
            if (der < n && _cmp.compare(_heap.get(der).valor(), _heap.get(mayor).valor()) > 0)
                mayor = der;
            if (mayor == i)
                break;

            HandleMH actual = _heap.get(i);
            HandleMH hijoMayor = _heap.get(mayor);

            actual._indice = mayor;
            hijoMayor._indice = i;

            _heap.set(i, hijoMayor);
            _heap.set(mayor, actual);
            i = mayor;
        }
    }

    public T maximo() {
        if (!(_heap.isEmpty())) { return _heap.get(0).valor();}
        else {return null;}
    }

    public int tamano() {
        return _heap.size();
    }

    public HandleMH desencolarMax() {
        if (_heap.isEmpty()) {
            return null;
        }

        HandleMH res = _heap.get(0);
        HandleMH ultimo = _heap.remove(_heap.size() - 1);

        if (!(_heap.isEmpty())){
            ultimo._indice = 0;
            _heap.set(0, ultimo);
            bajarIesiemo(0);
        }

        return res;
    }

    public void reencolar(Handle<T> handle) {
        HandleMH handleMH = (HandleMH) handle;
        handleMH._indice = _heap.size();
        _heap.add(handleMH);
        reordenarHeapUp(handleMH._indice);
    }
}
