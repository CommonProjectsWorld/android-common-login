# commonlogin Android Library

This is a reusable login screen library module (Email/Phone + Password) with validation.

- Package: `com.common.login`
- Activity: `CommonLoginActivity`

## How to use in your project

1. Copy the `commonlogin` folder into your project root.
2. In your `settings.gradle`:

   ```gradle
   include(":app", ":commonlogin")
   ```

3. In your app module `build.gradle`:

   ```gradle
   dependencies {
       implementation project(":commonlogin")
   }
   ```

4. Launch the login screen:

   ```kotlin
   val intent = CommonLoginActivity.createIntent(
       context = this,
       titleText = "Welcome",
       subtitleText = "Login with email or phone",
       loginButtonText = "Sign In"
   )
   startActivity(intent)
   ```
