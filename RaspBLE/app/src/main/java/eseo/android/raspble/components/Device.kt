package eseo.android.raspble.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eseo.android.raspble.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Device(
    title: String = "Mon titre",
    content: String = "Mon contenu",
    onClick: () -> Unit = {},
    image: Int? = R.drawable.ic_baseline_devices_other_24,
) {
    Card(modifier = Modifier.fillMaxWidth().padding(5.dp), onClick = onClick) {
        Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
            image?.let {
                Image(modifier = Modifier.height(50.dp), painter = painterResource(id = it), contentDescription = content)
            }
            Column() {
                Text(text = title)
                Text(text = content, fontWeight = FontWeight.Light, fontSize = 10.sp)
            }
        }
    }
}