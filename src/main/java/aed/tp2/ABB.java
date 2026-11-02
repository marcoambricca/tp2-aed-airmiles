package aed.tp2;

import java.util.*;

// Todos los tipos de datos "Comparables" tienen el método compareTo()
// elem1.compareTo(elem2) devuelve un entero. Si es mayor a 0, entonces elem1 > elem2
public class ABB<T extends Comparable<T>> {
    // Agregar atributos privados del Conjunto
    private Nodo _raiz;
    private int _cardinal;

    private class Nodo {
        // Agregar atributos privados del Nodo
        T dato;
        Nodo padre;
        Nodo l;
        Nodo r;

        // Crear Constructor del nodo
        Nodo(T v) {
            dato = v;
            padre = null;
            l = null;
            r = null;
        }
    }

    public ABB() {
        _raiz = null;
        _cardinal = 0;
    }

    public int cardinal() {
        return this._cardinal;
    }

    public T minimo() {
        return minimo_aux(_raiz, _raiz.dato);
    }

    private T minimo_aux(Nodo actual, T elem) {
        if (actual.l == null) {
            return elem;
        } else
            return minimo_aux(actual.l, actual.l.dato);
    }

    public T maximo() {
        return maximo_aux(_raiz, _raiz.dato);
    }

    private T maximo_aux(Nodo actual, T elem) {
        if (actual.r == null) {
            return elem;
        } else
            return maximo_aux(actual.r, actual.r.dato);
    }

    public void insertar(T elem) {
        if (_raiz == null) {
            _raiz = new Nodo(elem);
            _cardinal++;
            return;
        }

        buscar_nodo(_raiz, elem);
    }

    private void buscar_nodo(Nodo actual, T elem) {
        if (actual.dato == elem)
            return;

        int compare = elem.compareTo(actual.dato);

        if (compare > 0) {
            if (actual.r == null) {
                Nodo nuevo = new Nodo(elem);
                nuevo.padre = actual;
                actual.r = nuevo;
                _cardinal++;
            } else
                buscar_nodo(actual.r, elem);
        } else if (compare < 0) {
            if (actual.l == null) {
                Nodo nuevo = new Nodo(elem);
                nuevo.padre = actual;
                actual.l = nuevo;
                _cardinal++;
            } else
                buscar_nodo(actual.l, elem);
        }
    }

    public boolean pertenece(T elem) {
        return busqueda_recursiva(_raiz, elem);
    }

    private boolean busqueda_recursiva(Nodo actual, T elem) {
        if (actual == null) {
            return false;
        }

        int compare = elem.compareTo(actual.dato);

        if (compare > 0) {
            return busqueda_recursiva(actual.r, elem);
        } else if (compare < 0) {
            return busqueda_recursiva(actual.l, elem);
        } else {
            return true;
        }

    }

    public void eliminar(T elem) {
        eliminar_aux(_raiz, elem);
    }

    private void eliminar_aux(Nodo actual, T elem) {
        if (actual == null) {
            return;
        }

        int compare = elem.compareTo(actual.dato);

        if (compare > 0) {
            eliminar_aux(actual.r, elem);
            ;
        } else if (compare < 0) {
            eliminar_aux(actual.l, elem);
        } else { // hasta acá igual que pertenece. ese else es cuando compare == 0, luego,
                 // elimino este.
            if (actual.l == null && actual.r == null) { // primer caso -> no tiene hijos
                if (actual.padre == null) // si no tiene padre es la raiz, si la raiz no tiene hijos y la elimino, el
                                          // arbol es null.
                    _raiz = null;
                else if (actual.padre.r == actual) // actual es el hijo derecho de su padre.
                    actual.padre.r = null;
                else
                    actual.padre.l = null; // actual es el hijo izquierdo de su padre.
                _cardinal--; // cardinal-- xq saqué uno
            } else if (actual.l == null || actual.r == null) { // caso tiene UN solo hijo
                Nodo hijo;
                if (actual.l == null) { // si no tiene hijo a la izquierda, entonces su hijo está a la derecha.
                    hijo = actual.r;
                } else { // idem para la derecha.
                    hijo = actual.l;
                }
                if (actual == _raiz) { // si elimino la raiz y solo tiene un hijo, entonces el hijo es la nueva raiz.
                    _raiz = hijo;
                    hijo.padre = null;
                } else {
                    if (actual.padre.r == actual) { // si actual está a la derecha de su padre, el hijo se trasforma en
                                                    // el actual
                        actual.padre.r = hijo;
                        hijo.padre = actual.padre;
                    } else { // idem para la izquierda.
                        actual.padre.l = hijo;
                        hijo.padre = actual.padre;
                    }
                }
                _cardinal--; // cardinal-- xq saqué uno
            } else { // ultimo caso: tiene DOS hijos
                Nodo sucesor = actual.r;
                while (sucesor.l != null) { // el sucesor es el mínimo del sub-árbol de la derecha.
                    sucesor = sucesor.l;
                }

                actual.dato = sucesor.dato; // cambio el dato del actual por el dato del sucesor
                eliminar_aux(actual.r, sucesor.dato); // y agarro el sub-arbol de la derecha (que contiene a sucesor) y
                                                      // elimino ese sucesor recursivamente.
                // acá _cardinal-- estaria demás xq al hacer eliminar_aux(actual.r,
                // sucesor.dato) ya va a entrar en alguno de los otros casos que ya hace
                // _cardinal--.
            }
        }
    }

    public String toString() {
        if (_cardinal == 0)
            return "{}";

        StringBuilder res = new StringBuilder();
        res.append("{");

        ABB_Iterador iterador = iterador();
        while (iterador.haySiguiente()) {
            res.append(iterador.siguiente());

            if (iterador.haySiguiente()) {
                res.append(",");
            }
        }

        return res.append("}").toString();
    }

    public class ABB_Iterador {
        private Nodo _actual = minimo(ABB.this._raiz);

        private Nodo minimo(Nodo actual) {
            if (actual == null) {
                return null;
            }
            while (actual.l != null) {
                actual = actual.l;
            }
            return actual;
        }

        public boolean haySiguiente() {
            return _actual != null;
        }

        public T siguiente() {
            T valor = _actual.dato;
            _actual = sucesor(_actual);
            return valor;
        }

        private Nodo sucesor(Nodo actual) {
            if (actual.r != null) {
                Nodo aux = actual.r;
                while (aux.l != null) {
                    aux = aux.l;
                }
                return aux;
            } else {
                Nodo p = actual.padre;
                while (p != null && actual == p.r) {
                    actual = p;
                    p = p.padre;
                }
                return p;
            }
        }
    }

    public ABB_Iterador iterador() {
        return new ABB_Iterador();
    }

}
