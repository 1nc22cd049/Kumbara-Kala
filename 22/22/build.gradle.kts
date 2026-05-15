plugins {
    id("com.android.application") version "8.13.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
}

val localBuildRoot = File(System.getProperty("user.home"), "AppData/Local/KumbaraKalaBuild")

layout.buildDirectory.set(localBuildRoot.resolve("root"))

subprojects {
    layout.buildDirectory.set(localBuildRoot.resolve(name))
}
