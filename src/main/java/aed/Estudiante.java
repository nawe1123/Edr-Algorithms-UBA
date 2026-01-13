package aed;

public class Estudiante {

    public int[] respuestas;
    public boolean entrego;
    public boolean esSospechosx;
    public int aciertos;
    public int respondidas;
    public Posicion posicion;

    public Estudiante(int R){
        this.respuestas = new int[R];
        this.entrego = false;
        this.esSospechosx = false;
        this.aciertos = 0;
        this.respondidas = 0;
        this.posicion = null;
    }
    
    public void responder(int ejercicio, int res) {
        this.respuestas[ejercicio] = res;
        if (res != -1) {
            respondidas++;
        }
    }

}
