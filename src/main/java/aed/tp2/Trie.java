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

    private Nodo<T> raiz = new Nodo<>("");
    /*El método busca o crea un nodo hijo de padre cuyo carácter sea c. Recorre la lista enlazada de hijos, que se mantiene ordenada por carácter, usando h para el nodo actual y ant para el anterior. El recorrido se detiene al encontrar un carácter igual o uno mayor que c.

    Si encuentra un hijo con carácter c, lo devuelve sin modificar la lista. De lo contrario, crea un nodo y lo inserta en la posición correcta: antes de h. Si no hay nodo anterior, el nuevo pasa a ser el primer hijo; si lo hay, se enlaza después de ant. Finalmente, devuelve el nodo creado. 
    
    que pasa si hay una promo "PROMO" y otra "PROMOCION". donde termina promo y donde empieza "ocion"*/
    private Nodo<T> definirAux(Nodo<T> actual, char c) {
        Nodo<T> anterior;
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
            throw new Error("palabra no existe");
        }
        return nodo.valor;
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