# Walkthrough - Sprite Download & Injection

Implemented sprite download, version-based disk caching, offline fallback, and ROM sprite injection.

## Changes

### AlttprService
- **[AlttprService.kt](file:///Users/steve/Projects/ALTTPR/shared/src/commonMain/kotlin/com/stingers/alttpr/repository/remote/AlttprService.kt)**: Added `getSpriteFile(@Url url: String): ByteArray`.

### RomStorage
- **[RomStorage.kt](file:///Users/steve/Projects/ALTTPR/shared/src/commonMain/kotlin/com/stingers/alttpr/repository/local/RomStorage.kt)**, **RomStorage.android.kt**, **RomStorage.ios.kt**, **RomStorage.jvm.kt**: Added methods `saveSpriteFile` and `getSpriteFile` for storing sprite files in the local file system.

### AlttprRepository
- **[AlttprRepository.kt](file:///Users/steve/Projects/ALTTPR/shared/src/commonMain/kotlin/com/stingers/alttpr/repository/AlttprRepository.kt)**: Added `getSpriteBytes(sprite: Sprite): ByteArray?`:
  - Checks if downloaded file exists locally and matches version.
  - If offline, returns existing cached file bytes if available.
  - If online and version changed or missing, downloads sprite file via `alttprService.getSpriteFile(...)`, saves to disk, updates database entity with `downloadedFile` filename, and returns bytes.

### RomManager
- **[RomManager.kt](file:///Users/steve/Projects/ALTTPR/shared/src/commonMain/kotlin/com/stingers/alttpr/repository/RomManager.kt)**: Injected `AlttprRepository` into `RomManager` and updated `getPatchedRomBytes` to fetch sprite bytes and apply `injectSprite(...)`.

## Verification Results
- Successfully built project with `assembleDebug` (Build finished successfully).
