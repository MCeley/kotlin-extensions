# Kotlin Extensions

Just a simple collection of kotlin extensions for various purposes.

[![](https://jitpack.io/v/MCeley/kotlin-extensions.svg)](https://jitpack.io/#MCeley/kotlin-extensions)

## Download

### Step 1.

### Prior to Gradle 7.0:
Add the JitPack repository in your root `build.gradle` at the end of `repositories`:
```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

### Gradle 7.0+:
Add the JitPack repository in your `settings.gradle` file in the `repositories` block of `dependencyResolutionManagement`:
```groovy
dependencyResolutionManagement {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

### Step 2.
Add the dependency in your project level `build.gradle` file.
```groovy
dependencies {
    implementation 'com.github.MCeley:kotlin-extensions:1.1.0'
}
```