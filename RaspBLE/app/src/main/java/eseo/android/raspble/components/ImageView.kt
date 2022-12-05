package eseo.android.raspble.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eseo.android.raspble.R

@Composable
fun ImageView(
    image: Int? = R.drawable.ic_launcher_foreground,
    modifier: Modifier = Modifier,
) {
    Image(
        modifier = modifier.size(250.dp),
        painter = painterResource(id = image!!),
        contentDescription = "",
        alignment = Alignment.TopCenter
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ImageView()
}