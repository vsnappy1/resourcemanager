// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    id("dev.randos.resourcemanager") version "0.0.1" apply false
}

ext {
    set("kspVersion", libs.versions.kspVersion.get())
    set("agp", libs.versions.agp.get())
}