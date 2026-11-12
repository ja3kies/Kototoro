package org.skepsun.kototoro.extensions.install

import android.content.Context
import android.content.SharedPreferences
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.skepsun.kototoro.cloudstream.runtime.CloudstreamRuntimeManager
import org.skepsun.kototoro.core.github.GitHubMirrorCatalogRepository
import org.skepsun.kototoro.core.prefs.AppSettings
import org.skepsun.kototoro.extensions.repo.ExternalExtensionType
import org.skepsun.kototoro.extensions.repo.RepoAvailableExtension

/** Legacy video plugins must be refused before downloads or metadata changes. */
class CloudstreamExtensionInstallTest {
    @Test
    fun `cloudstream video plugin is rejected before side effects`() =
        assertVideoExtensionRejected(ExternalExtensionType.CLOUDSTREAM)

    @Test
    fun `aniyomi video plugin is rejected before side effects`() =
        assertVideoExtensionRejected(ExternalExtensionType.ANIYOMI)

    private fun assertVideoExtensionRejected(type: ExternalExtensionType) = runTest {
        val context = mockk<Context>(relaxed = true)
        val preferences = mockk<SharedPreferences>(relaxed = true)
        val runtime = mockk<CloudstreamRuntimeManager>(relaxed = true)
        val httpClient = mockk<OkHttpClient>(relaxed = true)
        val service = ExtensionInstallService(
            context, httpClient, mockk<AppSettings>(relaxed = true),
            mockk<GitHubMirrorCatalogRepository>(relaxed = true), runtime,
            mockk<SystemPackageInstaller>(relaxed = true),
        )

        val error = runCatching { service.install(extension(type)) }.exceptionOrNull()
        assertTrue(error is IllegalArgumentException)
        assertTrue(error?.message?.contains("reading extensions only") == true)
        verify(exactly = 0) { preferences.edit() }
        verify(exactly = 0) { context.getSharedPreferences(any(), any()) }
        verify(exactly = 0) { runtime.installPlugin(any(), any(), any()) }
        verify(exactly = 0) { httpClient.newCall(any()) }
    }

    private fun extension(type: ExternalExtensionType) = RepoAvailableExtension(
        type = type, name = "Unsupported video", pkgName = "test.plugin",
        versionName = "2", versionCode = 2, libVersion = 1.0, lang = "en", isNsfw = false,
        sourceNames = emptyList(), archiveName = "plugin.cs3",
        archiveUrl = "https://example.invalid/plugin.cs3",
        iconUrl = "", repoUrl = "https://example.invalid", repoName = "Test",
        signatureHash = "", isCompatible = true,
    )
}
