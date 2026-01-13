package aed;

public class NotaFinal implements Comparable<NotaFinal> {
    public double _nota;
    public int _id;

    public NotaFinal(double nota, int id){
        _nota = nota;
        _id = id;
    }

    public int compareTo(NotaFinal otra){
        if (otra._nota != this._nota){
           return Double.compare(otra._nota, this._nota);
            
        }
        return otra._id - this._id;
    }

     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotaFinal nf = (NotaFinal) o;
        return this._id == nf._id && this._nota == nf._nota;
    }
}
