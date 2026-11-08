package aed.tp2;

import java.util.ArrayList;
import java.util.Comparator;

public class MaxHeap<T extends Comparable<T>> {
    private ArrayList<T> _heap;
    private Comparator<T> _cmp;

    public MaxHeap(Comparator<T> cmp) {
        this._heap = new ArrayList<>();
        this._cmp = cmp;
    }

    private class HandleMH implements Handle<T>{
        /*Completar con los atributos privados*/
        private T _ref;

        private HandleMH(T n){
            this._ref = n;
        }

        public T valor(){
            return _ref;
        }

        public void modificar(T v){
            int indice = _heap.indexOf(v);
            reordenarHeapUp(indice); // paso indice xq estaba mirando solo el ultimo elemento del heap. quiero ver el indice del elemento que se modifico, para reordenar el heap desde ahi.
            bajarIesiemo(indice);
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
        HandleMH nuevo = new HandleMH(elem);
        _heap.add(elem);
        int indice = _heap.size()-1;
        reordenarHeapUp(indice);
        // bajarIesiemo(indice); !! acá saco esa xq ya lo agregué al final y no hace falta bajar, solo subir si necesario. (además, si lo reordeno, el ultimo elemento puede cambiar a otro, pero igual no hay que bajarlo a este otro...)

        return nuevo;
    }

    private int reordenarHeapUp(int i) {
        while (i > 0) {
            int padreIndex = (i - 1) / 2;
            if (_cmp.compare(_heap.get(i), _heap.get(padreIndex)) > 0) {
                T temp = _heap.get(i);
                _heap.set(i, _heap.get(padreIndex));
                _heap.set(padreIndex, temp);
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
            if (izq < n && _cmp.compare(_heap.get(izq), _heap.get(mayor)) > 0)
                mayor = izq;
            if (der < n && _cmp.compare(_heap.get(der), _heap.get(mayor)) > 0)
                mayor = der;
            if (mayor == i)
                break;
            T tmp = _heap.get(i);
            _heap.set(i, _heap.get(mayor));
            _heap.set(mayor, tmp);
            i = mayor;
        }
    }

    public T maximo() {
        if (!(_heap.isEmpty())) { return _heap.get(0);}
        else {return null;}
    }

    public int tamano() {
        return _heap.size();
    }

    public T desencolarMax() {
        T res = null;
        if (!(_heap.isEmpty())){
            res = _heap.get(0);
            _heap.set(0, _heap.get(_heap.size() - 1));
            bajarIesiemo(0);
            _heap.remove(_heap.size() - 1);
        }

        return res;
    }
}
