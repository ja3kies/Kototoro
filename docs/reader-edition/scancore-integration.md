# Kototoro × Scancore integration

## Inspected source snapshots

- Kototoro Reader: `9361499e61285a05b0901558b9627de338291a04`, Apache-2.0.
- Scancore: `20ef489dcea0165aef417b03a4e44d24f25cefba`, MIT, copyright 2026 Medium Dev.
- Work continues on `feature/reader-ui-overhaul`; `devel` remains unchanged.

## Implementation direction

Keep Kotlin/Compose, Room, existing source adapters and Android reader services as the application foundation. Scancore uses C++/Qt/QML, including its Android UI; its screens cannot be dropped into Compose. Port useful behavior deliberately. Retain the MIT notice with any copied or translated Scancore implementation. No Scancore code has been copied yet.

Scancore references inspected: `src/qml/pages/HomePage.qml`, `src/core/Backups.h`, README and LICENSE. Its home emphasizes resume, continue-reading shelves and updates. It preserves previous task results during refresh and defers library refreshes while hidden. Its backup model records source identity, chapter links, progress, collections and tracker IDs. Those identifiers require explicit mapping before import; title similarity alone is insufficient.

The current working assumption remains an Android manga/novel reader, pending the user's scope response. Anime/video must not be reintroduced implicitly.

## Product priorities

1. Reading home: a stable resume action, recent works, updates and an actionable empty state.
2. Unified library: manga/novel distinction, persistent combined filters, progress and collections.
3. Discovery: installed-source search, explicit partial failures and reliable identity-based deduplication.
4. Reader: preserve page/strip modes and novel preferences; simplify contextual controls.
5. Import: inspect Scancore's actual export container and schema before implementing a preview-first, non-destructive importer. Preserve unmapped source identities and report unresolved items.
6. Background work: preserve cached results during refresh; measure hidden-screen work before changing scheduling.

These are pending implementation requirements, not delivered features.

## Visual direction

Use expressive typography, dynamic color and rounded native controls with quiet surfaces, clear section hierarchy, generous spacing and compact contextual toolbars. Avoid decorative gradients and blanket glass effects. Keep Android back behavior, edge-to-edge insets, TalkBack semantics and scalable text.

References consulted:
- https://developer.apple.com/design/human-interface-guidelines/
- https://developer.android.com/develop/ui/compose/designsystems/material3
- https://developer.android.com/jetpack/androidx/releases/compose-material3

## First correction

The shared home layout previously omitted its greeting/discovery content when initialization completed with no highlights. It now displays that existing content for an empty library. Loading and populated states retain their existing behavior. The shared desktop Compose test now checks all three transitions; Android uses the same layout implementation.

## Validation and limits

- `git diff --check`: passed.
- `:core-ui:jvmTest --rerun-tasks`: passed on Ubuntu ARM64/JDK 21. All 24 tasks executed; shared `core-ui` source and JVM tests recompiled.
- Android unit tests and APK build were attempted after installing SDK 37.0/37.1, build-tools 37.0.0, CMake 3.22.1 and NDK 28.2.13676358. Gradle reached `:app:compileDebugKotlin`, but the process ended without a build result after prolonged ARM compilation. No APK was produced.
- The focused desktop UI test could not start: optional desktop configuration requires pinned Mihon compatibility JARs, which are not present locally.
- New CI run `43096501107` remains queued after more than 10 hours. GitHub returned HTTP 502 when fetching its job list.
- No visual validation or performance improvement is claimed.

## Rollback

Revert the integration commits individually on the feature branch. Do not reset or force-push shared branches. This first change performs no schema migration and deletes no user data. Keep the existing backup/restore procedure in `rollback.md` for future changes.
