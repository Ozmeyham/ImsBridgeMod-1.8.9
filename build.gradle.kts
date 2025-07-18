// build.gradle.kts

import net.minecraftforge.gradle.forge.ForgeExtension
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.jvm.toolchain.JavaLanguageVersion

buildscript {
    repositories {
        maven("https://plugins.gradle.org/m2/")                 // for Shadow plugin
        maven("https://maven.minecraftforge.net/")                // ForgeGradle
        mavenCentral()
    }
    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:2.1-SNAPSHOT")
        classpath("com.github.jengelman.gradle.plugins:shadow:8.1.1")
    }
}

plugins {
    java
    idea
}

apply(plugin = "net.minecraftforge.gradle.forge")
apply(plugin = "com.github.johnrengelman.shadow")

// ── PROJECT COORDINATES ─────────────────────────────────────────────────────
group = "com.yourname.modid"         // ← your package here
version = "1.0"                      // ← your mod version here
val archivesBaseName = "modid"       // ← your modid here

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

configure<ForgeExtension> {
    // This is the 1.8.9 Forge MDK triple-version
    minecraft {
        version           = "1.8.9-11.15.1.2318-1.8.9"
        mappings          = "stable_22"
        runDir            = "run"
        makeObfSourceJar  = false
    }
}

repositories {
    mavenCentral()
    maven("https://maven.minecraftforge.net/")
}

// ── SHADOW CONFIGURATION ────────────────────────────────────────────────────
val shadowImpl: Configuration by configurations.creating {
    // extend your normal implementation classpath
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    // Remapped-for-you at compile+runtime (dev & prod)
    add("deobfCompile", "org.java-websocket:Java-WebSocket:1.5.4")
    // Also pull it into our custom shadow config so it actually ends up inside the JAR
    add(shadowImpl.name, "org.java-websocket:Java-WebSocket:1.5.4")
}

tasks {
    processResources {
        inputs.property("version", project.version)
        inputs.property("mcversion",
            project.extensions.getByType<ForgeExtension>().minecraft.version
        )

        // Expand mcmod.info
        from(sourceSets["main"].resources.srcDirs) {
            include("mcmod.info")
            expand(
                "version"   to project.version,
                "mcversion" to project.extensions.getByType<ForgeExtension>().minecraft.version
            )
        }
        from(sourceSets["main"].resources.srcDirs) {
            exclude("mcmod.info")
        }
    }

    // Build the “dev” shadow JAR (contains WebSocket client)
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set(archivesBaseName)
        archiveClassifier.set("all-dev")
        configurations = listOf(shadowImpl)
    }

    // ForgeGradle will automatically re-obfuscate (reobf) your normal jar,
    // so we hook shadowJar → reobfJar → final “modid-1.0.jar”
    named("reobfJar") {
        dependsOn("shadowJar")
        // the FG plugin will look at the shadowJar's output and reobf it
    }

    // Make assemble depend on reobfJar
    assemble {
        dependsOn("reobfJar")
    }
}
