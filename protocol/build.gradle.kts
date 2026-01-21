plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

@Suppress("OPT_IN_USAGE")
kotlin {
    applyDefaultHierarchyTemplate()

    mingwX64()
    linuxArm64()
    linuxX64()
    macosArm64()
    macosX64()
    iosSimulatorArm64()
    iosArm64()
    iosX64()
    watchosSimulatorArm64()
    watchosArm32()
    watchosArm64()
    watchosX64()
    watchosDeviceArm64()
    tvosSimulatorArm64()
    tvosArm64()
    tvosX64()
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX64()
    androidNativeX86()

    jvm()
    
    js {
        browser()
        nodejs()
    }
    wasmJs {
        browser()
        nodejs()
        d8()
    }
    wasmWasi {
        nodejs()
    }
}
