import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.2.0"
    id("org.jetbrains.intellij.platform") version "2.7.0"
}

group = "com.moseoh"
version = "0.2.1"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }

}
dependencies {
    intellijPlatform {
        intellijIdeaUltimate("2025.2")

        bundledPlugin("com.intellij.java")
        pluginVerifier()
        zipSigner()

        testFramework(TestFrameworkType.Platform)
    }

    implementation("org.freemarker:freemarker:2.3.34")
    implementation("org.jsoup:jsoup:1.18.3")

    testImplementation("io.mockk:mockk:1.13.16")
    
    // JUnit 5 (Jupiter) - complete test framework
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:6.0.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
}

kotlin {
    jvmToolchain(21)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    patchPluginXml {
        sinceBuild.set("231")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("INTELLIJ_PUBLISH_TOKEN"))
        channels.set(listOf("stable"))
    }
}

fun bumpVersion(type: String) {
    val file = file("build.gradle.kts")
    val content = file.readText()
    val regex = """version = "(\d+)\.(\d+)\.(\d+)"""".toRegex()
    val match = regex.find(content) ?: error("Version not found")
    val (major, minor, patch) = match.destructured.toList().map { it.toInt() }

    val (newMajor, newMinor, newPatch) = when (type) {
        "major" -> Triple(major + 1, 0, 0)
        "minor" -> Triple(major, minor + 1, 0)
        else -> Triple(major, minor, patch + 1)
    }

    val newVersion = "$newMajor.$newMinor.$newPatch"
    val newContent = content.replace(match.value, """version = "$newVersion"""")
    file.writeText(newContent)
    println("$major.$minor.$patch → $newVersion")
}

tasks.register("bumpPatch") {
    group = "versioning"
    description = "Bump patch version (0.0.x)"
    doLast { bumpVersion("patch") }
}

tasks.register("bumpMinor") {
    group = "versioning"
    description = "Bump minor version (0.x.0)"
    doLast { bumpVersion("minor") }
}

tasks.register("bumpMajor") {
    group = "versioning"
    description = "Bump major version (x.0.0)"
    doLast { bumpVersion("major") }
}

tasks.register("printVersion") {
    group = "versioning"
    description = "Print current version"
    doLast { println(version) }
}
