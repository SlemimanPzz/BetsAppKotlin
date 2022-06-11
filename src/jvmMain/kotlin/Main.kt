import androidx.compose.desktop.DesktopTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.DarkDefaultContextMenuRepresentation
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.singleWindowApplication
import navControl.NavController
import navControl.NavigationHost
import navControl.composable
import navControl.rememberNavController
import screens.CreaCuenta
import screens.IniciaSesion
import screens.LanzarApp
import usuario.Usuario


@Composable
@Preview
fun App(){
    val screens = Screen.values().toList()
    val navController by rememberNavController(Screen.LanzarApp.name)
    val currrentScreen by remember { navController.currentScreen }

        Box (modifier = Modifier.fillMaxSize()){
            CustomNavigationHost(navController = navController)
        }
}


enum class Screen(val label : String){
    LanzarApp(
        label = "Lazar",
    ),
    InciaSesion(
        label = "Inicia Sesión"
    ),
    CreaCuenta(
        label = "Crea Cuenta"
    )

}




fun main() = application{
    Window(onCloseRequest = ::exitApplication, title = "Apuestas") {
        App()
    }
}


@Composable
fun CustomNavigationHost(
    navController: NavController,
) {
        NavigationHost(navController) {
            composable(Screen.LanzarApp.name) {
                LanzarApp(navController)
            }

            composable(Screen.InciaSesion.name) {
                IniciaSesion(navController)
            }

            composable(Screen.CreaCuenta.name) {
                CreaCuenta(navController)
            }

        }.build()
    }



