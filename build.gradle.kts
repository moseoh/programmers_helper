import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.2.0"
    id("org.jetbrains.intellij.platform") version "2.7.0"
}

group = "com.moseoh"
version = "0.0.19"

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

    implementation("org.freemarker:freemarker:2.3.33")
    implementation("org.jsoup:jsoup:1.15.4")

    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.3")
    testImplementation("junit:junit:4.13.2")
}

kotlin {
    jvmToolchain(21)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    patchPluginXml {
        sinceBuild.set("231.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("INTELLIJ_PUBLISH_TOKEN"))
        channels.set(listOf(System.getenv("INTELLIJ_PUBLISH_CHANNEL")))
    }
}
