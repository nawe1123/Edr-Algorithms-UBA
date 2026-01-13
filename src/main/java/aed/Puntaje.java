package aed;

public class Puntaje implements Comparable<Puntaje> {
    public int id;
    public double puntaje;

    public Puntaje(int id, double puntaje) {
        this.id = id;
        this.puntaje = puntaje;
    }

    @Override public int compareTo(Puntaje p){
        if(this.puntaje != p.puntaje) {
            return Double.compare(this.puntaje, p.puntaje);
        }
        return Integer.compare(this.id, p.id);
    }
}

