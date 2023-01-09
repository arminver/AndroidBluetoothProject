package eseo.android.raspble.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import eseo.android.raspble.R
import eseo.android.raspble.ui.components.ClickableButton
import eseo.android.raspble.ui.components.ImageView

@Composable
fun CommandScreen(
    navController: NavController
) {
    var ledState by remember { mutableStateOf(false) }

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageView(
                image = if (ledState) R.drawable.ic_baseline_bolt_on
                else R.drawable.ic_baseline_bolt_off,
            )
            ClickableButton(
                text = "CHANGER L'ETAT",
                onClick = { ledState = !ledState }
            )
        }
        /*
        @Composable
fun App(state: MutableState<Boolean>) {
  if (state.value) {
    // Render UI elements for the "true" state
  } else {
    // Render UI elements for the "false" state
  }
}

         */
    }
}
@Composable
@Preview(showBackground = true)
fun CommandScreenPreview() {
    CommandScreen(navController = rememberNavController())
}