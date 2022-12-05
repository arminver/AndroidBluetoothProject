package eseo.android.raspble.components

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ClickableButton(
    onClick: () -> Unit = {},
    text: String = "Button",
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick ,
    ) {
        Text(text = text)
    }
}