import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.room)
}

room3 {
    schemaDirectory("$projectDir/schemas")
}

val versionMajor = libs.versions.major.get().toInt()
val versionMinor = libs.versions.minor.get().toInt()
val versionBug = libs.versions.bug.get().toInt()
val versionString = "${versionMajor}.${versionMinor}.${versionBug}"

buildkonfig {
    packageName = "com.stingers.alttpr"

    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "VERSION_NAME", versionString)
        buildConfigField(FieldSpec.Type.STRING, "KTOR_VERSION", libs.versions.ktor.get())
        buildConfigField(FieldSpec.Type.STRING, "KTORFIT_VERSION", libs.versions.ktorfit.get())
        buildConfigField(FieldSpec.Type.STRING, "KOIN_VERSION", libs.versions.koin.get())
        buildConfigField(FieldSpec.Type.STRING, "COIL_VERSION", libs.versions.coil.get())
        buildConfigField(FieldSpec.Type.STRING, "FILEKIT_VERSION", libs.versions.filekit.get())
    }
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    jvm()

    android {
        namespace = "com.stingers.alttpr.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Koin & Koin Annotations
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.annotations)

            // File Picker
            implementation(libs.filekit.compose)
            implementation(libs.filekit.core)

            //Ktor Deps
            implementation(libs.ktorfit.lib)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.datetime)

            //Room Deps
            api(libs.androidx.paging.common)
            implementation(libs.androidx.paging.compose)
            implementation(libs.room.runtime)
            api(libs.room3.paging)
            implementation(libs.sqlite.bundled)

            // Navigation 3
            implementation(libs.navigation3.ui)

            // DataStore Preferences
            implementation(libs.androidx.datastore.preferences)

            //Coil
            // Coil Deps
            implementation(libs.coil)
            implementation(libs.coil.network.ktor)
            implementation(libs.coil.compose)
            implementation(libs.coil.svg)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
    add("kspAndroid", libs.room.compiler)
    add("kspAndroid", libs.room3.paging)
    add("kspAndroid", libs.androidx.paging.common)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room3.paging)
    add("kspIosSimulatorArm64", libs.androidx.paging.common)
    add("kspIosArm64", libs.room.compiler)
    add("kspIosArm64", libs.room3.paging)
    add("kspIosArm64", libs.androidx.paging.common)
    add("kspJvm", libs.room.compiler)
    add("kspJvm", libs.room3.paging)
    add("kspJvm", libs.androidx.paging.common)
}