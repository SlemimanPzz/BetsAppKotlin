package usuario

import com.google.common.hash.Hashing
import kotlinx.serialization.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.FileWriter
import java.io.File
import java.io.FileReader
import java.nio.charset.StandardCharsets

/**
 * Función extendida de [String] que regresa el hash SHA-512 en [String] de la misma.
 * Utilizada principalmente en usuario
 * @return Hash SHA-512 de [this]
 */
fun String.hash512() = Hashing.sha512().hashString(this, StandardCharsets.UTF_8).toString()

/**
 * Lee de la consola una contraseña y la devuelve.
 * @return La contraseña suministrada.
 */
fun leeContrasena(): String {
    val prov = System.console().readPassword("Contraseña:")
    println(String(prov))
    return String(prov)
}

/**
 * Usuario de la aplicación
 *
 * @property nombre Nombre del usuario.
 * @property saldo Saldo del usuario.
 * @property historial Historial del usuario.
 * @property hashPassword Hash de la contraseña del usuario.
 */
@Serializable
data class Usuario(val nombre: String, var saldo: Float, val historial: Historial, val hashPassword :String, var ultimaApuesta: Apuesta?){



    companion object {


        /**
         * Inicia sesión de un usuario.
         */
        fun iniciaSesion(nombre : String, password : String): Usuario? {
            val archivo = File("./.Usuarios/$nombre.json")
            if(!archivo.exists()) return null
            try {
                val x = Json.decodeFromString<Usuario>(archivo.readText())
                if(x.hashPassword == password.hash512()) return x
                return null
            } catch ( _: Exception) {
                return null
            }
        }
    }

    fun guardaNuevo(){
    }


    /**
     * Guarda el usuario en el directorio de los usuarios.
     */
    fun guarda(){
        val file = File("./.Usuarios")
        if(!file.exists()){
            println(file.absolutePath)
            println(file.canonicalPath)
            print(file.mkdir())
        }


        val archivoGuarda = FileWriter("./.Usuarios/${this.nombre}.json")
        archivoGuarda.write(Json.encodeToString(this))
        archivoGuarda.close()
    }

    fun agregarSaldo(f : Float) {
        saldo += f
    }

    fun consultaHistorial() {
        TODO("Not yet implemented")
    }
}
