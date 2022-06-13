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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import carrera.ApuestaCarrera
import carrera.Carrera
import carrera.Corredor
import navControl.NavController
import usuario.Historial
import usuario.Usuario
import kotlin.system.exitProcess

enum class Ventanas {
    NADA,
    CONSUTAUSUARIO,
    CONSULTAHISTORIAL,
    APOSTARCARRERA,
    CONSULTAHISTORIALCARRERA
}



@Composable
fun apuesta(carrera: Carrera, popup: MutableState<Ventanas>){
   var expandirSeleccion by remember { mutableStateOf(false) }
    val corredores = IntArray(6){it +1}
    var mSelectedText by remember { mutableStateOf("") }

    val icono = when(expandirSeleccion){
        true -> Icons.Default.KeyboardArrowDown
        false -> Icons.Default.KeyboardArrowUp
    }


   Dialog(
       onCloseRequest = {popup.value = Ventanas.NADA},
       title = "Apostar"
   ) {
       Column {
           IntArray(6) {it+1}.forEach {
               OutlinedButton(onClick = {carrera.apuesta1(it, 10F);popup.value = Ventanas.NADA }){
                   Text("Apuesta $it")
               }
           }
       }
   }
}

@Composable
fun int(i : Int){
    Row() {
            Text("$i")
    }
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

    val arr = remember { mutableStateListOf<Int>(1,2,3,4) }

    val ventana = remember { mutableStateOf(Ventanas.NADA) }

    val estado = rememberLazyListState()

    var apuestaCarrera by mutableStateOf(ApuestaCarrera(0,0F))

    val race = navController.usr?.let { Carrera(6, usuario = it, apuestaCarrera) }

    val carrera by remember { mutableStateOf(race) }



    //Dialogos
    when (ventana.value) {
        Ventanas.NADA -> {}
        Ventanas.CONSUTAUSUARIO -> consultaUsuario(navController.usr, ventana)
        Ventanas.CONSULTAHISTORIAL -> consultaHistorial(navController.usr!!.historial, ventana)
        Ventanas.APOSTARCARRERA -> race?.let { apuesta(it, ventana) }
        Ventanas.CONSULTAHISTORIALCARRERA -> if (race != null) {
            carrera?.let { consutlaCorredores(it.corredores, ventana) }
        }
        else -> {}
    }


    Row {


        //Barra Lateral
        Column( horizontalAlignment = Alignment.Start) {
            NavigationRail {
                Box {
                    IconButton(onClick = { exitProcess(0) }) {
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

        //La carrera
        Column(horizontalAlignment = Alignment.End) {
            Row(modifier = Modifier.padding(40.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column {

                    Text("La carrera de 6 corredores, Iteración ${carrera?.iterCarrera?.minus(6)}")

                    var apuesta by remember { mutableStateOf(TextFieldValue("0")) }
                    var porra by remember { mutableStateOf(TextFieldValue("0")) }

                    Row (horizontalArrangement = Arrangement.SpaceEvenly){
                        //Recibe Saldo
                        OutlinedTextField(
                            value = apuesta,
                            onValueChange = {newValue ->
                                run {
                                    apuesta = newValue;
                                }
                            },
                            textStyle = TextStyle(fontFamily = FontFamily.Monospace),
                            label = { Text("💸 Tu apuesta 💸")},
                            placeholder = { Text("El saldo que vas a apostar")},
                            singleLine = true,
                            isError = esErrorFloat(apuesta.text),
                            modifier = Modifier.width(120.dp)
                        )

                        OutlinedTextField(
                            value = porra,
                            onValueChange = {newValue ->
                                run {
                                    porra = newValue;
                                }
                            },
                            textStyle = TextStyle(fontFamily = FontFamily.Monospace),
                            label = { Text("A quien le vas ")},
                            placeholder = { Text("Elige 1 competidor del 1 al 6")},
                            singleLine = true,
                            isError = esErrorFloat(apuesta.text),
                            modifier = Modifier.width(120.dp)
                        )


                    }



                    Text("Tu apuesta se procesa en la siguiente carrera")

                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        var textoBoton by remember {mutableStateOf("Apuesta")}
                        Column {
                            OutlinedButton(onClick = {
                                textoBoton = if(!esErrorFloat(apuesta.text)){
                                    if(navController.usr?.saldo!! < apuesta.text.toFloat()) {
                                        textoBoton = "Saldo Insuficiente"
                                        return@OutlinedButton
                                    }
                                    if(!esErrorInt(porra.text) ){
                                        val comID = porra.text.toInt()
                                        if(comID in 1 .. 6){
                                            carrera?.apuesta1(comID,apuesta.text.toFloat())
                                        }
                                        return@OutlinedButton
                                    }else {
                                        "Competidor Invalido"
                                    }
                                } else {
                                    "Ingresa Saldo Valido"
                                }
                            }){
                                Text(textoBoton)
                            }
                        }

                        OutlinedButton(onClick = {ventana.value = Ventanas.CONSULTAHISTORIALCARRERA}){
                            Text("Consulta")
                        }
                    }
                }


                //Informacion Carrera
                Column(verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {

                    OutlinedTextField(
                        value = "A competidor ${carrera?.apuestaCarrera?.apuesta}, $ ${carrera?.apuestaCarrera?.apostado} ",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Ultima apuesta")},
                        singleLine =  true
                    )

                    OutlinedTextField(
                        value = "Competidor ${carrera?.lasWin?.id} con ${carrera?.lasWin?.chance?.times(100)} %",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Ultimo Ganador")},
                        singleLine =  true
                    )

                    Text("Segundas para la siguiente carrera ${carrera?.segundosParaSiguiente?.plus(1)} s")
                    LaunchedEffect(0){
                        carrera?.hacerCarrera()
                    }
                }


            }


            //Torneos
            Row {
                LazyColumn(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(12.dp)
                ) {
                    items(items = arr) {
                        int(it)
                        Spacer(Modifier.padding(10.dp))
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
