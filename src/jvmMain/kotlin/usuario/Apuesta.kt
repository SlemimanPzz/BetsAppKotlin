package usuario

import kotlinx.serialization.Serializable

@Serializable
data class Apuesta(val ganada  : Boolean, val apostado : Float, val ganancia: Float, val tipo : TipoApuesta)