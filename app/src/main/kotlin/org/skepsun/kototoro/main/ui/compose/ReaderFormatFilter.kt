package org.skepsun.kototoro.main.ui.compose

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.skepsun.kototoro.R
import org.skepsun.kototoro.core.ui.adaptive.tvDownFocusLink
import org.skepsun.kototoro.core.ui.adaptive.tvFocusable
import org.skepsun.kototoro.core.ui.compose.FilterFacetOption
import org.skepsun.kototoro.core.ui.compose.SingleChoiceFilterMenu
import org.skepsun.kototoro.parsers.model.ContentType

@Composable
fun ReaderFormatFilter(
    selectedType: ContentType?,
    enabledTypes: Set<ContentType>,
    onTypeSelected: (ContentType?) -> Unit,
    controlSize: Dp = 48.dp,
    iconSize: Dp = 20.dp,
    modifier: Modifier = Modifier,
    downFocusLink: FocusRequester? = null,
) {
    val types = listOf(null, ContentType.MANGA)
    val labels = listOf(R.string.all, R.string.content_type_manga)
        .map { stringResource(it) }
    val icons = listOf(
        R.drawable.ic_filter_content_type,
        R.drawable.ic_content_manga,
    )
    val normalizedType = selectedType?.readerFormatFamily()
    val availableFamilies = remember(enabledTypes) { enabledTypes.mapNotNull { it.readerFormatFamily() }.toSet() }
    // The reader edition has one eligible family; an extra selector adds no value.
    if (availableFamilies.size <= 1) return
    val selectedIndex = types.indexOf(normalizedType).coerceAtLeast(0)
    val enabled = availableFamilies.isNotEmpty()
    SingleChoiceFilterMenu(
        options = types.mapIndexed { index, type ->
            FilterFacetOption(type, labels[index], type == normalizedType, type == null || type in availableFamilies)
        },
        onOptionSelected = onTypeSelected,
        modifier = modifier,
        enabled = enabled,
    ) { openMenu ->
        IconButton(
            onClick = openMenu,
            enabled = enabled,
            modifier = Modifier
                .size(controlSize.coerceAtLeast(48.dp))
                .tvFocusable(enabled = enabled, shape = CircleShape, addFocusTarget = false)
                .tvDownFocusLink(downFocusLink),
        ) {
            Icon(
                painter = painterResource(icons[selectedIndex]),
                contentDescription = stringResource(R.string.content_type_filter) + ": " + labels[selectedIndex],
                modifier = Modifier.size(iconSize),
            )
        }
    }
}

/** Collapse reading sources to Manga and Novel; video never appears in the filter. */
internal fun ContentType.readerFormatFamily(): ContentType? = when (this) {
    ContentType.MANGA, ContentType.HENTAI_MANGA, ContentType.MANHWA,
    ContentType.MANHUA, ContentType.COMICS, ContentType.ONE_SHOT,
    ContentType.DOUJINSHI -> ContentType.MANGA
    ContentType.NOVEL, ContentType.HENTAI_NOVEL -> null
    else -> null
}
