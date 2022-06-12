package carrera

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import usuario.Apuesta
import usuario.TipoApuesta
import usuario.Usuario
import kotlin.random.Random

class Carrera(val numCompetidores : Int, val usuario : Usuario) {
    val corredores = Array(numCompetidores) {Corredor(it, MutableList(0){0}, 0f)}
    private var long = 0
    var apuesta : Int = 0
    var apostado : Float = 0F


    init {
        corredores.shuffle()
        corredores.forEachIndexed() {index, corredor ->  corredor.historial.add(index+1)}
        long += 1
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
        corredores.forEachIndexed() { index, corredor -> corredor.historial.add(index + 1) }
        corredores.forEach() {corredor -> probabilidades(corredor) }
        long+=1
    }

    private fun probabilidades(corredor : Corredor){
        val loDeArriba = (long * (numCompetidores + 1)) - corredor.historial.sum()
        val loDeAbajo = long * ((numCompetidores * (numCompetidores + 1))/2)
        val pc = loDeArriba.toFloat()/loDeAbajo.toFloat()
        corredor.chance = pc
    }

    fun consultaParticipante( id : Int){
        corredores.forEach { if(it.id == id) println(it)}
        println("El competidor no existe")
    }
    private fun procesaApuesta(){
        if(apuesta == 0) {
            println("Esperamos tu apuesta para la siguiente carrera.")
            return
        }
        println("Procesando apuesta")
        if(apuesta == corredores[0].id){
            println("¡Haz Acertado!")
            println("Ha ganado ${corredores[0].id}")
            val cuota = 1/corredores[0].chance
            println("Su cuta es de $cuota")
            val ganancia = apostado * cuota
            println("Por lo tanto al haber apostado $apostado haz ganado $ganancia")
            usuario.saldo = usuario.saldo - apostado + ganancia
            println("Tu nuevo saldo es de ${usuario.saldo}")
            usuario.historial.historial.add(Apuesta(ganada = true, apostado = apostado, ganancia = ganancia, tipo = TipoApuesta.CARRERA))
            println("Apuesta agregada a tu historial")
        } else {
            println("Pare que perdiste 😥")
            println("Ha ganado ${corredores[0].id} pero apastate por $apuesta")
            println("Haz perdido $apostado")
            usuario.saldo -= apostado
            println("Ahora tienes un saldo de ${usuario.saldo}")
            usuario.historial.historial.add(Apuesta(ganada = false, apostado = apostado, ganancia = 0F, tipo =  TipoApuesta.CARRERA))
            println("Apuesta agregada a tu historial")
        }
        println("Tu apuesta ha sido procesada.")
        apuesta = 0
        apostado = 0F
    }

    fun hacerCarrera(){
        println("Las carreras estan empezando.")
        var i = 1
        while(true){
            println("Carrera $i iniciando")
            i+=1
            lightOutAndAwayWeGo()
            println("Carrera terminada, los resultados:")
            corredores.forEachIndexed() {index, corredor -> println("${index+1} .- ${corredor.id}") }
            procesaApuesta()
            println("La siguiente carrera empezara en 10 segundos.")
            Thread.sleep(10000L)
            println("Siguiente===================")
        }
    }
}