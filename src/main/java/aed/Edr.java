package aed;
import java.util.ArrayList;

public class Edr {
    
    private int[] solExamen;
    private Estudiante[] alumnos;
    private int[][] aula;
    private Heap<Puntaje> alumnosPorPuntaje;
    private ArrayList<Handle<Puntaje>> puntajesPorId;
    private int dimension;
    private int E; // cantidad de alumnos
    private int R; // cantidad de ejercicios

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        // asignacion de variables y constantes O(1)
        E = Cant_estudiantes;
        R = ExamenCanonico.length;
        dimension = LadoAula;

        // copia el examen canonico O(R)
        solExamen = ExamenCanonico.clone();

        // Añado a todos los estudiantes y lleno sus examenes en blanco O(E * R)
        crearAlumnos(E, R);

        //distribucion de asientos O(E)
        nuevaAula(E,dimension);

        // contruir MinHeap inciial O(E)
        contruirMinHeap(E);
    }
    // sabemos que al sumar las complejidades nos queda O del max
    // en este caso Edr= O(E*R) -> manda la complejidad de la matriz

    private void crearAlumnos(int E, int R){
        alumnos = new Estudiante[E]; // Creo array con respuestas vacio y asigno valores O(1)
        for (int id = 0; id < E; id++) { // Añado a todos O(E)
            alumnos[id] = new Estudiante(R);
            // LLeno su examen en Blanco O(R)
            for (int j = 0; j < R; j++) {
                alumnos[id].respuestas[j] = -1;
            }
        }
    } 

    private void nuevaAula(int E, int dimension){
        aula = new int[dimension][dimension];
        int f = 0;
        int c = 0;
        // sienta a los alumnos -> O(E)
        for (int id = 0; id < E; id++) {
            alumnos[id].posicion = new Posicion(f, c);
            aula[f][c] = id;
            c += 2;

            if (c >= dimension) {
                f++;
                c = 0;
            }
        }
    }
    private void contruirMinHeap(int E){
        alumnosPorPuntaje = new Heap<>();
        puntajesPorId = new ArrayList<>(E);

        // mete a todos los alumnos en el heap por id ->O(E)
        ArrayList<Puntaje> heap = new ArrayList<>(E);
        for (int id = 0; id < E; id++){
            heap.add(new Puntaje(id,0.0));
        }
        ArrayList<Handle<Puntaje>> handles = alumnosPorPuntaje.heapify(heap);
        for (int id = 0; id < E; id++) { // el heapify se hace lineal -> O(E)
            puntajesPorId.add(handles.get(id));
        }


    }
    private double puntajeAlumne(int estudiante) {
        double puntaje = (100.0 * alumnos[estudiante].aciertos) / R;
        int res = (int) puntaje;
        return (double) res; // todas operaciones y accesos a arrays -> O(1)
    }

    // complejidad de Edr -> O(E*R)

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        double[] res = new double[E];
        // asigna el valor que tiene guardado para cada estudiante -> O(E)
        for (int i = 0; i < E; i++){
            double nota = puntajeAlumne(i);
            res[i] = nota;
        }
        return res; //
    }
    // complejidad de notas -> O(E)
