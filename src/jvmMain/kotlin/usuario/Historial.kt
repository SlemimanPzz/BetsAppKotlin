package usuario

import kotlinx.serialization.*

/**
 * Historial del usuario.
 */
@Serializable
data class Historial(val historial : MutableList<Apuesta>)
