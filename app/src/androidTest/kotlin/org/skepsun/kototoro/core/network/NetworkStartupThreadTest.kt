package org.skepsun.kototoro.core.network

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.atomic.AtomicReference

/**
 * Regression for MainActivity startup: Dagger may first resolve the base HTTP
 * singleton from onCreate on the main thread. Constructing it must not crash.
 * This does not authorize synchronous network requests from the UI thread.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NetworkStartupThreadTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Test
    fun baseHttpClientCanBeResolvedDuringActivityStartup() {
        hiltRule.inject()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val client = AtomicReference<OkHttpClient>()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            client.set(
                EntryPointAccessors.fromApplication<StartupNetworkEntryPoint>(context)
                    .baseHttpClient,
            )
        }
        assertNotNull(client.get())
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface StartupNetworkEntryPoint {
    @get:BaseHttpClient
    val baseHttpClient: OkHttpClient
}
