import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mycontacts.R

@Composable
fun ContactSearchItem(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
) {
    val color = colorResource(R.color.grey_727272)
    val color_light = colorResource(R.color.grey_outline)

    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        placeholder = { Text(text = "Search contact", color = color) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search Icon",
                tint = color
            )
        },
        trailingIcon = {
            Row(
                modifier = Modifier
                    .padding(end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Mic,
                    contentDescription = "Mic Icon",
                    tint = color_light,
                    modifier = Modifier
                        .clickable { }
                )
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More Options Icon",
                    tint = color_light,
                    modifier = Modifier
                        .clickable { }
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = color_light,
            unfocusedBorderColor = color_light,
            focusedContainerColor = colorResource(R.color.grey_light),
            unfocusedContainerColor = colorResource(R.color.grey_light)
        )
    )
}

@Composable
@Preview(showBackground = true)
fun ContactSearchItemPreview() {
    ContactSearchItem(
        text = "Search Contact",
        onValueChange = {}
    )
}
