package eseo.android.raspble

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import eseo.android.raspble.ui.SetupNavGraph
import eseo.android.raspble.ui.theme.RaspBLETheme

class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RaspBLETheme {
                navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }
}