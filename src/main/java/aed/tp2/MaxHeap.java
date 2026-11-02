package aed.tp2;

import aed.tp2.ABB.Nodo;

public class MaxHeap<T extends Comparable<T>> {
    ArrayList<T> heap;
    Nodo<T> raiz;

    private void encolar(T elem) {
        heap.add(elem);
        int index = heap.size() - 1;
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap.get(index).compareTo(heap.get(parentIndex)) > 0) {
                T temp = heap.get(index);
                heap.set(index, heap.get(parentIndex));
                heap.set(parentIndex, temp);
                index = parentIndex;
            } else {
                break;
            }
        }
    }
}
