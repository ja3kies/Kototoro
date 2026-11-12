# Renga — Reader Edition

Renga is a focused Android manga, manhwa and manhua reader. Its name distinguishes
this project from Kototoro. This phase reuses Kototoro's existing Kotlin/Compose
code; it does not purport to port SCANCORE's cross-platform C++ code.

## Reader-first navigation

- Home: resume the latest reading session and show a library shelf sourced from persisted favorites,
  sorted by last read. The full library opens from its header or quick action.
- History: recently read chapters (the history remains stored).
- Explore: find new works and configure manga-compatible sources.

Favorites, collections and progress remain persisted in the database, but no separate
favorites tab is presented. The old navigation preference is sanitized to three destinations
without destructive database changes.

## Content policy

Only manga, manhwa, manhua, comics, one-shots and doujinshi variants appear in reader
catalogs. Novel, video and standalone CG data are kept for historical compatibility,
but are excluded from the active content policy and space switcher.

## Coexistence and upgrades

For this migration, the existing applicationId remains unchanged to preserve the debug
app's local data and upgrade path. A distinct production package name must be planned
with backup/import support before installing alongside an upstream release; **changing
the applicationId now would orphan existing app data**.

## Architecture direction

SCANCORE inspiration: a unified reading-first library, reliable continuity, offline
progress and sources. Keep native Kotlin/Jetpack Compose and Material 3 with restrained
iOS-inspired transitions. Do not add its video or novel features.

See https://github.com/medium-gh/scancore and https://github.com/Kototoro-app/Kototoro .

### Remaining work

- Filter the library shelf to explicitly in-progress works with progress and completion metadata;
  the first iteration currently shows most recently read favorites (not necessarily unfinished).
- Complete removal of video and novel modules/dependencies only after a reference audit.
- Device UI tests, full regression suite, measured performance and migration testing.
- Distinct release package ID and an explicit data migration plan.
