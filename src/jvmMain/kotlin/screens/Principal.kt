package screens

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import carrera.Carrera
import navControl.NavController
import usuario.Apuesta
import usuario.Historial
import usuario.TipoApuesta
import usuario.Usuario
import kotlin.system.exitProcess

enum class Ventanas {
    NADA,
    CONSUTAUSUARIO,
    CONSULTAHISTORIAL,
}




@Composable
fun int(i : Int){
    Row {
        Text("$i")
    }
}


@Composable
fun consultaUsuario(usuario: Usuario?, popup: MutableState<Ventanas>){
    val saldo  = remember { mutableStateOf(usuario?.saldo) }
    Dialog(onCloseRequest = {popup.value = Ventanas.NADA}, title = "Consulta Usuario", ) {
        if (usuario != null) {
            Column(verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                Text("Eres ${usuario.nombre}", fontSize = 30.sp)
                Text("Tienes ${saldo.value} saldo", fontSize = 30.sp)
                Row (verticalAlignment = Alignment.Bottom){
                    OutlinedButton(onClick = {popup.value = Ventanas.NADA}){
                        Text("Ya termine")
                    }
                    OutlinedButton(onClick = {popup.value = Ventanas.CONSULTAHISTORIAL}){
                        Text("Vamos al historial")
                    }
                }
            }

        }
    }
}

@Composable
fun consultaHistorial(historial: Historial, popup: MutableState<Ventanas>) {
    Dialog(onCloseRequest = { popup.value = Ventanas.NADA }) {
        historial.historial.add(Apuesta(ganada = false, apostado = 90F, ganancia = 0F, tipo = TipoApuesta.TORNEO))
        if (historial.historial.size == 0) {
            Text("Aun no tienes historial, apuesta para ganar 😈")
        } else {
            val hist = remember { mutableStateListOf<Apuesta>() }


            historial.historial.forEach() {
                hist.add(it)
            }


            val estado = rememberLazyListState()

            LazyColumn {
                itemsIndexed(hist) { index, it ->
                    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxSize()) {
                        if (it.ganada) {
                            Text("Ganaste la apuesta $index", color = Color.Green)
                            Text("Apostaste en ${it.tipo}")
                            Text("Apostaste ${it.apostado}")
                            Text("Obtuviste una ganancia de ${it.ganancia}")
                        } else {
                            Text("Perdiste la apuesta $index", color = Color.Red)
                            Text("Apostaste en ${it.tipo}")
                            Text("Apostaste ${it.apostado}")
                        }
                    }
                }
            }

            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(
                    scrollState = estado
                )
            )
        }
    }
}

    @Composable
    fun Principal(navController: NavController) {

        val arr = remember { mutableStateListOf<Int>() }
        arr.add(0)

        val ventana = remember { mutableStateOf(Ventanas.NADA) }

        val estado = rememberLazyListState()

        val race = navController.usr?.let { Carrera(6, usuario = it) }
        val carrera = remember { mutableStateOf(race) }

        when (ventana.value) {
            Ventanas.NADA -> {}
            Ventanas.CONSUTAUSUARIO -> consultaUsuario(navController.usr, ventana)
            Ventanas.CONSULTAHISTORIAL -> consultaHistorial(navController.usr!!.historial, ventana)
            else -> {}
        }

        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(12.dp)
        ) {

            items(items = arr) {
                int(it)
                Spacer(Modifier.padding(10.dp))
            }
        }

        Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxHeight()) {
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(
                    scrollState = estado
                )
            )
        }

        BottomAppBar {
            Box {
                IconButton(onClick = { exitProcess(0) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            OutlinedButton(onClick = {
                ventana.value = Ventanas.CONSUTAUSUARIO
            }) {
                Text("Consulta tu Usuario")
            }

            Spacer(modifier = Modifier.padding(8.dp))

            OutlinedButton(onClick = { ventana.value = Ventanas.CONSULTAHISTORIAL }) {
                Text("Consulta Historial")
            }

            Spacer(Modifier.padding(8.dp))

            OutlinedButton(onClick = { arr.add(arr.last() + 1) }) {
                Text("Agrega ${arr.last() + 1}")
            }

            Spacer(Modifier.padding(8.dp))

            OutlinedButton(onClick = { arr.removeLast() }) {
                when (arr.size) {
                    0 -> Text("Si le picas va a dar error, estas avisado.")
                    else -> Text("Elimina ${arr.last()}")
                }
            }

        }
    }