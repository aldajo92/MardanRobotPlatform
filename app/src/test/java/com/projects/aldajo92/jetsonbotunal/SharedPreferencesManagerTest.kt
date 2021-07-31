package com.projects.aldajo92.jetsonbotunal

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.projects.aldajo92.jetsonbotunal.api.SharedPreferencesManager
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class SharedPreferencesManagerTest {

    private lateinit var context: Context

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        sharedPreferencesManager = SharedPreferencesManager(context)
    }

    @Test
    fun whenSaveIPConfigurationReceivesValue_thenItStoresCorrectly() {
        val storeRemote = SharedPreferencesManager.IPConfigurations.REMOTE
        sharedPreferencesManager.saveIPConfiguration(storeRemote)

        assertEquals(storeRemote, sharedPreferencesManager.getStoredIPConfiguration())

        val storeLocal = SharedPreferencesManager.IPConfigurations.LOCAL_IP
        sharedPreferencesManager.saveIPConfiguration(storeLocal)

        assertEquals(storeLocal, sharedPreferencesManager.getStoredIPConfiguration())
    }

    @Test
    fun whenGetBaseURL_thenItReturnsTheAssociatedURL() {
        val localIP = SharedPreferencesManager.IPConfigurations.LOCAL_IP
        val remote = SharedPreferencesManager.IPConfigurations.REMOTE

        assertEquals(sharedPreferencesManager.getBaseUrl(localIP), "http://192.168.0.123:80")
        assertEquals(sharedPreferencesManager.getBaseUrl(remote), "https://jetsonbotunal.ngrok.io")

        sharedPreferencesManager.saveBaseURL(localIP, "192.169.89.1")
        sharedPreferencesManager.saveBaseURL(remote, "http://example.com")

        assertEquals("192.169.89.1", sharedPreferencesManager.getBaseUrl(localIP))
        assertEquals("http://example.com", sharedPreferencesManager.getBaseUrl(remote))
    }

    @Test
    fun whenSaveSampleTime_thenItStoresCorrectly() {
        val sampleTime = 500
        sharedPreferencesManager.saveVideoSampleTime(sampleTime)

        assertEquals(500, sharedPreferencesManager.getStoredVideoSampleTime())
    }

}
