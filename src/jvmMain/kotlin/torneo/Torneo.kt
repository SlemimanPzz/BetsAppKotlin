package torneo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import carrera.ApuestaPartida
import kotlinx.coroutines.delay
import kotlin.random.nextInt
import usuario.*
import java.lang.Double.min
/**
 * Clase para simular el torneo de 16 o 32 candidatos
 * */
class Torneo(val usuario: Usuario, var apuestaTorneo: ApuestaPartida) {

    /**Lista de candidatos*/
    private var candidatos = mutableListOf<Candidatos>()

    /**Lista de candidatos que pasaron la primera ronda */
    private var candidatosQuePasaron = mutableListOf<Candidatos>()

    /** Candidato con menor habilidad en la partida */
    var minCandidato by mutableStateOf(Candidatos(0, 0, 0.0, 0.0))// el candidato con menor probabilidad de ganar

    /** Candidato con mayor habilidad en la partida */
    var maxCandidato by mutableStateOf(Candidatos(0, 0, 0.0, 0.0)) // el candidato con mayor probabilidad de ganar

    /** Candidato ganador de la partida*/
    private var candidatoGanador by mutableStateOf(Candidatos(0, 0, 0.0, 0.0))  // Ganador de la partida

    /** Random para determinar si tiene 16 o 32 candidatos*/
    private var ncandidatos = kotlin.random.Random.nextInt(1 .. 2) // EL número de candidatos puede ser 16 o 32

    /** Ganador del torneo*/
    var lastWinnner by mutableStateOf(Candidatos(0,0,0.0,0.0))

    /** Total de candidatos en el torneo*/
    var numCandidatos by mutableStateOf(candidatos.size)

    /** Segundos para el siguiente torneo*/
    var segundosParaSiguiente by mutableStateOf(15)

    init{
        when(ncandidatos){
            1-> ncandidatos = 16
            2-> ncandidatos = 32
        }
        for(i in 0 until ncandidatos){
            candidatos.add(Candidatos(0,i,0.0,0.0))
        }
        candidatos.shuffle()
        numCandidatos = candidatos.size
    }

    /**
     * Método para simular todas las partidas del torneo
     */
    suspend fun partidas(){
        while (candidatos.isNotEmpty()){
            if(candidatos.size == 1){
                return
            }
            for(i in 0 until candidatos.size  step 2){
                partida(candidatos.get(i), candidatos.get(i+1))
            }
            candidatos = candidatosQuePasaron
            candidatosQuePasaron = mutableListOf()
        }
    }
    /** Para obtener la apuesta del torneo*/
    fun apuesta(apostado : Int, cantida : Float){
        apuestaTorneo = apuestaTorneo.copy(apostado, cantida)
        println(apuestaTorneo)
    }

    /**
     * Método para simular un partido entre dos candidatos
     */
    suspend fun partida(candidatoA : Candidatos, candidatoB : Candidatos){
        segundosParaSiguiente = 15

        candidatoA.probabilidad = candidatoA.habilidad.toDouble() / (candidatoA.habilidad + candidatoB.habilidad)
        candidatoB.probabilidad = candidatoB.habilidad.toDouble() / (candidatoB.habilidad + candidatoA.habilidad)

        candidatoA.cuota =  1.0 / candidatoA.probabilidad
        candidatoB.cuota = 1.0 /candidatoB.probabilidad


        //Saca el jugador con menos probabilidad de ganar
        if(min(candidatoA.probabilidad,candidatoB.probabilidad) == candidatoA.probabilidad){
            minCandidato = candidatoA
            maxCandidato = candidatoB
        }else{
            minCandidato = candidatoB
            maxCandidato = candidatoA
        }

        while (segundosParaSiguiente >= 0){
            segundosParaSiguiente -= 1
            delay(1000L)
        }
        // Aquí se le daría tiempo al usuario para realizar la apuesta
        val ganador = kotlin.random.Random.nextFloat()



        //Determina al ganador y elimina al perdedor de la lista 
        if(ganador <= minCandidato.probabilidad){
            candidatoGanador = minCandidato
            procesaApuesta() // se procesa la apuesta para el usuario 
            candidatosQuePasaron.add(minCandidato)
        }else{
            candidatoGanador = maxCandidato
            procesaApuesta() // se procesa la apuesta para el usuario 
            candidatosQuePasaron.add(maxCandidato)
        }
        lastWinnner = candidatoGanador
        numCandidatos = candidatos.size - candidatosQuePasaron.size
    }

    fun procesaApuesta(){
        if(apuestaTorneo.apuesta == -1) {
            println("Esperamos tu apuesta para la siguiente carrera.")
            return
        }
        println("Procesando apuesta")
        if(apuestaTorneo.apuesta == candidatoGanador.id){
            println("¡Haz Acertado!")
            println("Ha ganado el candidadato ${candidatoGanador.id}")
            println("Su cuota es de ${candidatoGanador.cuota}")
            val ganancia = (apuestaTorneo.apostado * candidatoGanador.cuota)
            println("Por lo tanto al haber apostado ${apuestaTorneo.apostado} haz ganado $ganancia")
            usuario.saldo = ((usuario.saldo - apuestaTorneo.apostado) + ganancia).toFloat()
            println("Tu nuevo saldo es de ${usuario.saldo}")
            usuario.historial.historial.add(Apuesta(ganada = true, apostado = apuestaTorneo.apostado, ganancia = ganancia.toFloat(), tipo = TipoApuesta.TORNEO))
            println("Apuesta agregada a tu historial")
        } else {
            println("Pare que perdiste 😥")
            println("Ha ganado el candidadato ${candidatoGanador.id} pero apostaste por ${apuestaTorneo.apuesta}")
            println("Haz perdido ${apuestaTorneo.apostado}")
            usuario.saldo -= apuestaTorneo.apostado
            println("Ahora tienes un saldo de ${usuario.saldo}")
            usuario.historial.historial.add(Apuesta(ganada = false, apostado = apuestaTorneo.apostado, ganancia = 0F, tipo =  TipoApuesta.TORNEO))
            println("Apuesta agregada a tu historial")
        }
        println("Tu apuesta ha sido procesada.")
        apuestaTorneo = apuestaTorneo.copy(-1, 0F)
        usuario.ultimaApuesta = usuario.historial.historial.last()
    }

}