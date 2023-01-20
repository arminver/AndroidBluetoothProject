package eseo.android.raspble.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import eseo.android.raspble.R
import eseo.android.raspble.ui.Screen
import eseo.android.raspble.ui.components.ClickableButton
import eseo.android.raspble.ui.components.ImageView

@Composable
fun ScanScreen(
    navController: NavController
){
    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageView(image = R.drawable.logo_eseo)
            ClickableButton(text = stringResource(R.string.command_via_internet))
            ClickableButton(
                text = stringResource(R.string.scan_devices),
                onClick = {navController.navigate(Screen.Connect.route)}
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ScanScreenPreview() {
    ScanScreen(navController = rememberNavController())
}