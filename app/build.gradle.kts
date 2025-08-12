import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlinx.serialization)
}

//buildTypes
val release = "release"
val debug = "debug"
//productFlavors
val production = "production"
val staging = "staging"
val environment = "env"

//flavor-specific config
val stagingBaseUrl = "https://dummyjson.com/"
val prodBaseUrl = "https://dummyjson.com/"


android {
    namespace = "com.collatzinc.androidunittesting"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.collatzinc.androidunittesting"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName(release) {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName(debug) {
            isDefault = true
            isDebuggable = true
        }
    }

    // Specifies one flavor dimension.
    flavorDimensions.add(environment)

    productFlavors {
        create(staging) {
            isDefault = true
            dimension = environment
            versionNameSuffix = "s"
            buildConfigField("String", "BASE_URL", "\"$stagingBaseUrl\"")
        }
        create(production) {
            dimension = environment
            buildConfigField("String", "BASE_URL", "\"$prodBaseUrl\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)


    //Testing
    testImplementation(kotlin("test"))
    testImplementation(libs.junit)
    testImplementation (libs.mockk)
//    testImplementation(libs.mockito.core)
//    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)



    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.serialization)
    // LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)
    //Hilt
    implementation(libs.hilt.android)
    ksp (libs.hilt.compiler)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    // define a BOM and its version
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    //Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    // Icons
    implementation (libs.androidx.material.icons.core)
    implementation (libs.androidx.material.icons.extended)

}


androidComponents {
    beforeVariants { variantBuilder ->
        if (variantBuilder.productFlavors.containsAll(listOf(environment to production)) && variantBuilder.buildType ==debug) {
            // Gradle ignores any variants that satisfy the conditions above.
            variantBuilder.enable = false
        }
    }
}

afterEvaluate {
    android.applicationVariants.all { variant ->
        if (variant.buildType.name == release) {
            val variantName = variant.name.replaceFirstChar { it.uppercaseChar() }
            val bundleTaskName = "bundle${variantName}"
            val testTaskName = "test${variantName}UnitTest"
            tasks.named(bundleTaskName) {
                dependsOn(testTaskName)
            }
        }
        true
    }
    //To view the unit test report:
    //Open: app/build/reports/tests/test${variantName}UnitTest/index.html
}