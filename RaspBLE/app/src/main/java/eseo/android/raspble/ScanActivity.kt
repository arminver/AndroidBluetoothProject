package eseo.android.raspble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eseo.android.raspble.components.ClickableButton
import eseo.android.raspble.components.Device
import eseo.android.raspble.components.ImageView
import eseo.android.raspble.ui.theme.RaspBLETheme

class ScanActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RaspBLETheme {
                Scaffold {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
//                        ImageView(Modifier.weight(1f), R.drawable.logo_eseo)
                        ImageView(image = R.drawable.logo_eseo)
                        ClickableButton(text = "Commander via Internet")
                        ClickableButton(text = "Scanner les périphériques")
                    }
                }
            }
        }
    }
}

