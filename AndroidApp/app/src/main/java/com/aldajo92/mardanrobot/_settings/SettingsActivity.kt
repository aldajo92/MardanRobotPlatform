package com.aldajo92.mardanrobot._settings

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aldajo92.mardanrobot._settings.dataStore.DataStorePreferenceImpl
import com.aldajo92.mardanrobot._settings.model.visitor.CheckSettingsViewModel
import com.aldajo92.mardanrobot._settings.model.visitor.ChoiceListSettingsViewModel
import com.aldajo92.mardanrobot._settings.model.visitor.InputSettingsVisitorImpl
import com.aldajo92.mardanrobot._settings.model.visitor.InputTextSettingsViewModel
import com.aldajo92.mardanrobot._settings.model.visitor.SettingsUIRender
import com.aldajo92.mardanrobot._settings.model.visitor.TitleSettingsViewModel
import com.aldajo92.mardanrobot.ui.components.AppBarWithArrow

class SettingsActivity : ComponentActivity() {

    private val dataStorePreferenceAPI by lazy { DataStorePreferenceImpl(this) }

    private val listItems by lazy {
        listOf(
            CheckSettingsViewModel(
                title = "Enable BT",
                key = "defaultKey1",
                dataStorePreference = dataStorePreferenceAPI
            ),
            CheckSettingsViewModel(
                title = "Enable BT", key = "defaultKey2",
                dataStorePreference = dataStorePreferenceAPI
            ),
            TitleSettingsViewModel(title = "Enable BT"),
            CheckSettingsViewModel(
                title = "Enable BT",
                key = "defaultKey3",
                defaultValue = true,
                dataStorePreference = dataStorePreferenceAPI
            ),
            ChoiceListSettingsViewModel(
                title = "Choice From List",
                key = "key",
                defaultList = listOf("item1", "item2"),
                dataStorePreference = dataStorePreferenceAPI
            ),
            InputTextSettingsViewModel(title = "Check list", key = "key"),
        )
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                AppBarWithArrow(
                    title = "Settings",
                    onBackPressed = this::onBackPressed
                )
            }) {
                BodyContent()
            }
        }
    }

    @Composable
    fun BodyContent() {

        val inputSettingsVisitorImpl = InputSettingsVisitorImpl()

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(listItems) { item ->
                SettingsUIRender(this@SettingsActivity, item).AcceptUI(inputSettingsVisitorImpl)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dataStorePreferenceAPI.close() // TODO: Used as a temporal solution, create a singleton instead
    }

}
