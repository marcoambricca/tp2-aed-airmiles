package aed.tp2;

public class Trie<T> {
    private class Nodo<T> {
        char c;
        T valor;
        Nodo<T> hijo;
        Nodo<T> hermano;

        Nodo(char c) {
            this.c = c;
        }

    }

    private Nodo<T> raiz = new Nodo<>('\0');
    /*El método busca o crea un nodo hijo de padre cuyo carácter sea c. Recorre la lista enlazada de hijos, que se mantiene ordenada por carácter, usando h para el nodo actual y ant para el anterior. El recorrido se detiene al encontrar un carácter igual o uno mayor que c.

    Si encuentra un hijo con carácter c, lo devuelve sin modificar la lista. De lo contrario, crea un nodo y lo inserta en la posición correcta: antes de h. Si no hay nodo anterior, el nuevo pasa a ser el primer hijo; si lo hay, se enlaza después de ant. Finalmente, devuelve el nodo creado. 
    
    que pasa si hay una promo "PROMO" y otra "PROMOCION". donde termina promo y donde empieza "ocion"*/
    private Nodo<T> definirAux(Nodo<T> actual, char c) {
        Nodo<T> anterior = null;
        Nodo<T> hijo = actual.hijo;
        while (hijo != null && hijo.c != c) {
          anterior = hijo;
          hijo = hijo.hermano;  
        }
        if (hijo != null && hijo.c == c) {
            return hijo;}
        else {
            Nodo<T> nuevo = new Nodo<>(c);
            if (anterior == null) {
                actual.hijo = nuevo;
            } else {
                anterior.hermano = nuevo;
            }  
            return nuevo;
        }
    }

    public void definir(String palabra, T valor) {
        Nodo<T> actual = raiz;
        for (int i = 0; i < palabra.length(); i++) {
            actual = definirAux(actual, palabra.charAt(i));      
        }
        actual.valor = valor;
    }

    private Nodo<T> obtenerAux(String palabra){
        Nodo<T> actual = raiz;

        for (int i = 0; i < palabra.length(); i++) {
            char c = palabra.charAt(i);
            Nodo<T> hijo = actual.hijo;
            while (hijo.c != c) {
                hijo = hijo.hermano;
            } 
            actual = hijo.c == c ? hijo : null;
        }

        return actual;
    }
    
    public T obtener(String palabra) {
        Nodo<T> nodo = obtenerAux(palabra);
        if (nodo == null) {
            throw new Error("no existe la palabra");
        }
        return nodo.valor;
    }

    public void borrar(String palabra) {
        raiz.hijo = borrarAux(raiz.hijo, palabra, 0);
    }

    private Nodo<T> borrarAux(Nodo<T> actualHermano, String palabra, int indice) {
        if (actualHermano == null) { //este if se cubre si el hijo es null
            throw new Error("no existe la palabra");
        }
        char c = palabra.charAt(indice);
        Nodo<T> hermanoAnterior = null;
        Nodo<T> nodo = actualHermano;
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