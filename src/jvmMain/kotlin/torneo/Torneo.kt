package torneo

import kotlin.random.nextInt
import los.illuminati.estructuras.Lista
import los.illuminati.colors.Colors
import usuario.*
import java.lang.Double.min

class Torneo(val usuario: Usuario){

    var candidatos = Lista<Candidatos>() // Lista de candidatos
    var apuesta: Int = 0 // ID de los candidatos a los que se le apostó
    var apostado: Float = 0F // la cantidad que se aposto
    var minCandidato = Candidatos(0,0,0.0,0.0) // el candidato con menor probabilidad de ganar 
    var maxCandidato = Candidatos(0,0,0.0,0.0) // el candidato con mayor probabilidad de ganar
    var candidatoGanador = Candidatos(0,0,0.0,0.0)  // Ganador de la partida 
    var ncandidatos = kotlin.random.Random.nextInt(1 .. 2) // EL número de candidatos puede ser 16 o 32

    init{
        when(ncandidatos){
            1-> ncandidatos = 16
            2-> ncandidatos = 32
        }
        for(i in 0 .. ncandidatos-1){
            candidatos.add(Candidatos(0,i,0.0,0.0))
        }
        mezclaCandidatos()
        partidas()
    }

    /**
     * Método para mezclar los candidatos en la lista
     */
    private fun mezclaCandidatos(){

         val candidatosMezclados = Lista<Candidatos>()

        while(!candidatos.esVacia()){
            val index  = kotlin.random.Random.nextInt(candidatos.getLongitud())
            candidatosMezclados.add(candidatos.get(index))
            candidatos.elimina(candidatos.get(index))
        }
        candidatos = candidatosMezclados.copia()
     }
    /**
     * Método para simular todas las partidas del torneo
     */
    private fun partidas(){
        for(i in 0 .. ncandidatos-2){
            partida(candidatos.get(i), candidatos.get(i+1))
        }
    }

    /**
     * Método para simular un partido entre dos candidatos
     */
    private fun partida(candidatoA : Candidatos, candidatoB : Candidatos){

        candidatoA.probabilidad = candidatoA.habilidad.toDouble() / (candidatoA.habilidad + candidatoB.habilidad)
        candidatoB.probabilidad = candidatoB.habilidad.toDouble() / (candidatoB.habilidad + candidatoA.habilidad)

        candidatoA.cuota =  1.0 / candidatoA.probabilidad
        candidatoB.cuota = 1.0 /candidatoB.probabilidad

        Colors.println("Empieza Partida", Colors.HIGH_INTENSITY)
        Colors.println("Juega el candidato " + candidatoA.id + " vs " + candidatoB.id, Colors.HIGH_INTENSITY)
        Colors.println("--Información de los candidatos--", Colors.HIGH_INTENSITY + Colors.RED)
        Colors.println("Habilidad ->", Colors.HIGH_INTENSITY + Colors.BLUE)
        Colors.println("candidato " + candidatoA.id + " : " + candidatoA.habilidad, Colors.HIGH_INTENSITY)
        Colors.println("candidato " + candidatoB.id + " : "  + candidatoB.habilidad, Colors.HIGH_INTENSITY)
        println("========================")
        Colors.println("Probabilidad ->", Colors.HIGH_INTENSITY + Colors.BLUE)
        Colors.println("candidato " + candidatoA.id + " : " + candidatoA.probabilidad, Colors.HIGH_INTENSITY)
        Colors.println("candidato " + candidatoB.id + " : "  + candidatoB.probabilidad, Colors.HIGH_INTENSITY)
        println("========================")
        Colors.println("Cuotas ->", Colors.HIGH_INTENSITY + Colors.GREEN)
        Colors.println("candidato " + candidatoA.id + " : " + candidatoA.cuota, Colors.HIGH_INTENSITY)
        Colors.println("candidato " + candidatoB.id + " : "  + candidatoB.cuota, Colors.HIGH_INTENSITY )
        println("========================")
        Colors.println("Escribe el id del candidato al que quieres apostar", Colors.HIGH_INTENSITY)
        Colors.println(" Tienes " + " segundos para realizar tu apuesta ", Colors.HIGH_INTENSITY)

        apostar(candidatoA,candidatoB)
        // Aquí se le daría tiempo al usuario para realizar la apuesta

        val ganador = kotlin.random.Random.nextInt(1..100)

        //Saca el jugador con menos probabilidad de ganar
        if(min(candidatoA.probabilidad,candidatoB.probabilidad) == candidatoA.probabilidad){
            minCandidato = candidatoA
            maxCandidato = candidatoB
        }else{
            minCandidato = candidatoB
            maxCandidato = candidatoA
        }

        //Determina al ganador y elimina al perdedor de la lista 
        if(ganador <= minCandidato.probabilidad){
            candidatoGanador = minCandidato
            procesaApuesta() // se procesa la apuesta para el usuario 
            candidatos.elimina(maxCandidato)
        }else{
            candidatoGanador = maxCandidato
            procesaApuesta() // se procesa la apuesta para el usuario 
            candidatos.elimina(minCandidato)
        }
    }

