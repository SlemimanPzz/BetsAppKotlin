package usuario

import kotlinx.serialization.Serializable

@Serializable
enum class TipoApuesta {
    TORNEO,
    CARRERA
}