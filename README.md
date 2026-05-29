# Handee

Flutter app with an embedded Unity ASL avatar on the home screen.

## Flow

1. Handee splash screen
2. Home screen with embedded Unity avatar
3. Type a word and tap **green play** → avatar signs via Unity
4. **Purple video** button → sign video fallback page
5. Side icons and bottom navigation (store, history, profile, etc.)

## Requirements

- Flutter SDK (3.x)
- Android Studio / Android SDK
- Physical Android device recommended (ARM64)

## Run

```bash
cd handee-amanyy
flutter pub get
flutter run
```

If build fails after pulling, run:

```bash
flutter clean
flutter pub get
flutter run
```

## Unity avatar (included in repo)

This repo includes the **avatar_final2** Unity runtime files required to build without a separate Unity export:

- `android/unityLibrary/UnityExport/unityLibrary/src/main/jniLibs/arm64-v8a/`
- `android/unityLibrary/UnityExport/unityLibrary/src/main/assets/bin/Data/`
- `assets/app.apk` (standalone Unity build reference)

Flutter sends signs with:

```dart
unityWidgetController?.postMessage(
  'AvatarController',
  'PlaySign',
  word,
);
```

Legacy fallback: `Hamada` + `ReceiveTextFromFlutter` (used by current Unity scene).

## GitHub

https://github.com/jessicaamged/handee
