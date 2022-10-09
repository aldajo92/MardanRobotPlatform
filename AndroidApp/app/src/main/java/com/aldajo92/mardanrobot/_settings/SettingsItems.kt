package com.aldajo92.mardanrobot._settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun ItemSettingsTemplate(
    modifier: Modifier = Modifier,
    text: String = "DefaultText",
    imageVector: ImageVector? = null,
    contentEnd: (@Composable RowScope.() -> Unit)? = {},
    itemClicked: () -> Unit = {}
) {
    Box(modifier = modifier.clickable { itemClicked() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            imageVector?.let {
                Icon(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    imageVector = it,
                    tint = Color.Black,
                    contentDescription = "MENU"
                )
            }
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically), text = text
            )
            contentEnd?.let { contentEnd() }
        }
        Divider(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun ItemSettingsTitle(
    modifier: Modifier = Modifier,
    text: String = "DefaultText",
    answerText: String = "DefaultAnswer"
) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            text = text,
            color = Color.LightGray
        )
        Divider(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            color = Color.LightGray
        )
    }
}

@Preview
@Composable
fun ItemSettingsInputText(
    modifier: Modifier = Modifier,
    text: String = "DefaultText",
    answerText: String = "DefaultAnswer",
    itemClicked: () -> Unit = {}
) {
    var name by remember { mutableStateOf("Alejandro") }
    ItemSettingsTemplate(
        text = text,
        imageVector = Icons.Filled.Info,
        contentEnd = {
            TextField(
                modifier = Modifier
                    .weight(1f),
                value = name,
                onValueChange = { name = it },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    // backgroundColor = Color.Transparent
                ),
            )
        }
    )
}

@Preview
@Composable
fun ItemSettingsCheck(
    modifier: Modifier = Modifier,
    text: String = "DefaultText",
    checkState: Boolean = false,
    itemClicked: () -> Unit = {},
    onCheckChanged: (Boolean) -> Unit = {}
) {
    ItemSettingsTemplate(
        text = text,
        imageVector = Icons.Filled.Settings,
        contentEnd = {
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = checkState,
                onCheckedChange = {
                    onCheckChanged(it)
                }
            )
        }
    )
}

@Preview
@Composable
fun ItemSettingsChoiceList(
    modifier: Modifier = Modifier,
    text: String = "DefaultText",
    values: List<String> = listOf(),
    checkState: Boolean = false,
    itemClicked: () -> Unit = {},
    onCheckChanged: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    ItemSettingsTemplate(
        text = text,
        imageVector = Icons.Filled.Settings,
        contentEnd = {
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier
                    .clickable {
                        val items = values.toTypedArray()
                        androidx.appcompat.app.AlertDialog
                            .Builder(context)
                            .setItems(items) { dialog, which ->
                                // Respond to item chosen
                            }
                            .show()
                    },
                imageVector = Icons.Filled.List,
                tint = Color.Black,
                contentDescription = "MENU"
            )
        }
    )
}