    fun procesaApuesta(){
        if(apuesta == 0) {
            println("Esperamos tu apuesta para la siguiente carrera.")
            return
        }
        println("Procesando apuesta")
        if(apuesta == candidatoGanador.id){
            println("¡Haz Acertado!")
            println("Ha ganado el candidadato ${candidatoGanador.id}")
            println("Su cuota es de ${candidatoGanador.cuota}")
            val ganancia = (apostado * candidatoGanador.cuota)
            println("Por lo tanto al haber apostado $apostado haz ganado $ganancia")
            usuario.saldo = ((usuario.saldo - apostado) + ganancia).toFloat()
            println("Tu nuevo saldo es de ${usuario.saldo}")
            usuario.historial.historial.add(Apuesta(ganada = true, apostado = apostado, ganancia = ganancia.toFloat(), tipo = TipoApuesta.TORNEO))
            println("Apuesta agregada a tu historial")
        } else {
            println("Pare que perdiste 😥")
            println("Ha ganado el candidadato ${candidatoGanador.id} pero apostaste por $apuesta")
            println("Haz perdido $apostado")
            usuario.saldo -= apostado
            println("Ahora tienes un saldo de ${usuario.saldo}")
            usuario.historial.historial.add(Apuesta(ganada = false, apostado = apostado, ganancia = 0F, tipo =  TipoApuesta.TORNEO))
            println("Apuesta agregada a tu historial")
        }
        println("Tu apuesta ha sido procesada.")
        apuesta = 0
        apostado = 0F
    }

    fun apostar(candidatoA : Candidatos, candidatoB : Candidatos){
        while (true){
            if(apuesta != 0){
                println("Estas seguro que quieres sobreescribir la apuesta?[y/N]")
                val sobre = readln()
                if(sobre == "y" || sobre == "Y") {
                    println("Saliendo")
                    return
                }
                else println("Continuando")
            }
            println("¿Cuanto quieres apostar?(-1 para salir)")
            val x = readln()
            try { x.toFloat() } catch (_ : NumberFormatException) { println("Ingresa un Numero valido"); continue }
            if(x.toFloat() < 0){
                println("Saliendo de las apuestas")
                return
            }
            println("¿A quien se lo quieres apostar?")
            val y = readln()
            try { x.toInt() } catch (_ : NumberFormatException) { println("Ingresa un Numero valido"); continue }
            if(y.toInt() != candidatoA.id && y.toInt() != candidatoB.id ){
                println("Tienes que escoger un corredor valido puedes esoger ${candidatoA.id} o ${candidatoB.id}")
                println("Vuelve a apostar")
                continue
            }
            println("Toma en cuenta que tu apuesta es para la siguiente carrera.")
            apostado = x.toFloat()
            apuesta = y.toInt()
            println("Tu apuesta quedo registrada en la carrera.")
            return
        }
    }
}