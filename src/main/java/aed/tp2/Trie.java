package aed.tp2;

public class Trie<T> {

    private class Nodo {
        T valor;
        Object[] hijos;

        Nodo() {
            hijos = new Object[256]; //decimos que el array hijos asociado al nodo puede tener 256 elementos porque se condice con la cantidad de 
        }                            //caracteres que tiene el ascii extendido. cada indice representa un caracter del ascii
        
    }
 
    private Nodo raiz = new Nodo();

    public void definir (String palabra, T valor) { //vamos a recorrer la palabra y si esta vacia esa posicion del array agregamos ese caracter en ella
        Nodo actual = raiz;
        for (int i = 0; i < palabra.length(); i++) {
            int car = palabra.charAt(i); //en vez de devolver un caracter el charAt devuelve un int que se corresponde con ese caracter en ascii 
            if (actual.hijos[car] == null) {
                actual.hijos[car] = new Nodo();
            }
            actual = (Nodo) actual.hijos[car]; //es necesario explicitar el tipo de dato porque sino no lo toma como Nodo
        }
        actual.valor = valor;
    }

    private Nodo obtenerAux(String palabra) { //misma logica que la funcion anterior, solo que si no esta el caracter entonces devolvemos null
        Nodo actual = raiz;                   //y si todos los caracteres estan entonces devolvemos el nodo
        for (int i = 0; i < palabra.length(); i ++) {
            int car = palabra.charAt(i);
            if (actual.hijos[car] == null) {
                return null;
            }
            actual = (Nodo) actual.hijos[car];
        }
        return actual;
    
    }

    public T obtener(String palabra) {
        Nodo nodo = obtenerAux(palabra);
        if (nodo == null || nodo.valor == null) { //devolvemos null (no esta la palabra) en el caso de que o bien no este 
            return null;                          //o este pero sea un prefijo (y no una palabra definida)
        }
        return nodo.valor;
    }

    public void borrar(String palabra) {
        borrarAux(raiz, palabra, 0);
    }  

    private boolean borrarAux(Nodo actual, String palabra, int indice) {
        if (indice == palabra.length()) { //llega al final de la palabra
            if (actual.valor == null) {
                return false; //como nunca fue definida, no hay nada que borrar
            }
            actual.valor = null;
        } else {
            int car = palabra.charAt(indice);
            if (actual.hijos[car] == null) {
                return false; //este camino no existe y por lo tanto la palabra no esta en el trie 
            }
            Nodo hijo = (Nodo) actual.hijos[car];
            boolean cortarHijo = borrarAux(hijo, palabra, indice + 1);
            if (cortarHijo) {
                actual.hijos[car] = null; //cortamos sin reconectar nada
            }
        }
        return actual.valor == null && sinHijos(actual);
    }

    private boolean sinHijos(Nodo nodo) { //vaciamos a todos los hijos de actual
        for (Object h : nodo.hijos) {
            if (h != null) {
                return false;
            }
        }
        return true;
    }
}   

