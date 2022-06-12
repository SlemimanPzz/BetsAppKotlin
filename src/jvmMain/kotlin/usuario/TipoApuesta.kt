package usuario

import kotlinx.serialization.Serializable

@Serializable
enum class TipoApuesta {
    TORNEO,
    CARRERA;

    override fun toString() : String{
        return when(this){
            TORNEO  -> "Torneo"
            CARRERA -> "Carrera"
        }
    }
}