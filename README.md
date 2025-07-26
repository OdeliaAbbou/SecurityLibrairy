# 🛡️ Secure Notes App

A fully functional Android application to write, save, and view secure notes using Firebase.  
This project comes with a powerful, **modular security library** called "securitylib" that can be reused in any Android project.

---

## 📱 Screenshots

 📋 Notes List

 🔐 Secure Detail View

 🚨 Suspicious Behavior Alert 

---

## 📦 Reusable Security Library – "securitylib"

"securitylib" is a reusable Android library designed to protect your app against data leaks and suspicious user behavior.

### 🔐 Key Features

✅ **Disable screenshots**  
Prevents screenshots or screen recording using "FLAG_SECURE".

✅ **Block previews in recent apps**  
Prevents your app content from being visible in the app switcher (recents view).

✅ **Disable copy/paste**  
Blocks all text interaction on input fields:
- no text selection
- no copy/paste
- no context menu

✅ **Automatically clear clipboard**  
Wipes the clipboard immediately after each change to avoid accidental leakage.

✅ **Detect suspicious app switching**  
Tracks how often the user switches apps:
> If the user leaves and returns **more than 4 times within 60 seconds**, a security alert is shown and the app is forcefully closed.

⏱️ This 60-second window is defined in `SecuritySwitchDetector.java` as:
private static final long TIME_WINDOW_MS = 60 * 1000; // 1 minute


🎛️ Per-Activity Custom Configuration
You can enable or disable each security layer independently per Activity by using SecurityConfig.

public class MyActivity extends BaseSecureActivity {
    @Override
    protected SecurityConfig provideSecurityConfig() {
        return new SecurityConfig.Builder()
            .disableScreenshots(true)          // enable screen capture protection
            .disableCopyPaste(false)           // allow copy/paste in this screen
            .disableRecentAppsPreview(true)    // hide content in recents view
            .build();
    }
}

💡 This means you can fine-tune where strong security is required (e.g. detail views), and where it's not necessary (e.g. login screen or public forms).

🚀 Getting Started
Prerequisites
- Android Studio Arctic Fox or newer
- Firebase (Realtime Database)
- Add your google-services.json file under /app

Setup
git clone https://github.com/your-username/SecureNotesApp.git
cd SecureNotesApp

- Open the project in Android Studio
- Sync Gradle
- Run the app on an emulator or real device

🧪 Testing & Reliability
 - ✅ Tested on Android 9 to 13
 - 🔒 Screen capture blocked
 - 👁️ Recents preview protected
 - 🔁 Suspicious behavior detection active
 - 📋 Clipboard automatically wiped






