package org.skepsun.kototoro.core.model

import org.skepsun.kototoro.parsers.model.ContentType

/** Reading scope, independent of loaded extensions. Persisted enum values remain unchanged. */
object ReaderContentPolicy {
    val contentTypes: Set<ContentType> = setOf(
        ContentType.MANGA, ContentType.MANHWA, ContentType.MANHUA,
        ContentType.COMICS, ContentType.HENTAI_MANGA,
        ContentType.ONE_SHOT, ContentType.DOUJINSHI,
    )

    private val retiredSourcePrefixes = listOf(
        "ANIYOMI_", "CLOUDSTREAM_", "JSON_TVBOX_",
        "JSON_LEGADO_", "JSON_LNREADER_", "IREADER_", "TSUNDOKU_",
        "LOCAL_NOVEL",
    )

    fun acceptsSource(name: String, type: ContentType): Boolean =
        name != "LOCAL_VIDEO" && retiredSourcePrefixes.none(name::startsWith) && type in contentTypes

    /** Restored metadata wins over an unresolved extension's OTHER fallback. */
    fun acceptsSnapshot(name: String, persistedType: ContentType?, resolvedType: ContentType): Boolean =
        acceptsSource(name, persistedType?.takeUnless { it == ContentType.OTHER } ?: resolvedType)
}
