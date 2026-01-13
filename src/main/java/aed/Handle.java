package aed;

public class Handle<E> {
        int indice;
        E elemento;

        public Handle(E elemento, int indice){
            this.elemento = elemento;
            this.indice = indice;
        }
}
