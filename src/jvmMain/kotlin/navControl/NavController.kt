package navControl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import usuario.Usuario


class NavController(
    private val start : String,
    private var backStackScreens: MutableSet<String> = mutableSetOf(),
    var usr : Usuario?
) {
    var currentScreen : MutableState<String> = mutableStateOf(start)


    fun navigate(ruta : String){
        if(ruta != currentScreen.value){
            if(backStackScreens.contains(currentScreen.value) && currentScreen.value != start){
                backStackScreens.remove(currentScreen.value)
            }
            if(ruta == start){
                backStackScreens = mutableSetOf()
            } else {
                backStackScreens.add(currentScreen.value)
            }

            currentScreen.value = ruta
        }
    }

    fun navigateBack() {
        if (backStackScreens.isNotEmpty()) {
            currentScreen.value = backStackScreens.last()
            backStackScreens.remove(currentScreen.value)
        }
    }
}

@Composable
fun rememberNavController(
    start : String,
    backStackScreens: MutableSet<String> = mutableSetOf(),
    usr: Usuario? = null
) : MutableState<NavController> = rememberSaveable {
    mutableStateOf(NavController(start, backStackScreens, usr))
}