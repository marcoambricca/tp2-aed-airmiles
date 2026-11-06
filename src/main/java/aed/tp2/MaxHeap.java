package aed.tp2;

import java.util.ArrayList;
import java.util.Comparator;

public class MaxHeap<T> {
    private ArrayList<T> _heap;
    private Comparator<T> _cmp;

    public MaxHeap(Comparator<T> cmp) {
        this._heap = new ArrayList<>();
        this._cmp = cmp;
    }

    public void encolar(T elem) {
        _heap.add(elem);
        reordenarHeapUp();
    }

    public void actualizar() {
        reordenarHeapUp();
    }

    private void reordenarHeapUp() {
        int i = _heap.size() - 1;
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