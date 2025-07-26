# 🛡️ Secure Notes App

A fully functional Android app to create and view secure notes using Firebase.  
This project includes a **modular and reusable security library** called "securitylib", designed to prevent data leaks and monitor suspicious behavior.

---

## 📱 Screenshots

| 📋 Notes List 
<img width="302" height="509" alt="image" src="https://github.com/user-attachments/assets/fe48af8c-6986-4edf-90c6-d3e020ce266a" />


| 🔐 Secure Detail View 
<img width="292" height="685" alt="image" src="https://github.com/user-attachments/assets/8851e793-ac55-47dd-8f88-30656884bfb5" />


| 🚨 Suspicious Behavior Alert 
<img width="312" height="565" alt="image" src="https://github.com/user-attachments/assets/6123d5fa-b246-40d4-8cd1-c63f38a60d32" />


---

## 📦 Reusable Security Library – `securitylib`

"securitylib" is an Android library you can plug into any app to protect sensitive data on screen and track unusual user behavior.

### 🔐 Key Features

✅ **Disable screenshots**  
Blocks screenshots and screen recording with "FLAG_SECURE".

✅ **Block preview in recent apps**  
Hides your app content in the Android recent tasks list.

✅ **Disable copy/paste**  
Removes all interaction with text fields:
- no text selection
- no context menu
- no copy/paste

✅ **Clear clipboard automatically**  
Every time you type in a field, the clipboard is wiped to avoid leaks.

✅ **Detect suspicious app switching**  
If the user switches away and back to the app **more than 4 times within 60 seconds**, a warning is shown and the app closes automatically.

⏱️ This 60-second behavior is defined in the library by:
private static final long TIME_WINDOW_MS = 60 * 1000;


🎛️ Configure per Activity
Each activity can define its own protection level with SecurityConfig:

public class MyActivity extends BaseSecureActivity {
    @Override
    protected SecurityConfig provideSecurityConfig() {
        return new SecurityConfig.Builder()
            .disableScreenshotsAndRecentApps(false)     // allow screenshots and preview in recents
            .disableCopyPaste(true)                    // disable copy/paste
            .build();
    }
}


🚀 Getting Started
Prerequisites
 - Android Studio (Arctic Fox or later)
 - Firebase (Realtime Database)
 - Add your google-services.json inside /app

Setup
git clone https://github.com/your-username/SecureNotesApp.git
cd SecureNotesApp
 - Open in Android Studio
 - Sync Gradle and run the app



🧪 Security Validated
 - ✅ Tested on Android 9–13
 - 🔒 Screen capture blocked where needed
 - 👁️ Recent apps view hidden
 - 🔁 Suspicious behavior detection in action
 - 📋 Clipboard automatically wiped




