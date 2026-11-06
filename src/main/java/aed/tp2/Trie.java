package aed.tp2;

public class Trie<T> {
    private class Nodo {
        char c;
        T valor;
        Nodo hijo;
        Nodo hermano;

        Nodo(char c) {
            this.c = c;
        }

    }
    private Nodo raiz = new Nodo('\0'); // raiz es el caracter nulo de ascii (preguntar si tiene que valer esto o null)
    
    private Nodo definirAux(Nodo actual, char c) {
        Nodo anterior = null;
        Nodo hijo = actual.hijo;
        while (hijo != null && hijo.c != c) {
          anterior = hijo;
          hijo = hijo.hermano;  
        }
        if (hijo != null && hijo.c == c) {
            return hijo;}
        else {
            Nodo nuevo = new Nodo(c);
            if (anterior == null) {
                actual.hijo = nuevo;
            } else {
                anterior.hermano = nuevo;
            }  
            return nuevo;
        }
    }

    public void definir(String palabra, T valor) {
        Nodo actual = raiz;
        for (int i = 0; i < palabra.length(); i++) {
            actual = definirAux(actual, palabra.charAt(i));      
        }
        actual.valor = valor;
    }

    private Nodo obtenerAux(String palabra){
        Nodo actual = raiz;

        for (int i = 0; i < palabra.length(); i++) {
            char c = palabra.charAt(i);
            Nodo hijo = actual.hijo;
            while (hijo != null && hijo.c != c) { // avanza mientras haya hermanos (no null) y no sea el buscado
                hijo = hijo.hermano;
            } 
            if (hijo == null) { // se recorrieron todos los hermanos sin encontrar c (el prefijo no existe)
                return null;
            }
            actual = hijo.c == c ? hijo : null;
        }

        return actual;
    }
    
    public T obtener(String palabra) {
        Nodo nodo = obtenerAux(palabra);
        if (nodo == null || nodo.valor == null) { // recordemos que si encontramos la palabra pero nodo.valor es null entonces es una palabra que nunca "definimos"
            throw new Error("no existe la palabra"); // simplemente esta por ser prefijo de una palabra que si definimos en el trie.
        }
        return nodo.valor;
    }

    public void borrar(String palabra) {
        raiz.hijo = borrarAux(raiz.hijo, palabra, 0);
    }

    private Nodo borrarAux(Nodo actualHermano, String palabra, int indice) {
        if (actualHermano == null) { //este if se cubre si el hijo es null
            throw new Error("no existe la palabra");
        }
        char c = palabra.charAt(indice);
        Nodo hermanoAnterior = null;
        Nodo nodo = actualHermano;
        while (nodo != null && nodo.c != c) {
            hermanoAnterior = nodo;
            nodo = nodo.hermano;
        }

        if (nodo == null) {
            throw new Error("no existe la palabra"); //xq no existe la palabra
        }


        if (indice == palabra.length() - 1) {
            if (nodo.valor == null) {
                throw new Error ("no existe la palabra"); //la palabra "existe" como prefijo (pero no es una palabra con valor)
            }
            nodo.valor = null;
        } else {
            nodo.hijo = borrarAux(nodo.hijo, palabra, indice + 1);
        }

        if (nodo.valor == null && nodo.hijo == null) {
            if (hermanoAnterior == null) {
                actualHermano = nodo.hermano;
            } else {
                hermanoAnterior.hermano = nodo.hermano;
            }
        }

        return actualHermano;
    }
}