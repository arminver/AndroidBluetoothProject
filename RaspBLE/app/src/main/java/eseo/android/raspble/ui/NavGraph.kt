package eseo.android.raspble.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import eseo.android.raspble.ui.screen.ConnectScreen
import eseo.android.raspble.ui.screen.ScanScreen

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Scan.route
    ) {
        composable(
            route = Screen.Scan.route
        ) {
            ScanScreen(navController)
        }
        composable(
            route = Screen.Connect.route
        ) {
            ConnectScreen(navController, LocalContext.current)
        }
    }
}