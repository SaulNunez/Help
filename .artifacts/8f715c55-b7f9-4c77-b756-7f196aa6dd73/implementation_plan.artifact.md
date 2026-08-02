# Implementation Plan - Layout Best Practices

This plan aims to modernize the project's layouts by implementing Edge-to-Edge support, updating to Material 3 components, and improving accessibility and visual consistency.

## Proposed Changes

### Build Configuration

#### [MODIFY] [app/build.gradle](file:///home/saul/StudioProjects/Help/app/build.gradle)
- Add `androidx.activity:activity-ktx` dependency to enable `enableEdgeToEdge()` support.

### Activities & Logic

#### [MODIFY] [HelpMain.kt](file:///home/saul/StudioProjects/Help/app/src/main/java/com/saulnunez/help/HelpMain.kt)
- Call `enableEdgeToEdge()` in `onCreate`.
- Implement `ViewCompat.setOnApplyWindowInsetsListener` to adjust the `topAppBar` and `bottom_navigation` to account for system bars.

#### [MODIFY] [SettingsActivity.kt](file:///home/saul/StudioProjects/Help/app/src/main/java/com/saulnunez/help/SettingsActivity.kt)
- Call `enableEdgeToEdge()` in `onCreate`.

### Layouts

#### [MODIFY] [activity_help_main.xml](file:///home/saul/StudioProjects/Help/app/src/main/res/layout/activity_help_main.xml)
- Add `android:contentDescription` to the `topAppBar`.
- Ensure the `fragment_container_view` appropriately fills the space between the banner and bottom navigation.

#### [MODIFY] [sound_fragment.xml](file:///home/saul/StudioProjects/Help/app/src/main/res/layout/sound_fragment.xml)
- Replace `com.google.android.material.switchmaterial.SwitchMaterial` with `com.google.android.material.materialswitch.MaterialSwitch`.
- Add `android:padding="16dp"` to the root container for better spacing.
- Add `contentDescription` to switches.

#### [MODIFY] [location_fragment.xml](file:///home/saul/StudioProjects/Help/app/src/main/res/layout/location_fragment.xml)
- Replace `com.google.android.material.switchmaterial.SwitchMaterial` with `com.google.android.material.materialswitch.MaterialSwitch`.
- Add `android:padding="16dp"` to the root container.
- Add `contentDescription` to the switch.

### Resources & Manifest

#### [MODIFY] [AndroidManifest.xml](file:///home/saul/StudioProjects/Help/app/src/main/AndroidManifest.xml)
- Add `android:windowSoftInputMode="adjustResize"` to `HelpMain` and `SettingsActivity`.

#### [MODIFY] [dimens.xml](file:///home/saul/StudioProjects/Help/app/src/main/res/values/dimens.xml)
- Define standard spacing values (e.g., `grid_1` for 8dp, `grid_2` for 16dp).

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to ensure the project still builds.

### Manual Verification
- Deploy to a device/emulator and verify:
    - Content draws behind the status bar and navigation bar.
    - The Toolbar and Bottom Navigation are correctly padded and not obscured by system bars.
    - Switches have the updated Material 3 style.
    - Padding in fragments looks consistent.
