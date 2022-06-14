package screens

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import carrera.ApuestaPartida
import carrera.Carrera
import carrera.Corredor
import navControl.NavController
import torneo.Torneo
import usuario.Historial
import usuario.Usuario
import kotlin.system.exitProcess

enum class Ventanas {
    NADA,
    CONSUTAUSUARIO,
    CONSULTAHISTORIAL,
    CONSULTAHISTORIALCARRERA,
}




@Composable
fun consutlaCorredores(corredores : Array<Corredor>, popup: MutableState<Ventanas>){
    Dialog(onCloseRequest = {popup.value = Ventanas.NADA}, title = "Corredores", state = DialogState(size = DpSize(1000.dp,500.dp))){
        val estado = rememberLazyListState()

        val corredores2 by remember { mutableStateOf(corredores) }




        /*Row (modifier = Modifier.fillMaxSize()){
            corredores2.forEach {
                Text("Corredor ${it.id} con ${it.chance.times(100)} %")
                LazyColumn {
                    itemsIndexed(it.historial){index , it ->
                        Row {
                            Text("En ${index.plus(1)} quede $it")
                        }
                    }
                }
            }
        }*/
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly) {
            corredores2.forEachIndexed { index, it ->
                Column {
                    Text("Corredor ${it.id}")
                    Text("Con ${it.chance.times(100)} %")

                    Spacer(modifier = Modifier.padding(8.dp))
                    Column {
                        it.historial.forEachIndexed { index , it ->
                            Text("En la carrera ${index + 1} : $it°")
                        }
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
    Dialog(onCloseRequest = { popup.value = Ventanas.NADA }, title = "Tu historial", state = DialogState(size = DpSize(750.dp, 500.dp))) {

        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(onClick = {popup.value = Ventanas.NADA}) { Text("Cerrar")}
                OutlinedButton(onClick = {popup.value = Ventanas.CONSUTAUSUARIO}) { Text("Ir a Usuario")}
                Spacer(modifier = Modifier.height(30.dp))

            }
            Row (verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center){
                if (historial.historial.size == 0) {
                    Text("Aun no tienes historial, apuesta para ganar 😈")
                } else {
                    val estado = rememberLazyListState()
                    LazyColumn {
                        itemsIndexed(historial.historial) { index, it ->
                            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxSize()
                            ) {
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
                            Spacer(modifier = Modifier.padding(8.dp))
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


    }
}

@Composable
fun Principal(navController: NavController) {


    val ventana = remember { mutableStateOf(Ventanas.NADA) }

    var apuestaCarrera by mutableStateOf(ApuestaPartida(0, 0F))
    var apuestaTorneo by mutableStateOf(ApuestaPartida(-1, 0F))

    var torneo by remember { mutableStateOf(navController.usr?.let { Torneo(it, apuestaTorneo) }) }
    val race = navController.usr?.let { Carrera(6, usuario = it, apuestaCarrera) }

    val carrera by remember { mutableStateOf(race) }


    //Dialogos
    when (ventana.value) {
        Ventanas.NADA -> {}
        Ventanas.CONSUTAUSUARIO -> consultaUsuario(navController.usr, ventana)
        Ventanas.CONSULTAHISTORIAL -> consultaHistorial(navController.usr!!.historial, ventana)
        Ventanas.CONSULTAHISTORIALCARRERA -> if (race != null) {
            carrera?.let { consutlaCorredores(it.corredores, ventana) }
        }
        else -> {}
    }


    Row {


        //Barra Lateral
        Column(horizontalAlignment = Alignment.Start) {
            NavigationRail {
                Box {
                    IconButton(onClick = { navController.usr?.guarda(); exitProcess(0) }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))

                IconButton(onClick = {
                    ventana.value = Ventanas.CONSUTAUSUARIO
                }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.padding(8.dp))

                OutlinedButton(onClick = { ventana.value = Ventanas.CONSULTAHISTORIAL }) {
                    Text("Historial")
                }

                var saldoExtra by remember { mutableStateOf(TextFieldValue("0")) }
                var textoBoton by remember { mutableStateOf("💰 Agregar 💲") }
                OutlinedButton(
                    onClick = {
                        if (esErrorFloat(saldoExtra.text)) {
                            textoBoton = "Error"
                            return@OutlinedButton
                        } else {
                            navController.usr?.agregarSaldo(saldoExtra.text.toFloat())
                        }
                    }
                ) {
                    Text(textoBoton)
                }
                OutlinedTextField(
                    value = saldoExtra,
                    onValueChange = { saldoExtra = it },
                    label = { Text("Agrega Saldo") },
                    placeholder = { Text("Saldo") },
                    modifier = Modifier.width(80.dp),
                    isError = esErrorFloat(saldoExtra.text)
                )
            }


            }


            //Apuesta y Carrera
            Column {

                //La carrera
                Row(
                    modifier = Modifier.padding(40.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column {

                        Text("La carrera de 6 corredores, Iteración ${carrera?.iterCarrera?.minus(6)}")

                        var apuesta by remember { mutableStateOf(TextFieldValue("0")) }
                        var porra by remember { mutableStateOf(TextFieldValue("0")) }

                        Row {
                            //Recibe Saldo
                            OutlinedTextField(
                                value = apuesta,
                                onValueChange = { newValue ->
                                    run {
                                        apuesta = newValue;
                                    }
                                },
                                textStyle = TextStyle(fontFamily = FontFamily.Monospace),
                                label = { Text("💸 Tu apuesta 💸") },
                                placeholder = { Text("El saldo que vas a apostar") },
                                singleLine = true,
                                isError = esErrorFloat(apuesta.text),
                                modifier = Modifier.width(120.dp)
                            )

                            OutlinedTextField(
                                value = porra,
                                onValueChange = { newValue ->
                                    run {
                                        porra = newValue;
                                    }
                                },
                                textStyle = TextStyle(fontFamily = FontFamily.Monospace),
                                label = { Text("A quien le vas ") },
                                placeholder = { Text("Elige 1 competidor del 1 al 6") },
                                singleLine = true,
                                isError = esErrorInt(porra.text),
                                modifier = Modifier.width(120.dp)
                            )


                        }

                        Text("Tu apuesta se procesa en la siguiente carrera")

                        Row {
                            var textoBoton by remember { mutableStateOf("Apuesta") }
                            Column {
                                OutlinedButton(onClick = {
                                    textoBoton = if (!esErrorFloat(apuesta.text)) {

                                        if (navController.usr?.saldo!! < apuesta.text.toFloat()) {
                                            textoBoton = "Saldo Insuficiente"
                                            return@OutlinedButton
                                        }
                                        if (!esErrorInt(porra.text)) {
                                            val apostado = porra.text.toInt()
                                            if (apostado in 1..6) {
                                                carrera?.apuesta(apostado, apuesta.text.toFloat())
                                            }
                                            textoBoton = "Competidor Invalido"
                                            return@OutlinedButton
                                        } else {
                                            "Competidor Invalido"
                                        }
                                    } else {
                                        "Ingresa Saldo Valido"
                                    }
                                }) {
                                    Text(textoBoton)
                                }
                            }

                            OutlinedButton(onClick = { ventana.value = Ventanas.CONSULTAHISTORIALCARRERA }) {
                                Text("Consulta")
                            }
                        }
                    }


                    //Informacion Carrera
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        OutlinedTextField(
                            value = when (carrera?.apuestaCarrera?.apuesta) {
                                0 -> "Aun no has apostado"
                                else -> "A competidor ${carrera?.apuestaCarrera?.apuesta}, $ ${carrera?.apuestaCarrera?.apostado}"
                            },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Ultima apuesta") },
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = "Competidor ${carrera?.lasWin?.id} con ${carrera?.lasWin?.chance?.times(100)} %",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Ultimo Ganador") },
                            singleLine = true
                        )

                        Text("Segundas para la siguiente carrera ${carrera?.segundosParaSiguiente?.plus(1)} s")
                        LaunchedEffect(0) {
                            carrera?.hacerCarrera()
                        }
                    }

                }


                //El torneo
                Row(
                    modifier = Modifier.padding(40.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column {
                        Text("El torneo, actualmente ${torneo?.numCandidatos} participantes")

                        var porra by remember { mutableStateOf(TextFieldValue("0")) }
                        var apostado by remember { mutableStateOf(TextFieldValue("0")) }

                        Row {
                            OutlinedTextField(
                                value = apostado,
                                onValueChange = { apostado = it },
                                textStyle = TextStyle(fontFamily = FontFamily.Monospace),
                                label = { Text("💸 Tu apuesta 💸") },
                                placeholder = { Text("El saldo que vas a apostar") },
                                singleLine = true,
                                isError = esErrorInt(apostado.text),
                                modifier = Modifier.width(120.dp),
                            )

                            OutlinedTextField(
                                value = porra,
                                onValueChange = { porra = it },
                                textStyle = TextStyle(fontFamily = FontFamily.Monospace),
                                label = { Text("A quien le vas") },
                                placeholder = { Text("El saldo que vas a apostar") },
                                singleLine = true,
                                isError = esErrorInt(porra.text),
                                modifier = Modifier.width(120.dp)
                            )


                        }

                        Text("Estas apostando para el ganador de la partida acutal.")


                        Row {
                            var textoBoton by remember { mutableStateOf("Apuesta") }
                            OutlinedButton(onClick = {
                                //Acepta la apuesta
                                textoBoton = if (!esErrorFloat(apostado.text)) {

                                    if (navController.usr?.saldo!! < apostado.text.toFloat()) {
                                        textoBoton = "Saldo Insuficiente"
                                        return@OutlinedButton
                                    }
                                    if (!esErrorInt(porra.text)) {
                                        val candidatoApostado = porra.text.toInt()
                                        if (candidatoApostado == torneo?.maxCandidato?.id || candidatoApostado == torneo?.minCandidato?.id) {
                                            torneo?.apuesta(candidatoApostado, apostado.text.toFloat())
                                        }
                                        textoBoton = "Competidor Invalido"
                                        return@OutlinedButton
                                    } else {
                                        "Competidor Invalido"
                                    }
                                } else {
                                    "Ingresa Saldo Valido"
                                }


                            }) {
                                Text(textoBoton)
                            }
                        }
                    }


                    //Info Torneo
                    Column {
                        Text("Segundos para siguiente ${torneo?.segundosParaSiguinte?.plus(1)}")
                        OutlinedTextField(
                            value = when (torneo?.apuestaTorneo?.apuesta) {
                                -1 -> "Aun no has apostado"
                                else -> "Ultima apauesta a ${torneo?.apuestaTorneo?.apuesta}, ${torneo?.apuestaTorneo?.apostado} "
                            },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Ultima Apuesta") },
                            singleLine = true,
                            placeholder = { TextFieldValue("Aun no has apostado.") }
                        )

                        OutlinedTextField(
                            value = "Competidor ${torneo?.lastWinnner?.id} con ${torneo?.lastWinnner?.habilidad} habilidad",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Ultimo Ganador") },
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = "${torneo?.maxCandidato?.id}(Lvl ${torneo?.maxCandidato?.habilidad}) vs ${torneo?.minCandidato?.id}(Lvl ${torneo?.minCandidato?.habilidad})\n" +
                                    "Cuota ${torneo?.maxCandidato?.id} : %2.2f vs Cuota ${torneo?.minCandidato?.id} : %2.2f".format(
                                        torneo?.maxCandidato?.cuota,
                                        torneo?.minCandidato?.cuota
                                    ),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Partida Actual") },
                            textStyle = TextStyle(textAlign = TextAlign.Center, fontFamily = FontFamily.Monospace)
                        )
                    }
                    LaunchedEffect(1) {
                        while (true) {
                            torneo?.partidas()
                            torneo = navController.usr?.let { Torneo(it, apuestaTorneo) }
                        }
                    }


                }



                Row(horizontalArrangement = Arrangement.Center) {
                    var texto by remember { mutableStateOf("Aun no has apostado") }
                    when (navController.usr?.ultimaApuesta?.ganada) {
                        true -> texto =
                            "Ultima Apuesta : Has gando, apostate ${navController.usr!!.ultimaApuesta?.apostado} y ganaste ${navController.usr!!.ultimaApuesta?.ganancia} en ${navController.usr!!.ultimaApuesta?.tipo}"
                        false -> texto =
                            "Ultima Apuesta : Apostaste en ${navController.usr!!.ultimaApuesta?.tipo} y perdiste ${navController.usr!!.ultimaApuesta?.apostado}"
                        else -> {}
                    }
                    Text(texto, textAlign = TextAlign.Center)
                }
            }

    }
}


