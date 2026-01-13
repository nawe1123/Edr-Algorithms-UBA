package aed;

import java.util.ArrayList;
//sabemos que el Heap, al ser un arbol binario, tiene para n elementos
// una altura de log n. Con esto nos aseguramos que cada vez que usamos las 
// operaciones Agregar o Eliminar tiene como costo O(log n)
// en nuestro caso O(log E)

public class Heap<T extends Comparable<T>> {

    private ArrayList<T> heap;
    private ArrayList<Handle<T>> handles;

    public Heap() {
        this.heap = new ArrayList<>();
        this.handles = new ArrayList<>();
    }

    public int tamaño(){
        return heap.size();
    }

    public boolean vacio(){
        return heap.isEmpty();
    }

    public T raiz(){
        return heap.get(0);
    }

    public Handle<T> agregar(T elemento) {
        int id = heap.size();
        heap.add(elemento);

        Handle<T> h = new Handle<>(elemento, id);
        handles.add(h);

        siftUp(id);
        return h;
    }

    public ArrayList<Handle<T>> heapify(ArrayList<T> elementos) {
        int n = elementos.size();

        heap = new ArrayList<>(n);
        handles = new ArrayList<>(n);
        ArrayList<Handle<T>> resultado = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            T elem = elementos.get(i);
            heap.add(elem);
            Handle<T> h = new Handle<>(elem, i);
            handles.add(h);
            resultado.add(h);
        }

        for (int i = n / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }

        return resultado;
    }

    public T sacarRaiz() {   
        if (heap.isEmpty()) {
            return null;
        }

        T raiz = heap.get(0);

        int ultimo = heap.size() - 1;
        intercambiar(0, ultimo);

        heap.remove(ultimo);
        Handle<T> hanUltimo = handles.remove(ultimo);
        hanUltimo.indice = -1;

        if (!heap.isEmpty()) {
            siftDown(0);
        }
        return raiz;
    }

    public void eliminar(Handle<T> h) {
        int ind = h.indice;
        if (ind < 0 || ind >= heap.size()) {
            return; 
        }

        int ultimo = heap.size() - 1;
        intercambiar(ind, ultimo);

        heap.remove(ultimo);
        Handle<T> hUltimo = handles.remove(ultimo);
        hUltimo.indice = -1;

        h.indice = -1;

        if (ind < heap.size()) {
            siftUp(ind);
            siftDown(ind);
        }
    }

    public void actualizarClave(Handle<T> h, T nuevoValor) {
        int ind = h.indice;
        if (ind < 0 || ind >= heap.size()) {
            return;
        }
        heap.set(ind, nuevoValor);
        h.elemento = nuevoValor;

        siftUp(ind);
        siftDown(ind);
        
    }

    private int padre(int i) {
        return (i-1) / 2;
    }

    private int hijoIzq(int i) {
        return 2 * i + 1;
    }

    private int hijoDer(int i) {
        return 2 * i + 2;
    }

    private void siftUp(int i) {
        while (i > 0) {
            int padre = padre(i);
            if (heap.get(i).compareTo(heap.get(padre)) < 0) {
                intercambiar(i, padre);
                i = padre;
            } else {
                break;
            }
        }
    }

    private void siftDown(int i) {
        int n = heap.size();
        while (true) {
            int hijoIzq = hijoIzq(i);
            int hijoDer = hijoDer(i);
            int act = i;

            if (hijoIzq < n && heap.get(hijoIzq).compareTo(heap.get(act)) < 0) {
                act = hijoIzq;
            }
            if (hijoDer < n && heap.get(hijoDer).compareTo(heap.get(act)) < 0) {
                act = hijoDer;
            }
            if (act == i) {
                break;
            }
            intercambiar(i, act);
            i = act;
        }
    }

    private void intercambiar(int i, int j) {
        
        T ni = heap.get(i);
        T nj = heap.get(j);
        heap.set(i, nj);
        heap.set(j, ni);

        Handle<T> hi = handles.get(i);
        Handle<T> hj = handles.get(j);
        handles.set(i, hj);
        handles.set(j, hi);

        hi.indice = j;
        hj.indice = i;
    }
    
}
