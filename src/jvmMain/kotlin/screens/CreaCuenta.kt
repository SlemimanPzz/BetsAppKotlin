package screens

import App
import Screen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.delay
import navControl.NavController
import usuario.Historial
import usuario.Usuario


fun valida(s : String): Boolean {
    return try{
        s.toFloat()
        false
    } catch ( _ : NumberFormatException){
        true
    }
}


@Composable
fun CreaCuenta( navController: NavController ) {

    val pop = remember { mutableStateOf(0) }


    when (pop.value) {
        0 -> {}
        1 -> {
            Dialog(onCloseRequest = {
                navController.navigate(Screen.LanzarApp.name)
            }) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Datos ingresados correctamente")
                    Text("Cierra esta ventana para que inicies sesion.")
                }
            }
        }
        2 -> {
            Dialog(onCloseRequest = { pop.value = 0 }) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { Text("Ingresa el saldo correctamente ") }
            }
        }
        3 -> {
            Dialog(onCloseRequest = { pop.value = 0 }) {
                // Draw a rectangle shape with rounded corners inside the dialog
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { Text("Tus contraseñas no coinciden") }
            }
        }
        else -> {}
    }


    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var name by remember { mutableStateOf(TextFieldValue("")) }
        var password by remember { mutableStateOf(TextFieldValue("")) }
        var passowrdConfirm by remember { mutableStateOf(TextFieldValue("")) }
        var saldo by remember { mutableStateOf(TextFieldValue("")) }
        var dinero by remember { mutableStateOf(0F) }

        Text(
            text = "Crea Usuario",
            textAlign = TextAlign.Center,
        )

        OutlinedTextField(
            value = name,
            onValueChange = { newValue -> name = newValue },
            modifier = Modifier.padding(8.dp),
            textStyle = TextStyle(fontFamily = FontFamily.Monospace),
            label = { Text("Nombre:") },
            placeholder = { Text("tu nombre") },
            singleLine = true,
            isError = when (name.text.trim()) {
                "" -> true
                else -> false
            }

        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            Modifier.padding(8.dp),
            textStyle = TextStyle(fontFamily = FontFamily.Monospace),
            label = { Text("Contraseña:") },
            placeholder = { Text("tu contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            isError = when (password.text.trim()) {
                "" -> true
                else -> false
            }
        )
        OutlinedTextField(
            value = passowrdConfirm,
            onValueChange = { passowrdConfirm = it },
            modifier = Modifier.padding(8.dp),
            textStyle = TextStyle(fontFamily = FontFamily.Monospace),
            label = { Text("Confirma tu contraseña:") },
            placeholder = { Text("confrima tu contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            isError = when (passowrdConfirm.text.trim()) {
                "" -> true
                else -> false
            }
        )
        OutlinedTextField(
            value = saldo,
            onValueChange = {
                    newSaldo -> saldo = newSaldo
                try {
                    dinero = saldo.text.toFloat()
                } catch (_ : NumberFormatException){

                }
                            },
            modifier = Modifier.padding(8.dp),
            textStyle = TextStyle(fontFamily = FontFamily.Monospace),
            label = { Text("Saldo") },
            placeholder = { Text("tu saldo inicial") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, autoCorrect = true),
            singleLine = true,
            isError = valida(saldo.text)
        )



        OutlinedButton(
            onClick = {
                var i : Float = -1F
                try {
                    i = saldo.text.toFloat()
                } catch ( _ : NumberFormatException) {
                    pop.value = 2
                }
                if (password != passowrdConfirm) pop.value = 3
                else{
                    pop.value = 1
                    if(i == -1F) pop.value = 2
                    val usr = Usuario(
                        nombre = name.text,
                        saldo = i,
                        historial = Historial(mutableListOf()),
                        password.text.hash512(),
                        null
                    )
                    println("Crea $password")
                    usr.guarda()
                }
            }
        ) {
            Text("Crear Usuario")
        }
    }
    Box {
        IconButton(onClick = { navController.navigate(Screen.LanzarApp.name) }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null
            )
        }
    }
}

