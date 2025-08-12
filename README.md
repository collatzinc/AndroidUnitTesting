# Android Unit Test Sample

This project is an **Android application** built using **Jetpack Compose** and **Clean Architecture** principles.  
It includes **unit test cases** for different layers and demonstrates how **Gradle** automatically generates test reports in HTML format.  
**Flavours implemented**: `staging` and `production`.

---

## Features
- **Jetpack Compose** for UI
- **Clean Architecture** (data, domain, presentation layers)
- **Unit tests** for data and domain layer logic
- **Gradle test reports** viewable in a web browser
- **Flavour-based builds**: staging and production
- **Release build** automatically runs unit tests before generating the bundle

---

## No CI/CD in your project?
You can still configure **Gradle** to run unit tests automatically before creating a release build.  
This helps prevent releasing builds with broken code.

---

## Gradle Configuration to Run Unit Tests in Release Build

Add the following to your `app/build.gradle.kts`:

```kotlin
afterEvaluate {
    android.applicationVariants.all { variant ->
        if (variant.buildType.name == "release") {
            val variantName = variant.name.replaceFirstChar { it.uppercaseChar() }
            val bundleTaskName = "bundle${variantName}"
            val testTaskName = "test${variantName}UnitTest"
            tasks.named(bundleTaskName) {
                dependsOn(testTaskName)
            }
        }
        true
    }
    // To view the unit test report:
    // Open: app/build/reports/tests/test${variantName}UnitTest/index.html
}
```
---

## How to View Test Report

1. **Clone** this project:
   ```bash
   git clone https://github.com/collatzinc/AndroidUnitTesting.git
   ```
2. **Open** the project in **Android Studio**.
3. **Run the unit tests**:
4. **Locate the test report**:
   ```
   app/build/reports/tests/test${variantName}UnitTest/index.html
   ```
   Replace `${variantName}` with your build variant, for example:
   - `StagingDebug`
   - `ProductionRelease`
5. **Open the HTML file** in your preferred web browser to see the results.
