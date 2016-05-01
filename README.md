# ConnectU-android

# Android Version:
Android Studio 2.1
API 23: Android 6.0 (Marshmallow) revision 3

#Open Source

1. Volley - Install volley from - git submodule add -b master https://android.googlesource.com/platform/frameworks/volley volley
2. Add this to setting.gadle - include ':volley'
3. In  app/build.gradle, add a compile dependency for the volley project - compile project(':volley')
