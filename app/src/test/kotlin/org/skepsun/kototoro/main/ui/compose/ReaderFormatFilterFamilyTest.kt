package org.skepsun.kototoro.main.ui.compose

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.skepsun.kototoro.parsers.model.ContentType

class ReaderFormatFilterFamilyTest {
    @Test
    fun `manhwa manhua and comics remain under manga`() {
        for (type in listOf(ContentType.MANGA, ContentType.MANHWA, ContentType.MANHUA, ContentType.COMICS, ContentType.ONE_SHOT)) {
            assertEquals(ContentType.MANGA, type.readerFormatFamily())
        }
    }

    @Test
    fun `novel formats share their readable facet`() {
        assertEquals(ContentType.NOVEL, ContentType.NOVEL.readerFormatFamily())
        assertEquals(ContentType.NOVEL, ContentType.HENTAI_NOVEL.readerFormatFamily())
    }

    @Test
    fun `video formats have no reader facet`() {
        assertNull(ContentType.VIDEO.readerFormatFamily())
        assertNull(ContentType.HENTAI_VIDEO.readerFormatFamily())
    }
}