//------------------------------------------------COPIARSE------------------------------------------------------------------------

    public void copiarse(int estudiante) {
        // busca vecino con mas respuestas o desempata con id -> O(R)
        int[] resultadoBusqueda = encontrarMejorVecinoParaCopiar(estudiante);
        int vecinoACopiar = resultadoBusqueda[0];
        int maxRespondidas = resultadoBusqueda[1];

        // sino no hay vecino valida no hace nada -> O(1)
        if (vecinoACopiar == -1 || maxRespondidas == 0) {
            return;
        }

        // se copia y actualiza el puntaje si corresponde -> O(R + log(E))
        copiarseYActualizar(estudiante, vecinoACopiar);
    }

    private int[] encontrarMejorVecinoParaCopiar(int estudiante) {
        int [] resEstudiante = alumnos[estudiante].respuestas;
        ArrayList<Integer> vecinos = vecinos(estudiante);

        int vecinoACopiar = -1;
        int maxRespondidas = 0;

        for (int i = 0; i < vecinos.size(); i++){
            int vecino = vecinos.get(i);
            int[] resVecino = alumnos[vecino].respuestas;
            int otrasRespondidas = 0;   //bucle conste -> O(1)
            for (int j= 0; j < R; j++) {
                if(resEstudiante[j] == -1 && resVecino[j] != -1) {
                    otrasRespondidas++;
                }
                if (otrasRespondidas > maxRespondidas || (otrasRespondidas == maxRespondidas && vecino > vecinoACopiar)) {
                    maxRespondidas = otrasRespondidas;
                    vecinoACopiar = vecino;
                }       //bucle que itera R veces -> O(R)

            }   // suma los bucles, gana el que itera R veces -> O(R)


        }
        return new int[]{vecinoACopiar,maxRespondidas};
    }

    private void copiarseYActualizar(int estudiante, int vecinoACopiar) {
        int[] resEstudiante = alumnos[estudiante].respuestas;
        int[] resACopiar = alumnos[vecinoACopiar].respuestas;
        int ejercicioACopiar = -1;

        for (int j = 0; j < R; j++) { // recorre R veces -> O(R)
            if (resEstudiante[j] == -1 && resACopiar[j] != -1) {
                ejercicioACopiar = j;
                break;
            }
        }
        if (ejercicioACopiar == -1) {
            return;
        }
        int respCopiada = resACopiar[ejercicioACopiar];
        resEstudiante[ejercicioACopiar] = respCopiada;
        alumnos[estudiante].respondidas++;


        if (respCopiada == solExamen[ejercicioACopiar]) {
            actualizarPuntaje(estudiante);
        }   //actualiza el heap -> O(log E)
    }
    private ArrayList<Integer> vecinos(int estudiante){
        ArrayList<Integer> vecinos = new ArrayList<>();
        Posicion pos = alumnos[estudiante].posicion;
        int f = pos.fila;
        int c = pos.columna;
        if(f - 1 >= 0) {
            int vecino = aula[f - 1][c];
            vecinos.add(vecino);
        }
        if(c - 2 >= 0) {
            int vecino = aula[f][c-2];
            vecinos.add(vecino);
        }
        if(c+2 <= dimension){
            int vecino = aula[f][c+2];
            vecinos.add(vecino);
        }   //consulta el array -> O(1)

        return vecinos;
    } 
    // sumamos las complejidades para obtener la
    // complejidad de copiarse -> O(R+(log E) )

//-----------------------------------------------RESOLVER----------------------------------------------------------------

    public void resolver(int estudiante, int NroEjercicio, int res) {
        int[] resAlumnx = alumnos[estudiante].respuestas; // obtener en un array es O(1)
        resAlumnx[NroEjercicio] = res; // modificar posicion en un array es O(1)
        alumnos[estudiante].respondidas += 1; 
        if (res == solExamen[NroEjercicio]) { // comparar y obtener de un array O(1)
            actualizarPuntaje(estudiante); 
        }
    } 

    private void actualizarPuntaje(int estudiante) {
        alumnos[estudiante].aciertos += 1; // Modificar Posicion en un array O(1)
        double nuevoPuntaje = puntajeAlumne(estudiante); // O(1)
        Puntaje puntajeActualizado = new Puntaje(estudiante, nuevoPuntaje); // O(1)
        Handle<Puntaje> h = puntajesPorId.get(estudiante);
        alumnosPorPuntaje.actualizarClave(h, puntajeActualizado); // O(log E)        
    }
    //complejidad de resolver -> O(log E) 
    //ya que son todas O(1) hasta que actualiza el heap


//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------

    public void consultarDarkWeb(int n, int[] examenDW) {
            // extraer los n estudiante con peor puntaje -> O(n log E)
            int[] copionxs = peoresEstudiantes(n);

            // aplica el examen DW y los reinserta en el Min Heap -> O(n*(R+log E))
            copiarseDeDarkWebYReinsertar(copionxs, examenDW);
        }

    private int[] peoresEstudiantes(int n ) {
        int[] copionxs = new int[n];

        for (int k = 0; k < n; k++) { //bucle que se ejecuta n veces ->O(n)
            Puntaje peorPuntaje = alumnosPorPuntaje.sacarRaiz(); //sacar el minimo del heap cuesta O(log E)
            int id = peorPuntaje.id;
            copionxs[k] = id;
            puntajesPorId.set(id, null);
        }
        return copionxs;    // ejecutar n veces algo que cuesta O(log E) -> O(n log E)
    }

    private void copiarseDeDarkWebYReinsertar(int[] copionxs, int[] examenDW) {
        for (int i = 0; i < copionxs.length; i++) { // se ejecuta n veces-> O(n)
            int id = copionxs[i];                   // n es la long de copionxs
            int correctas = 0;
            int actRespondidas = 0;

            // aplicar el examen y contar (O(R))
            for (int j = 0; j < R; j++) {
                int respuesta = examenDW[j];
                alumnos[id].respuestas[j] = respuesta;

                if (respuesta != -1) {
                    actRespondidas++;
                    if (respuesta == solExamen[j]) {
                        correctas++;
                    }
                }
            }

            // actualizar estado y reinsertar al MinHeap (O(log E))
            alumnos[id].aciertos = correctas;
            alumnos[id].respondidas = actRespondidas;

            double nuevaNota = puntajeAlumne(id);
            Puntaje nuevoPuntaje = new Puntaje(id, nuevaNota);

            Handle<Puntaje> h = alumnosPorPuntaje.agregar(nuevoPuntaje);
            puntajesPorId.set(id, h);
        }
        // un bucle que se ejecuta n veces, adentro un bucle O(R) que usa el minHeap (O(log E))
        // -> O (n*(R+log E))
    }
    // complejidad de copiarse -> O (n*(R+log E))
    // es el max entre peoresEstudiantes y copiarseDeDarkWebYReinsertar

