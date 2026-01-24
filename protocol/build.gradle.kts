plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

@Suppress("OPT_IN_USAGE")
kotlin {
    applyDefaultHierarchyTemplate()

    // Windows
    mingwX64()

    // Linux
    linuxArm64()
    linuxX64()

    // macOS
    macosArm64()
    macosX64()

    // iOS
    iosSimulatorArm64()
    iosArm64()
    iosX64()

    // watchOS
    watchosSimulatorArm64()
    watchosArm32()
    watchosArm64()
    watchosX64()

    // tvOS
    tvosSimulatorArm64()
    tvosArm64()
    tvosX64()

    // Android Native
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX64()
    androidNativeX86()

    // JVM
    jvm()

    // JS
    js {
        browser()
        nodejs()
    }

    // WASM
    wasmJs {
        browser()
        nodejs()
        d8()
    }
    wasmWasi {
        nodejs()
    }
}
