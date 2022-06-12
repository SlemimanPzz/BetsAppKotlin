package screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import navControl.NavController


@Composable
fun int(i : Int){
    Row {
        Text("$i")
    }
}

@Composable
@Preview
fun Principal(navController: NavController){
    var carrera = remember { mutableStateOf(8) }

    val arr  = remember { mutableStateListOf<Int>()}
    arr.add(0)

    val estado = rememberLazyListState()



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

    Row {
        OutlinedButton( onClick = {arr.add(arr.size)}) {
            Text("Agrega ${arr.size}")
        }
        Spacer(Modifier.padding(8.dp))
        OutlinedButton(onClick = {arr.removeLast()}) {
            when(arr.size){
                0-> {}
                else -> Text("Elimina ${arr.last()}")
            }
        }
    }


}