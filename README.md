Kumbara-Kala (State Pride)
Android App for Traditional Potters | Built with Kotlin + GenAI

The Problem
Traditional potters across India are losing buyers to plastic and steel — not because their products are inferior, but because they have no way to explain why clay is better. Clay cookware is chemical-free, naturally cooling, biodegradable, and rooted in centuries of heritage. But artisans, many living in rural areas without internet access or smartphones with advanced features, cannot easily communicate this to modern consumers.
Word of mouth is no longer enough.

Who This App Is For
Primary users: Traditional potters and clay artisans in rural Karnataka (and beyond) who want to promote their work digitally but have no technical background.
Secondary users: Buyers, households, and communities looking for sustainable, handcrafted alternatives to industrial kitchenware.

What Kumbara-Kala Does
Kumbara-Kala lets potters generate Digital Story Cards — visually appealing images that pair a clay product photo with a science-backed benefit message (e.g., "Naturally cools milk", "Chemical-free cooking"). The potter personalises the card with their name, village, and contact number, then shares it directly on WhatsApp in a single tap.

No internet required
No technical skill needed
Works on any basic Android phone (2 GB RAM, Android 8.0+)
App size under 15 MB


Features

Browse clay products in a gallery (curd pots, lamps, kulhads, and more)
Tap any product to get a health or eco-friendly benefit message
Personalise cards with artisan name, village, and contact number
Generate story card images using Android Canvas
Share instantly to WhatsApp with one tap
Fully offline — all data stored locally on device
Lightweight and fast on low-end devices


Tech Stack
LayerTechnologyLanguageKotlinIDEAndroid StudioLocal DatabaseRoom DatabaseImage GenerationAndroid Canvas APISharingAndroid Share Intent (WhatsApp)AI IntegrationGoogle AI Studio (GenAI)StorageLocal device storage

Hardware Requirements

Android smartphone running Android 8.0 (Oreo) or above
Minimum 2 GB RAM
At least 50 MB free storage


Installation & Setup
Prerequisites
Make sure the following are installed on your development machine:

Android Studio (latest stable version)
Android SDK (API Level 26 or above)
JDK 11 or above
A physical Android device or an AVD (Android Virtual Device) emulator


Step 1 — Clone the Repository
bashgit clone https://github.com/your-username/kumbara-kala.git
cd kumbara-kala

Step 2 — Open in Android Studio
File → Open → Select the cloned project folder
Wait for Gradle to sync. If it does not sync automatically:
bash./gradlew sync

Step 3 — Build the Project
bash# On macOS / Linux
./gradlew build

# On Windows
gradlew.bat build

Step 4 — Run the App
Option A — On a physical Android device:

Enable Developer Options on your phone:
Settings → About Phone → Tap "Build Number" 7 times
Enable USB Debugging:
Settings → Developer Options → USB Debugging → ON
Connect your phone via USB, then run:

bash./gradlew installDebug
Or use the Run ▶ button in Android Studio after selecting your device.
Option B — On an emulator:

Open AVD Manager in Android Studio:
Tools → Device Manager → Create Device
Choose a device with API Level 26 or above
Start the emulator, then run:

bash./gradlew installDebug

Step 5 — Verify the Build (Optional)
To run unit tests:
bash./gradlew test
To run instrumented tests on a connected device or emulator:
bash./gradlew connectedAndroidTest

Step 6 — Generate a Release APK (Optional)
bash./gradlew assembleRelease
The APK will be generated at:
app/build/outputs/apk/release/app-release.apk
Transfer this APK to an Android device and install it directly.

Project Structure
kumbara-kala/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/         # Kotlin source files
│   │   │   ├── res/          # Layouts, drawables, strings
│   │   │   └── AndroidManifest.xml
│   │   └── test/             # Unit tests
│   └── build.gradle
├── gradle/
├── build.gradle
└── README.md

Permissions Required
The app requests the following permissions at runtime:

WRITE_EXTERNAL_STORAGE — to save generated story card images
No internet permission required (fully offline)


Conclusion
Kumbara-Kala is not just a mobile application — it is a step toward sustainability, cultural revival, and digital empowerment. By combining lightweight Android technology with GenAI-powered content, it gives traditional artisans a voice in the digital marketplace without requiring internet access or technical knowledge.
