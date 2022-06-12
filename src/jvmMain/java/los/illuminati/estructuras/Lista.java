package los.illuminati.estructuras;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para listas genéricas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas implementan la interfaz {@link Iterable}, y por lo tanto se
 * pueden recorrer usando la estructura de control <em>for-each</em>. Las listas
 * no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Iterable<T> {



    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /* Clase Iterador privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nuevo iterador. */
        private Iterador() {
            siguiente = cabeza;
            anterior = null;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if(hasNext()){
                anterior = siguiente;
                siguiente = siguiente.siguiente;
                return anterior.elemento;
            }
            else {throw new NoSuchElementException();}
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if(hasPrevious()){
                siguiente = anterior;
                anterior = anterior.anterior;
                return siguiente.elemento;
            }
            else{throw new NoSuchElementException();}
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            siguiente = cabeza;
            anterior = null;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            anterior = rabo;
            siguiente = null;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    public boolean esVacia() {
        return cabeza == null;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        if ( elemento == null){
            throw new IllegalArgumentException();
        }
        Nodo n = new Nodo(elemento);
        if(rabo == null){
            cabeza = rabo = n;
        }
        else{
            rabo.siguiente = n;
            n.anterior = rabo;
            rabo = n;
        }
        longitud++;
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        if(elemento == null) throw new IllegalArgumentException();
        Nodo n = new Nodo(elemento);
        if( rabo== null) cabeza = rabo = n;
        else {
            cabeza.anterior= n;
            n.siguiente = cabeza;
            cabeza = n;
        }
        longitud++;
    }

    private Nodo buscarNodo(int i){
        Nodo ind = cabeza;
        int contador = 0;
        while(ind != null){
            if( contador == i) return ind;
            ind = ind.siguiente;
            contador++;
        }
        return null;
    }
    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        if (elemento == null){throw new IllegalArgumentException();}
        if ( i <= 0){
            agregaInicio(elemento);
            return;
        }
        if ( i >= longitud){
            agregaFinal(elemento);
            return;
        }
        Nodo n = new Nodo(elemento);
        Nodo siguienteDeInserta = buscarNodo(i);
        Nodo anteriorDeInserta = siguienteDeInserta.anterior;
        longitud++;
        n.anterior = anteriorDeInserta;
        anteriorDeInserta.siguiente = n;
        n.siguiente = siguienteDeInserta;
        siguienteDeInserta.anterior = n;
    }

    public void add(T elemento) {
        agregaFinal(elemento);
    }


    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    public void elimina(T elemento) {
        if(elemento == null){
            return;
        }
        int indice = indiceDe(elemento);
        if ( indice < 0 || indice >= longitud){
            return;
        }
        if ( indice == 0){
            eliminaPrimero();
            return;
        }
        if ( indice == longitud - 1){
            eliminaUltimo();
            return;
        }
        Nodo eliminado = buscarNodo(indice);
        Nodo anteriorDelEliminado = eliminado.anterior;
        Nodo siguienteDeEliminado = eliminado.siguiente;
        anteriorDelEliminado.siguiente = siguienteDeEliminado;
        siguienteDeEliminado.anterior = anteriorDelEliminado;
        longitud--;
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     *
     * @throws NoSuchElementException si la lista es vacía.
     */
    public void eliminaPrimero() {
        if (cabeza == null){
            throw new NoSuchElementException();
        }
        if (longitud == 1){
            T eliminado = cabeza.elemento;
            cabeza.elemento = null;
            rabo = cabeza = null;
            longitud = 0;
            return;
        }
        Nodo ahoraSeraElPrimero = cabeza.siguiente;
        Nodo elEliminado = cabeza;
        ahoraSeraElPrimero.anterior = null;
        cabeza = ahoraSeraElPrimero;
        longitud--;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     *
     * @throws NoSuchElementException si la lista es vacía.
     */
    public void eliminaUltimo() {
        if(rabo == null){
            throw new NoSuchElementException();
        }
        if ( longitud ==1){
            T eliminado = rabo.elemento;
            cabeza.elemento = null;
            rabo = cabeza = null;
            longitud--;
            return;
        }
        Nodo ahoraSeraElUltimo = rabo.anterior;
        Nodo elEliminado = rabo;
        ahoraSeraElUltimo.siguiente = null;
        rabo = ahoraSeraElUltimo;
        longitud--;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean contiene(T elemento) {
        Nodo n = cabeza;
        while( n!= null){
            if (n.elemento.equals(elemento)){
                return true;
            }
            n = n.siguiente;
        }
        return false;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        Lista<T> delrevez = new Lista<T>();
        Nodo agregado = rabo;
        while ( agregado != null){
            delrevez.agregaFinal(agregado.elemento);
            agregado = agregado.anterior;
        }
        return delrevez;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> copia = new Lista<T>();
        Nodo agregado = cabeza;
        while ( agregado != null){
            copia.agregaFinal(agregado.elemento);
            agregado = agregado.siguiente;
        }
        return copia;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    public void limpia() {
        cabeza = null;
        rabo = null;
        longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        if (esVacia()) throw new NoSuchElementException();
        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        if (esVacia()) throw new NoSuchElementException();
        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        if ( i < 0 || i >=  longitud) throw new ExcepcionIndiceInvalido();
        return buscarNodo(i).elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        Nodo ind = cabeza;
        int indice = 0;
        while ( ind != null){
            if(ind.elemento.equals(elemento)){
                return indice;
            }
            ind = ind.siguiente;
            indice++;
        }
        return -1;
    }


    private String auxString(String s, Nodo n){
        if(n != rabo){
            s += String.format("%s, ",n.elemento);
            return auxString(s, n.siguiente);
        }
        return s;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        if (esVacia()) return "[]";
        String s = "[";
        s = auxString( s , cabeza);
        s += String.format("%s]", rabo.elemento);
        return s;
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        if ( getLongitud() != lista.getLongitud()){
            return false;
        }
        Nodo n = cabeza;
        Nodo m = lista.cabeza;
        while ( n!= null){
            if ( !( n.elemento.equals(m.elemento))){
                return false;
            }
            n = n.siguiente;
            m = m.siguiente;
        }
        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    private int cuentanodos(){
        int a = 0;
        Nodo m = this.cabeza;
        while(m != null){
            a++;
            m = m.siguiente;
        }
        return a;
    }
    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
        int i = cuentanodos();
        if(i <= 1) return this.copia();
        int m =  i / 2;

        Lista<T> lista1 = this.copia();
        lista1.rabo = lista1.buscarNodo(m-1);
        lista1.rabo.siguiente = null;

        Lista<T> lista2 = this.copia();
        lista2.cabeza = lista2.buscarNodo(m);
        lista2.cabeza.anterior = null;

        lista1 = lista1.mergeSort(comparador);
        lista2 = lista2.mergeSort(comparador);
        Lista<T> ordendada = lista1.mezcla(lista1 ,lista2, comparador);
        return ordendada;
    }

    private Lista<T> mezcla(Lista<T> l1, Lista<T> l2, Comparator<T> comprarador){
        Lista<T> listaomerge = new Lista<T>();
        Nodo i = l1.cabeza;
        Nodo j = l2.cabeza;
        while(i != null && j != null){
            if(comprarador.compare(i.elemento, j.elemento) <= 0){
                listaomerge.agregaFinal(i.elemento);
                i = i.siguiente;
            }
            else{
                listaomerge.agregaFinal(j.elemento);
                j = j.siguiente;
            }
        }
        while(i != null){
            listaomerge.agregaFinal(i.elemento);
            i = i.siguiente;
        }
        while(j != null){
            listaomerge.agregaFinal(j.elemento);
            j = j.siguiente;
        }
        return listaomerge;
    }


    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        if (elemento == null) return false;
        if (esVacia()) return false;
        Nodo n = cabeza;
        while(n != null){
            if(comparador.compare(elemento, n.elemento) == 0) return true;
            n = n.siguiente;
        }
        return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}