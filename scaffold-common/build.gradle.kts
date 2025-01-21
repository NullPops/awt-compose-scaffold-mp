import org.gradle.kotlin.dsl.withType
import org.jreleaser.gradle.plugin.tasks.JReleaserDeployTask
import org.jreleaser.model.Active
import org.jreleaser.model.Signing

plugins {
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    kotlin("jvm")
    `java-library`
    `maven-publish`
    id("org.jreleaser")
}

group = "io.github.nullpops"
version = "1.0.0"

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_23
    targetCompatibility = JavaVersion.VERSION_23
}

kotlin {
    jvmToolchain(23)
}

dependencies {
    implementation("io.github.nullpops:logger:1.0.2")
    implementation("io.github.nullpops:properties:1.0.2")

    compileOnly(files("../lib/android-35.jar"))

    with(compose) {
        compileOnly(runtime)
        compileOnly(ui)
        compileOnly(desktop.currentOs)
    }

    with(libs) {
        implementation(libs.androidx.annotation.jvm)
        compileOnly(kotlin.reflect)
        compileOnly(eventbus)
        compileOnly(gson)
        compileOnly(material3)
        compileOnly(line.awesome)
    }
}


tasks.withType<JReleaserDeployTask> {
    outputs.upToDateWhen { false }
    dependsOn("publish")
}

publishing {
    repositories {
        maven {
            name = "staging"
            url = uri("${layout.buildDirectory.asFile.get().path }/staging-deploy")
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {

            groupId = "io.github.nullpops"
            artifactId = "scaffold-common"
            version = project.version.toString()

            from(components["java"])
            pom {
                name.set("scaffold-common")
                description.set("JVM-mp Compose scaffold supporting AWT via emulation")
                url.set("https://github.com/nullpops/awt-compose-scaffold-mp")
                licenses {
                    license {
                        name.set("AGPL-3.0-only")
                        url.set("https://www.gnu.org/licenses/agpl-3.0.html")
                        distribution.set("repo")
                    }
                    license {
                        name.set("NullPops Commercial License")
                        url.set("https://github.com/NullPops/awt-compose-scaffold-mp/blob/main/LICENSE")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("zeruth")
                        name.set("Tyler Bochard")
                        email.set("tylerbochard@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/nullpops/awt-compose-scaffold-mp.git")
                    developerConnection.set("scm:git:ssh://github.com/nullpops/awt-compose-scaffold-mp.git")
                    url.set("https://github.com/nullpops/awt-compose-scaffold-mp")
                }
            }
        }
    }
}

jreleaser {
    signing {
        dryrun = true
        active.set(Active.ALWAYS)
        armored.set(true)
        mode = Signing.Mode.MEMORY
        providers.environmentVariable("JRELEASER_GPG_PUBLIC_KEY_PATH").orNull?.let {
            publicKey.set(file(it).readText())
        }
        providers.environmentVariable("JRELEASER_GPG_PRIVATE_KEY_PATH").orNull?.let {
            secretKey.set(file(it).readText())
        }

        passphrase.set(providers.environmentVariable("JRELEASER_GPG_PASSPHRASE"))
    }
    deploy {
        maven {
            mavenCentral {
                create("scaffold-common") {
                    active.set(Active.ALWAYS)
                    url.set("https://central.sonatype.com/api/v1/publisher")
                    stagingRepository("build/staging-deploy")
                }
            }
        }
    }
}