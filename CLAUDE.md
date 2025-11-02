# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Korean Patch** is a multi-platform Minecraft client-side mod supporting Fabric, NeoForge, and legacy Forge loaders. It enables seamless Hangul (Korean) character input without interfering with game keybinds. The architecture uses **bytecode injection via Mixins** to intercept Minecraft's text input pipeline and **Java ServiceLoader pattern** for multi-platform abstraction.

**Current Target:** Minecraft 1.21.9-1.21.10 running on Java 21

## Common Build & Development Commands

### Build Commands
```bash
./gradlew build              # Build all modules (fabric, neoforge, common)
./gradlew :common:build      # Build only common module
./gradlew :fabric:build      # Build only Fabric version
./gradlew :neoforge:build    # Build only NeoForge version
./gradlew clean              # Clean all build artifacts
```

### Running/Testing
```bash
./gradlew :fabric:runClient   # Launch Fabric dev client with mod
./gradlew :neoforge:runClient # Launch NeoForge dev client with mod
./gradlew :neoforge:runServer # Launch NeoForge dev server
./gradlew jar                  # Package mod JAR without running
```

### Packaging & Publishing
```bash
./gradlew sourcesJar           # Generate sources JAR
./gradlew build -x test        # Build without running tests
./gradlew publishToMavenLocal  # Publish to local Maven repository
```

## Multi-Loader Architecture

The project is organized as a **3-module Gradle build**:

```
root/
├── common/       (Platform-agnostic core logic)
├── fabric/       (Fabric-specific entry points & integrations)
├── neoforge/     (NeoForge-specific entry points & integrations)
└── forge/        (Legacy - currently inactive)
```

### Key Design Principle: ServiceLoader Pattern

All platform-specific code is loaded dynamically via Java's `ServiceLoader`:

```java
// In common module (platform-agnostic)
Services.load(IPlatformHelper.class)  // Runtime discovery
```

**Implementations:**
- `FabricPlatformHelper` (fabric/src/main/java)
- `NeoForgePlatformHelper` (neoforge/src/main/java)

**Benefit:** Common code never directly imports platform APIs. All integration happens through service implementations.

### Version Management

**All versions are centralized in `gradle.properties`**. Important note from `gradle.properties`:
> Every field you add must be added to the root build.gradle expandProps map.

When adding new version properties:
1. Add to `gradle.properties`
2. Update `root build.gradle` `expandProps` block
3. Reference via `${property_name}` in `*.json` and `.toml` files

## Code Architecture

### Input Processing Pipeline

```
KeyboardEvent
  ↓
KeyboardHandlerMixin (intercepts keyboard)
  ↓
InputManager.getController() (platform dispatch)
  ↓
WinController/DarwinController/EmptyController
  ↓
HangulProcessor (composes Korean characters)
  ↓
LangTypeManager (tracks input language state)
  ↓
WrapperEditBox / WrapperTextField (applies text to UI)
  ↓
IndicatorHandler (renders language state visual)
```

### Core Modules

**`driver/` - Platform Abstraction Layer**
- `InputController` (interface) → `driver.arch.{WinController, DarwinController, EmptyController}`
- Handles OS-specific keyboard/IME APIs
- Windows: Direct Windows API calls for IME state
- Mac: Cocoa input handling via JNI
- Linux: Fallback implementation