//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        alumnos[estudiante].entrego = true;
        Handle<Puntaje> h = puntajesPorId.get(estudiante);
        alumnosPorPuntaje.eliminar(h); // usamos un handle y nos cuesta O(log E) reordenar el heap
        puntajesPorId.set(estudiante, null);
    }
    //complejidad de entregar -> O(log E)

//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        // llena el MaxHeap solo con estudiantes no sospechosos -> O(E log E)
        Heap<NotaFinal> heap = construirHeapDeNoSospechosos();

        // vaciar el heap para obtener resultado ordenado -> (O(E log E))
        NotaFinal[] res = obtenerNotasOrdenadas(heap);

        return res;
    }

    private Heap<NotaFinal> construirHeapDeNoSospechosos(){
        Heap<NotaFinal> heap = new Heap<>();

        for (int id = 0; id < E; id++) { // se ejecuta E veces
            if (!alumnos[id].esSospechosx) {
                double nota = puntajeAlumne(id);
                NotaFinal notaFinal = new NotaFinal(nota, id);
                heap.agregar(notaFinal); // usar el heap -> O(log E)
            }
        }
        return heap;    // O(E) veces O(log E) -> O(E log E)
    }
    private NotaFinal[] obtenerNotasOrdenadas(Heap<NotaFinal> heap) {
        int tamaño = heap.tamaño(); // O(1)
        NotaFinal[] res = new NotaFinal[tamaño];

        for (int i = 0; i < tamaño; i++) { // E veces
            res[i] = heap.sacarRaiz(); // saca el máximo -> O(log E)
        }
        return res;     // O(E) veces O(log E) -> O(E log E)
    }
    // complejidad de corregir ->  O(E log E)

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        // contar la frecuencia de cada respuesta para cada ejercicio ->(O(E*R))
        int[][] vecesRepetidas = contarFrecuenciaDeRespuestas();

        // identificar sospechosos usando la tabla de frecuencias ->(O(E*R))
        int[] res = identificarSospechosos(vecesRepetidas);

        return res;
    }

    private int[][] contarFrecuenciaDeRespuestas(){
        int[][] vecesRepetidas = new int[R][10];
        for (int i = 0; i < E; i++){ // bucle que recorre E veces
            int[] respuestas = alumnos[i].respuestas;
            for (int j = 0; j < R; j++) { // bucle q recorre R veces
                int resEjercicio = respuestas[j];
                if (resEjercicio != -1) {
                    vecesRepetidas[j][resEjercicio] += 1;
                }
            }
        }       // recorrer E veces un bucle R -> O(E*R)
        return vecesRepetidas;
    }
    private int[] identificarSospechosos(int[][] vecesRepetidas) {
        ArrayList<Integer> listaSospechosxs = new ArrayList<>();

        for (int i = 0; i < E; i++) {// bucle que recorre E veces
            int[] respuestas = alumnos[i].respuestas;
            int resCopiadas = 0;

            for (int j = 0; j < R; j++) { // bucle q recorre R veces
                int resEjercicio = respuestas[j];
                if (resEjercicio != -1) {
                    int apariciones = vecesRepetidas[j][resEjercicio];
                    // criterio de copia: 4 * (apariciones - 1) >= E - 1
                    if (4 * (apariciones - 1) >= (E - 1)) {
                        resCopiadas += 1;
                    }
                }
            }

            // si respondio algo y TODAS sus respuestas son copias -> O(1)
            if (alumnos[i].respondidas > 0 && resCopiadas == alumnos[i].respondidas) {
                alumnos[i].esSospechosx = true;
                listaSospechosxs.add(i);
            }
        }

        // convertir el ArrayList a int[] para la salida
        int[] res = new int[listaSospechosxs.size()];
        for (int k = 0; k < listaSospechosxs.size(); k++) { //O(E) si se copian todos
            res[k] = listaSospechosxs.get(k);
        }
             // recorrer E veces un bucle R -> O(E*R)
        return res;
    }

    // complejida de chequearCopias -> O(E*R)
    // ya que los dos procesos tienen la misma complejidad
}
// aclaracion, tal vez redundante, cuando digo "recorre E veces", "se ejecuta R veces", etc.
// hablamos siempre del "peor caso"

