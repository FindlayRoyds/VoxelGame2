import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val lwjglVersion = "3.3.3"
val lwjglNatives = "natives-macos-arm64"

plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    application
}

group = "me.findlay"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}



tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

application {
    mainClass.set("MainKt")
    applicationDefaultJvmArgs = listOf("-XstartOnFirstThread")
}

dependencies {
    implementation("org.lwjgl:lwjgl:3.3.3")
    implementation("org.lwjgl:lwjgl-glfw:3.3.3")
    implementation("org.lwjgl:lwjgl-opengl:3.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl:lwjgl")
    implementation ("org.lwjgl:lwjgl-assimp")
    implementation("org.lwjgl:lwjgl-bgfx")
    implementation("org.lwjgl:lwjgl-cuda")
    implementation("org.lwjgl:lwjgl-egl")
    implementation("org.lwjgl:lwjgl-fmod")
    implementation("org.lwjgl:lwjgl-freetype")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-harfbuzz")
    implementation("org.lwjgl:lwjgl-hwloc")
    implementation("org.lwjgl:lwjgl-jawt")
    implementation("org.lwjgl:lwjgl-jemalloc")
    implementation("org.lwjgl:lwjgl-ktx")
    implementation("org.lwjgl:lwjgl-libdivide")
    implementation("org.lwjgl:lwjgl-llvm")
    implementation("org.lwjgl:lwjgl-lmdb")
    implementation("org.lwjgl:lwjgl-lz4")
    implementation("org.lwjgl:lwjgl-meow")
    implementation("org.lwjgl:lwjgl-meshoptimizer")
    implementation("org.lwjgl:lwjgl-nanovg")
    implementation("org.lwjgl:lwjgl-nfd")
    implementation("org.lwjgl:lwjgl-nuklear")
    implementation("org.lwjgl:lwjgl-odbc")
    implementation("org.lwjgl:lwjgl-openal")
    implementation("org.lwjgl:lwjgl-opencl")
    implementation("org.lwjgl:lwjgl-opengl")
    implementation("org.lwjgl:lwjgl-opengles")
    implementation("org.lwjgl:lwjgl-opus")
    implementation("org.lwjgl:lwjgl-par")
    implementation("org.lwjgl:lwjgl-remotery")
    implementation("org.lwjgl:lwjgl-rpmalloc")
    implementation("org.lwjgl:lwjgl-shaderc")
    implementation("org.lwjgl:lwjgl-spvc")
    implementation("org.lwjgl:lwjgl-stb")
    implementation("org.lwjgl:lwjgl-tinyexr")
    implementation("org.lwjgl:lwjgl-tinyfd")
    implementation("org.lwjgl:lwjgl-vma")
    implementation("org.lwjgl:lwjgl-vulkan")
    implementation("org.lwjgl:lwjgl-xxhash")
    implementation("org.lwjgl:lwjgl-yoga")
    implementation("org.lwjgl:lwjgl-zstd")
    runtimeOnly("org.lwjgl:lwjgl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-assimp::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-bgfx::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-freetype::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-harfbuzz::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-hwloc::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-jemalloc::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-ktx::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-libdivide::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-llvm::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-lmdb::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-lz4::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-meow::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-meshoptimizer::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-nanovg::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-nfd::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-nuklear::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-openal::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengles::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opus::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-par::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-remotery::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-rpmalloc::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-shaderc::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-spvc::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-stb::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-tinyexr::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-tinyfd::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-vma::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-vulkan::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-xxhash::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-yoga::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-zstd::$lwjglNatives")

    implementation("org.joml:joml:1.10.5")
    // runtimeOnly("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    // implementation("io.github.spair:imgui-java-app:1.86.11")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    // Or shorter:
    jvmToolchain(17)
    // For example:
    jvmToolchain(17)
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    from({ configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) } })
}