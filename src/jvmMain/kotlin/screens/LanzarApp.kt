package screens

import Screen
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import navControl.NavController
import usuario.Historial
import usuario.Usuario
import kotlin.system.exitProcess

@Composable
@Preview
fun LanzarApp(navController: NavController){
    Row(Modifier.fillMaxSize().padding(30.dp), horizontalArrangement = Arrangement.Center, ){
        Text(text = "Bienvenido a apuestas Los Illuminati", textAlign = TextAlign.Center, fontStyle = FontStyle.Italic, fontSize = 24.sp)
    }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            OutlinedButton(onClick = {navController.navigate(Screen.InciaSesion.name)}) {
                Text("Inicia Sesión")
            }
            OutlinedButton(onClick = {navController.navigate(Screen.CreaCuenta.name)}) {
                Text("Crea una Cuenta")
            }
            OutlinedButton(onClick = {
                navController.usr = Usuario(nombre = "Invitado", saldo = 1000F, historial = Historial(mutableListOf()), "")
                navController.navigate(Screen.Principal.name)
            }) {
                Text("Modo Invitado")
            }
        }


    Box{
        IconButton(onClick = { exitProcess(0)}){
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null
            )
        }
    }

    }