**`process/` - Hangul Character Logic**
- `HangulProcessor` - Decomposes keystrokes into Hangul syllables
- Referenced from LGPL-3.0 licensed [NaraeChat](https://github.com/sokcuri/NaraeChat)
- Implements Korean syllable composition rules

**`mixin/` - Bytecode Injection Targets**
- `mixin/indicator/` - Screen rendering (ChatScreen, EditSign, etc.)
- `mixin/mods/` - Third-party mod compatibility (REI, CommandBlockIDE, EasyAnvils, Xaero's)
- `mixin/accessor/` - Reflection helpers for private field access
- `FormattableEditBoxMixin`, `KeyboardHandlerMixin`, `MultilineEditBoxMixin` - Core input interception

**`wrapper/` - Text Field Interception**
- Wraps EditBox/TextField instances to intercept text changes
- Applies Korean composition before updating UI
- Multiple implementations for different mod compatibility

**`config/` - Configuration Management**
- YACL-based in-game GUI configuration
- Persistent JSON storage via GSON
- Settings for indicator appearance (color, position, easing)
- Keybinding configuration (platform-specific toggle keys)

**`indicator/` - Visual Feedback**
- Renders language state indicator on screen
- Supports animations and customizable appearance
- Integrated via Mixin into various screen types

### Mixin Organization

**Shared Mixin Files:**
- `koreanpatch.mixins.json` - Client-side Mixin configuration
- Located in `common/src/main/resources`
- Injected into all loader variants

**Loader-Specific Mixins:**
- `koreanpatch.fabric.mixins.json` - Fabric-only (fabric/src/main/resources)
- `koreanpatch.neoforge.mixins.json` - NeoForge-only (neoforge/src/main/resources)

### Platform Support

Platform keybindings (configurable in mod settings):
- **Windows:** Right-Alt (한/영) or Ctrl+I (IME toggle)
- **Mac/Linux:** Left-Control (한/영)

Different controllers handle platform-specific differences in character input and IME state management.

## Extending the Mod

### Adding Support for a New Screen Type

1. Create new Mixin in `common/src/main/java/com/hyfata/najoan/koreanpatch/mixin/indicator/`
   - Follow pattern from `ChatScreenMixin`, `EditSignMixin`, etc.
   - Inject into `render()` method to display indicator
2. Add entry to `common/src/main/resources/koreanpatch.mixins.json`
3. Test via `./gradlew :fabric:runClient`

### Adding Support for a Third-Party Mod

1. Add CompileOnly dependency to `common/build.gradle` (Modrinth Maven artifact)
2. Create new Mixin in `common/src/main/java/com/hyfata/najoan/koreanpatch/mixin/mods/`
3. Add to `koreanpatch.mixins.json`
4. Mirror implementation to `fabric/build.gradle` and `neoforge/build.gradle` as needed
5. Example: REI, CommandBlockIDE, EasyAnvils already integrated

### Modifying Configuration

1. Add property to `com.hyfata.najoan.koreanpatch.config.ModConfig`
2. Create YACL category in `config/yacl/category/` if needed
3. Update `ConfigManager` for persistence logic
4. Regenerate config screen by modifying `config/yacl/YACLBuilder`

## Dependency Notes

### Critical Dependencies

| Dependency | Version | Purpose |
|-----------|---------|---------|
| Mixin | 0.8.5 | Bytecode manipulation for Minecraft integration |
| MixinExtras | 0.3.5 | Enhanced Mixin capabilities (injections, callbacks) |
| Parchment | 1.21.9 | Human-readable mappings for decompiled code |
| YACL | 3.8.0+1.21.9 | Config screen GUI library |
| Cloth Config | 20.0.148 | Legacy config support |

### Optional Mod Integrations (CompileOnly)

These are **CompileOnly** dependencies - they don't get bundled with the mod:
- ModMenu, Command Block IDE, Better Command Block UI
- Easy Anvils (separate versions for Fabric/NeoForge)
- Axiom, Xaero's Minimap & World Map
- REI (Rich Inventory Exploration)

When updating mod integrations, check Modrinth for latest artifact IDs (they change when mods are updated).

### OpenSource Attribution

- **NaraeChat** (LGPL 3.0) - Referenced: Hangul_Set_2_Layout, KeyboardLayout, QwertyLayout, HangulProcessor
- **caramelChat** (LGPL 3.0) - Referenced: IOperator interface and driver package architecture

## Gradle Build System Details

### Custom Gradle Plugins (buildSrc/)

**multiloader-common.gradle**
- Shared Java/Maven publishing configuration
- Version string generation (release vs. snapshot variants)
- Resource processing to expand `${property_name}` placeholders in JSON/TOML files
- Capability declarations for proper dependency resolution

**multiloader-loader.gradle**
- Pulls common source and resources into loader-specific builds
- Configures compileJava to include common module classes
- Reuses common mixins across all loaders

### Loader-Specific Configuration

**Fabric (fabric-loom plugin)**
- Embeds Fabric API, Cloth Config, YACL
- Uses `include()` in build.gradle to embed optional dependencies

**NeoForge (net.neoforged.moddev plugin)**
- Different plugin system and mod metadata format
- Uses NeoForge's mod.toml for metadata
- Separate artifact stream from Fabric

### Resource Placeholder Expansion

In `gradle.properties`, properties like `${mod_version}` are expanded during `processResources` task:
- `fabric.mod.json` - Fabric mod metadata
- `neoforge.mods.toml` - NeoForge mod metadata
- Mixin JSON configuration files

When updating versions, these are automatically substituted.

## Common Development Patterns

### Accessing Platform APIs from Common Code

Use ServiceLoader discovery:
```java
// In common/ module - no platform imports
IPlatformHelper helper = Services.load(IPlatformHelper.class);
helper.doSomethingPlatformSpecific();
```

### Extending Mixin Targets

When targeting new Minecraft classes in Mixins:
1. Check Parchment mappings (human-readable names vs. obfuscated)
2. Use `@Mixin(ClassName.class)` or `@Mixin(targets = "fully.qualified.ClassName")`
3. Test in both Fabric and NeoForge to ensure mappings are compatible

### Text Input Interception

Text changes flow through `WrapperEditBox` or `WrapperTextField`:
1. Wrapping happens in Mixin injections
2. Korean composition happens in wrapper
3. Wrapper applies composed text to actual EditBox

Track wrapper lifecycle carefully - mixins must inject at screen creation time.

## Troubleshooting

### Build Failures
- `./gradlew clean build` - Clean cache and rebuild
- Check Java version: Must be 21+
- Update IDE project structure after gradle.properties changes

### Mixin Issues
- Verify Mixin JSON paths in loader configs (fabric.mod.json, neoforge.mods.toml)
- Check mixin target classes exist in current Minecraft version (Parchment mappings)
- Runtime errors in Mixin suggest injection point mismatch

### Platform-Specific Issues
- Input not working? Check InputController for current OS via `Services.load(IOperator.class)`
- Indicator not rendering? Verify screen Mixin targets correct render method
- Config not loading? Check ConfigManager persistence path

## Repository Information

**Version:** 1.9.2
**License:** LGPL-3.0
**Developers:** Najoan, Shihyeon
**Issue Tracker:** https://github.com/najoan125/KoreanPatch-multiLoader/issues