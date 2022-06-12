package screens

import Screen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.common.hash.Hashing
import navControl.NavController
import usuario.Usuario
import java.nio.charset.StandardCharsets

fun String.hash512() = Hashing.sha512().hashString(this, StandardCharsets.UTF_8).toString()

@Composable
fun IniciaSesion(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var name by remember { mutableStateOf(TextFieldValue("")) }
        var password by remember { mutableStateOf(TextFieldValue("")) }
        val nameDisplay by remember { mutableStateOf("Aun no me das tu nombre.") }
        val passDisplay by remember { mutableStateOf("Aun no me das tu contraseña") }
        var pop by remember { mutableStateOf(-1) }

        Text( text = "Inicia sesión")




        when(pop){
            0 -> {
                Dialog(onCloseRequest = { pop = -1}, title = "Info Inicio de Sesión") {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Credenciales incorrectas")
                    }
                }
            }
            1 -> {
                Dialog(onCloseRequest = { navController.navigate(Screen.Principal.name) }, title = "Info Inicio de Sesión") {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Bienvenido ${navController.usr?.nombre}")
                        Text("Cierra esta ventana para que inicies sesión.")
                    }
                }
            }
            else -> {}
        }

        OutlinedTextField(
            value = name,
            onValueChange = { newValue -> name = newValue},
            modifier = Modifier.padding(8.dp),
            textStyle = TextStyle(fontFamily = FontFamily.Monospace),
            label = { Text("Nombre") },
            placeholder = { Text("tu nombre") },
            singleLine = true,
            isError = when(name.text.trim()){
                "" -> true
                else -> false
            }
        )

        OutlinedTextField(
            value = password,
            modifier = Modifier.padding(8.dp),
            label = { Text("Password:") },
            placeholder = { Text("tu contraseña") },
            textStyle = TextStyle(fontFamily = FontFamily.Monospace),
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { password = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            isError = when(password.text.trim()){
                "" -> true
                else -> false
            }
        )

        Text(
            text = when(nameDisplay.trim()){
                    "" -> "Aun no me das tu nombre"
                    else -> nameDisplay
                }
        )
        Text(
            text = when(passDisplay.trim()){
                "" -> "Aun no me das tu nombre"
                else -> passDisplay
            }
        )

        OutlinedButton(
            onClick = {
                val usr : Usuario? = Usuario.iniciaSesion(name.text, password.text)
                if(usr == null){
                    pop = 0
                    return@OutlinedButton
                } else {
                    navController.usr = usr
                    pop = 1
                }
            },
        ){
            Text(text = "Ingresar")
        }

    }
    Box(){
        IconButton( onClick = {navController.navigate(Screen.LanzarApp.name)}){
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null
            )
        }
    }
}