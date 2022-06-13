package carrera

import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import kotlinx.coroutines.delay
import usuario.Apuesta
import usuario.TipoApuesta
import usuario.Usuario
import kotlin.random.Random

import androidx.compose.runtime.*


class Carrera(val numCompetidores : Int, val usuario : Usuario, var apuestaCarrera: ApuestaCarrera) {
    val corredores = Array(numCompetidores) {Corredor(it +1, MutableList(0){0}, 0f)}
    var lasWin by mutableStateOf(corredores[0])
    var iterCarrera by mutableStateOf(0)
    var segundosParaSiguiente by mutableStateOf(10)


    fun apuesta1(apostado : Int,cantida : Float){
        apuestaCarrera = apuestaCarrera.copy(apostado, cantida)
    }


    init {
        corredores.shuffle()
        corredores.forEachIndexed() {index, corredor ->  corredor.historial.add(index+1)}
        iterCarrera += 1
        corredores.forEach {probabilidades(it)}
        repeat(5){
            lightOutAndAwayWeGo()
        }
    }

    fun lightOutAndAwayWeGo(){
        corredores.sortBy { corredor ->  corredor.chance}
        val numGanador = Random.nextFloat()
        var ganador : Corredor =corredores.first()
        var acumulado = 0f
        for( x in corredores){
            acumulado += x.chance
            if(acumulado > numGanador){
                ganador = x
                break
            }
        }
        corredores.shuffle()
        val inter = corredores.first()
        val indexWinner = corredores.indexOf(element = ganador)
        corredores[indexWinner] = inter
        corredores[0] = ganador
        lasWin = ganador
        corredores.forEachIndexed() { index, corredor -> corredor.historial.add(index + 1) }
        corredores.forEach() {corredor -> probabilidades(corredor) }
        iterCarrera+=1
    }

    private fun probabilidades(corredor : Corredor){
        val loDeArriba = (iterCarrera * (numCompetidores + 1)) - corredor.historial.sum()
        val loDeAbajo = iterCarrera * ((numCompetidores * (numCompetidores + 1))/2)
        val pc = loDeArriba.toFloat()/loDeAbajo.toFloat()
        corredor.chance = pc
    }

    fun consultaParticipante( id : Int){
        corredores.forEach { if(it.id == id) println(it)}
        println("El competidor no existe")
    }
    private fun procesaApuesta(){
        if(apuestaCarrera.apuesta == 0) {
            println("Esperamos tu apuesta para la siguiente carrera.")
            return
        }
        println("Procesando apuesta")
        if(apuestaCarrera.apuesta == corredores[0].id){
            println("¡Haz Acertado!")
            println("Ha ganado ${corredores[0].id}")
            val cuota = 1/corredores[0].chance
            println("Su cuta es de $cuota")
            val ganancia = apuestaCarrera.apostado * cuota
            println("Por lo tanto al haber apostado ${apuestaCarrera.apostado} haz ganado $ganancia")
            usuario.saldo = usuario.saldo - apuestaCarrera.apostado + ganancia
            println("Tu nuevo saldo es de ${usuario.saldo}")
            usuario.historial.historial.add(Apuesta(ganada = true, apostado = apuestaCarrera.apostado, ganancia = ganancia, tipo = TipoApuesta.CARRERA))
            println("Apuesta agregada a tu historial")
        } else {
            println("Pare que perdiste 😥")
            println("Ha ganado ${corredores[0].id} pero apastate por ${apuestaCarrera.apuesta}")
            println("Haz perdido ${apuestaCarrera.apostado}")
            usuario.saldo -= apuestaCarrera.apostado
            println("Ahora tienes un saldo de ${usuario.saldo}")
            usuario.historial.historial.add(Apuesta(ganada = false, apostado = apuestaCarrera.apostado, ganancia = 0F, tipo =  TipoApuesta.CARRERA))
            println("Apuesta agregada a tu historial")
        }
        println("Tu apuesta ha sido procesada.")
        apuestaCarrera = apuestaCarrera.copy(0, 0F)
    }

    suspend fun hacerCarrera() {
        println("Las carreras estan empezando.")
        var i = 1
        while(true){
            println("Carrera $i iniciando")
            segundosParaSiguiente = 10
            i+=1
            lightOutAndAwayWeGo()
            println("Carrera terminada, los resultados:")
            corredores.forEachIndexed() {index, corredor -> println("${index+1} .- ${corredor.id}") }
            procesaApuesta()
            println("La siguiente carrera empezara en 10 segundos.")
            while (segundosParaSiguiente >= 0){
                segundosParaSiguiente -= 1
                delay(1000L)
            }
            println("Siguiente===================")
        }
    }
}