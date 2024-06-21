<<<<<<< HEAD
buildscript {
    dependencies {
        classpath(libs.google.services)
    }
}
=======
>>>>>>> 488b164a06a7be2ed7c9159c36100d5bfa872c4f
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
<<<<<<< HEAD
=======
    id("com.google.gms.google-services") version "4.4.2" apply false
>>>>>>> 488b164a06a7be2ed7c9159c36100d5bfa872c4f
}