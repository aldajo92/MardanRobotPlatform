package com.aldajo92.mardanrobot._settings

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aldajo92.mardanrobot.R


@Preview
@Composable
fun ItemSettingsTemplate(
    modifier: Modifier = Modifier,
    text: String = "DefaultText",
    value: String? = "DefaultValue",
    imageVector: ImageVector? = null,
    contentEnd: (@Composable RowScope.() -> Unit)? = {},
    itemClicked: () -> Unit = {}
) {
    Box(modifier = modifier.clickable { itemClicked() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
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
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = text)
                value?.let {
                    Text(text = value, fontStyle = FontStyle.Italic, color = Color.LightGray)
                }
            }
            contentEnd?.let { contentEnd() }
        }
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
        value = null,
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
        value = checkState.toString(),
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
    context: Context = LocalContext.current,
    text: String = "DefaultText",
    values: List<String> = listOf(),
    selectedValue: String? = null,
    onNewItemAdded: (String) -> Unit = {},
    onDeleteValueByPosition: (Int) -> Unit = {},
    onValueSelected: (String) -> Unit = {}
) {
    ItemSettingsTemplate(
        text = text,
        value = selectedValue ?: "No value selected",
        imageVector = Icons.Filled.Settings,
        contentEnd = {
            Row(
                modifier = Modifier.align(Alignment.CenterVertically),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier
                        .clickable {
                            var dialog: AlertDialog? = null
                            dialog = showListDialog(context, values,
                                {
                                    onDeleteValueByPosition(it)
                                    dialog?.dismiss()
                                },
                                {
                                    onValueSelected(it)
                                }
                            )
                        },
                    imageVector = Icons.Filled.List,
                    tint = Color.Black,
                    contentDescription = "MENU"
                )
                Icon(
                    modifier = Modifier
                        .clickable {
                            showDialog(context) { onNewItemAdded(it) }
                        },
                    imageVector = Icons.Filled.Add,
                    tint = Color.Black,
                    contentDescription = "MENU"
                )
            }
        }
    )
}

fun showDialog(
    context: Context,
    onTextInputCompleted: (String) -> Unit = {}
): AlertDialog = AlertDialog.Builder(context).apply {
    val input = EditText(context).apply {
        hint = "Enter Text"
        inputType = InputType.TYPE_CLASS_TEXT
    }
    setView(input)
    setPositiveButton("OK") { dialog, _ ->
        val textCompleted = input.text.toString()
        onTextInputCompleted(textCompleted)
        dialog.dismiss()
    }
    setNegativeButton(
        "Cancel"
    ) { dialog, _ -> dialog.cancel() }
}.show()


fun showListDialog(
    context: Context,
    list: List<String> = listOf(),
    onDeleteItem: (Int) -> Unit = {},
    onItemSelected: (String) -> Unit = {}
): AlertDialog = AlertDialog.Builder(context).apply {

    class ViewHolder(view: View) {
        val icon: ImageView = view.findViewById(R.id.icon)
        val title: TextView = view.findViewById(R.id.title)
    }

    val items = list.toTypedArray()

    val adapter: ListAdapter = object : ArrayAdapter<String?>(
        context, R.layout.list_item, items
    ) {
        private val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: inflater.inflate(R.layout.list_item, parent, false)
            val holder = ViewHolder(view)

            holder.title.text = items[position]
            holder.icon.setImageResource(R.drawable.ic_baseline_delete_24)

            holder.icon.setOnClickListener {
                onDeleteItem(position)
            }
            return view
        }
    }
    setAdapter(adapter) { dialog, which ->
        onItemSelected(items[which])
        dialog.dismiss()
    }
}.show()
