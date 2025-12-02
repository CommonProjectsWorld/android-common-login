# android-common-login

Android library module providing a reusable email/phone + password login screen.

## Publish with JitPack

1. Push this project to GitHub as `CommonProjectsWorld/android-common-login`.
2. Create tag:

   git tag v1.0.0
   git push origin v1.0.0

3. On JitPack, use:

   com.github.CommonProjectsWorld:android-common-login:v1.0.0

4. Consumer app Gradle:

   repositories {
       google()
       mavenCentral()
       maven { url "https://jitpack.io" }
   }

   dependencies {
       implementation "com.github.CommonProjectsWorld:android-common-login:v1.0.0"
   }

Make sure the consumer app theme extends a MaterialComponents theme.
