package aed.tp2;

public class Trie<T> {
    private static class Nodo<T> {
        char c;
        T valor; // null = ninguna clave termina aca
        Nodo<T> hijo; // primer hijo
        Nodo<T> hermano; // siguiente hermano

        Nodo(char c) {
            this.c = c;
        }
    }

    private Nodo<T> raiz = new Nodo<>('\0');
    private int tamaño = 0;

    // Busca el hijo con la letra c y si no existe, lo crea en orden
    private Nodo<T> obtenerOCrearHijo(Nodo<T> padre, char c) {
        Nodo<T> ant = null;
        Nodo<T> h = padre.hijo;
        while (h != null && h.c < c) {
            ant = h;
            h = h.hermano;
        }
        if (h != null && h.c == c)
            return h;

        Nodo<T> nuevo = new Nodo<>(c);
        nuevo.hermano = h;
        if (ant == null)
            padre.hijo = nuevo;
        else
            ant.hermano = nuevo;
        return nuevo;
    }

    private Nodo<T> buscarNodo(String clave) {
        Nodo<T> nodo = raiz;
        for (int i = 0; i < clave.length() && nodo != null; i++) {
            char c = clave.charAt(i);
            Nodo<T> h = nodo.hijo;
            while (h != null && h.c < c)
                h = h.hermano;
            nodo = (h != null && h.c == c) ? h : null;
        }
        return nodo;
    }

    public void definir(String clave, T valor) {
        Nodo<T> nodo = raiz;
        for (int i = 0; i < clave.length(); i++) {
            nodo = obtenerOCrearHijo(nodo, clave.charAt(i));
        }
        if (nodo.valor == null)
            tamaño++;
        nodo.valor = valor;
    }

    public T obtener(String clave) {
        Nodo<T> n = buscarNodo(clave);
        return (n == null) ? null : n.valor;
    }

    public boolean esta(String clave) {
        return obtener(clave) != null;
    }

    public boolean borrar(String clave) {
        Nodo<T> n = buscarNodo(clave);
        if (n == null || n.valor == null)
            return false; // no estaba
        borrarRecursivo(raiz, clave, 0);
        tamaño--;
        return true;
    }

    // Devuelve true si 'nodo' quedó sin valor y sin hijos (el padre lo puede sacar)
    private boolean borrarRecursivo(Nodo<T> nodo, String clave, int i) {
        if (i == clave.length()) {
            nodo.valor = null; // caso base: llegamos al final
        } else {
            char c = clave.charAt(i);

            // buscar el hijo con la letra c, recordando el hermano anterior
            Nodo<T> ant = null;
            Nodo<T> h = nodo.hijo;
            while (h.c != c) { // existe seguro: ya lo verificó borrar()
                ant = h;
                h = h.hermano;
            }

            if (borrarRecursivo(h, clave, i + 1)) {
                // desenganchar h de la lista de hermanos
                if (ant == null)
                    nodo.hijo = h.hermano;
                else
                    ant.hermano = h.hermano;
            }
        }
        return nodo.valor == null && nodo.hijo == null;
    }

    public int tamaño() {
        return tamaño;
    }
}