package aed.tp2;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Comparator;
import org.junit.jupiter.api.Test;

public class EstructurasBasicasTests {

    @Test
    void trieDefineYObtienePalabras() { // definimos palabras y comprobamos que devuelva sus valores 
        Trie<Integer> trie = new Trie<>();

        trie.definir("hola", 10);
        trie.definir("hilo", 20);
        trie.definir("casa", 30);

        assertEquals(10, trie.obtener("hola"));
        assertEquals(20, trie.obtener("hilo"));
        assertEquals(30, trie.obtener("casa"));
        assertNull(trie.obtener("ho"));
        assertNull(trie.obtener("adios")); // no encuentra palabras inexistentes
    }

    @Test
    void trieBorraPalabraSinRomperPrefijos() { // verificamos que los prefijos no se rompan a la hora de borrar
        Trie<String> trie = new Trie<>();

        trie.definir("perro", "dog");
        trie.definir("pera", "pear");

        trie.borrar("perro"); // comprueba que al borrar perro no borremos pera (mismo prefijo)

        assertNull(trie.obtener("perro"));
        assertEquals("pear", trie.obtener("pera"));
    }

    @Test
    void trieBorraPalabraQueEsPrefijoDeOtra() {
        Trie<String> trie = new Trie<>();

        trie.definir("cas", "casa");
        trie.definir("casa", "casita");

        trie.borrar("cas"); // borramos una palabra sin borrar otra que usa su mismo camino como prefijo

        assertNull(trie.obtener("cas"));
        assertEquals("casita", trie.obtener("casa"));
    }

    @Test
    void diccionarioABBInsertaYBusca() { 
        DiccionarioABB<Integer, String> dic = new DiccionarioABB<>();

        assertEquals(0, dic.cardinal()); // comprobamos que el abb empiece vacio

        dic.insertar(5, "cinco");
        dic.insertar(3, "tres");
        dic.insertar(8, "ocho"); // insertamos claves

        assertEquals(3, dic.cardinal()); // el cardinal vale
        assertEquals("tres", dic.obtener(3));
        assertEquals("cinco", dic.obtener(5));
        assertEquals("ocho", dic.obtener(8)); // busca valores
        assertEquals("tres", dic.minimo());
        assertEquals("ocho", dic.maximo()); // obtenemos maximo y minimo
    }

    @Test
    void diccionarioABBEliminaHoja() {
        DiccionarioABB<Integer, String> dic = new DiccionarioABB<>();

        dic.insertar(5, "cinco");
        dic.insertar(3, "tres");
        dic.insertar(8, "ocho");
        dic.insertar(7, "siete");

        dic.eliminar(7); // eliminamos un nodo hoja

        assertEquals(3, dic.cardinal());
        assertEquals("ocho", dic.obtener(8));
        assertEquals("tres", dic.minimo());
        assertEquals("ocho", dic.maximo()); // comprobamos que se actualice la cantidad sin afectar los demas valores
    }

    @Test
    void diccionarioABBEliminaRaizConDosHijos() {
        DiccionarioABB<Integer, String> dic = new DiccionarioABB<>();

        dic.insertar(5, "cinco");
        dic.insertar(3, "tres");
        dic.insertar(10, "diez");
        dic.insertar(7, "siete");
        dic.insertar(8, "ocho");

        dic.eliminar(5); // borramos una raiz con dos hijos cuyo sucesor tambien tiene un hijo

        assertEquals(4, dic.cardinal());
        assertEquals("siete", dic.obtener(7));
        assertEquals("ocho", dic.obtener(8));
        assertEquals("tres", dic.minimo());
        assertEquals("diez", dic.maximo());
    }

    @Test
    void maxHeapEncolaYDesencola() {
        MaxHeap<Integer> heap = new MaxHeap<Integer>(Comparator.<Integer>naturalOrder());

        heap.encolar(10);
        heap.encolar(3);
        heap.encolar(27);
        heap.encolar(15);

        assertEquals(27, heap.maximo()); // el maximo es elemento prioritario
        assertEquals(4, heap.tamano()); 

        assertEquals(27, heap.desencolarMax()); 
        assertEquals(15, heap.maximo()); // comprobamos que al desencolar se actualice correctamente el maximo
        assertEquals(3, heap.tamano()); // el tamaño tambien se actualiza

        assertEquals(15, heap.desencolarMax());
        assertEquals(10, heap.maximo());
    }

    @Test
    void maxHeapManejaVacioYUnElemento() {
        MaxHeap<Integer> heap = new MaxHeap<Integer>(Comparator.<Integer>naturalOrder());

        assertNull(heap.maximo()); // consultamos el maximo de un heap vacio
        assertNull(heap.desencolarMax()); // desencolar vacio no devuelve un elemento
        assertEquals(0, heap.tamano());

        heap.encolar(6);
        assertEquals(6, heap.maximo()); // comprobamos el caso con un unico elemento
        assertEquals(6, heap.desencolarMax());
        assertNull(heap.maximo()); // al quitar el unico elemento el heap vuelve a quedar vacio
        assertEquals(0, heap.tamano());
    }

    @Test
    void maxHeapDesencolaRepetidosEnOrden() {
        MaxHeap<Integer> heap = new MaxHeap<Integer>(Comparator.<Integer>naturalOrder());

        heap.encolar(4);
        heap.encolar(9);
        heap.encolar(9);
        heap.encolar(2); // incluimos prioridades repetidas

        assertEquals(9, heap.desencolarMax()); // los maximos repetidos tambien salen primero
        assertEquals(9, heap.desencolarMax());
        assertEquals(4, heap.desencolarMax());
        assertEquals(2, heap.desencolarMax());
        assertEquals(0, heap.tamano()); // verificamos que se pueda vaciar completamente
        assertNull(heap.desencolarMax());
    }
}
