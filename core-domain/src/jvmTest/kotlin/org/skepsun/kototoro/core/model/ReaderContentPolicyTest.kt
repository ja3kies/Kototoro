package org.skepsun.kototoro.core.model

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.skepsun.kototoro.parsers.model.ContentType

class ReaderContentPolicyTest {
    @Test
    fun `all persisted non-comic variants are excluded`() {
        for (type in listOf(
            ContentType.VIDEO, ContentType.HENTAI_VIDEO, ContentType.OTHER,
            ContentType.NOVEL, ContentType.HENTAI_NOVEL,
            ContentType.IMAGE_SET, ContentType.ARTIST_CG, ContentType.GAME_CG,
        )) {
            assertFalse(ReaderContentPolicy.acceptsSource("RESTORED", type))
        }
    }

    @Test
    fun `manga manhwa and manhua sources remain supported`() {
        for (name in listOf("MIHON_1", "LOCAL", "MANGADEX")) {
            for (type in listOf(ContentType.MANGA, ContentType.MANHWA, ContentType.MANHUA, ContentType.COMICS)) {
                assertTrue(ReaderContentPolicy.acceptsSource(name, type), "$name / $type")
            }
        }
    }

    @Test
    fun `retired providers cannot bypass the policy with misleading metadata`() {
        for (name in listOf("ANIYOMI_1", "CLOUDSTREAM_1", "JSON_TVBOX_1", "LOCAL_VIDEO", "LOCAL_NOVEL", "IREADER_1", "TSUNDOKU_1")) {
            assertFalse(ReaderContentPolicy.acceptsSource(name, ContentType.MANGA))
        }
    }

    @Test
    fun `unavailable reading extensions keep their restored content type`() {
        assertTrue(ReaderContentPolicy.acceptsSnapshot("RESTORED", ContentType.MANHWA, ContentType.OTHER))
        assertTrue(ReaderContentPolicy.acceptsSnapshot("RESTORED", ContentType.OTHER, ContentType.MANHUA))
        assertFalse(ReaderContentPolicy.acceptsSnapshot("RESTORED", ContentType.NOVEL, ContentType.MANGA))
        assertFalse(ReaderContentPolicy.acceptsSnapshot("RESTORED", ContentType.VIDEO, ContentType.MANGA))
        assertFalse(ReaderContentPolicy.acceptsSnapshot("RESTORED", null, ContentType.OTHER))
    }
}
