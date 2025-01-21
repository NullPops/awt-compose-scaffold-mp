import org.gradle.kotlin.dsl.withType
import org.jreleaser.gradle.plugin.tasks.JReleaserDeployTask
import org.jreleaser.model.Active

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    `maven-publish`
    id("org.jreleaser")
}

group = "io.github.nullpops"
version = "1.0.0"

android {
    namespace = "io.github.nullpops"
    compileSdk = 36

    defaultConfig {
        minSdk = 30
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_23
        targetCompatibility = JavaVersion.VERSION_23
    }
    kotlinOptions {
        jvmTarget = "23"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(project(":scaffold-common"))

    implementation("io.github.nullpops:android-awt:1.0.1")
    implementation("io.github.nullpops:logger:1.0.2")
    implementation("io.github.nullpops:properties:1.0.2")

    with(libs) {
        implementation(eventbus)
        implementation(exoplayer)
        implementation(firebase.analytics)
        implementation(firebase.crashlytics)
        implementation(google.firebase.analytics)
        implementation(kotlin.reflect)
        implementation(line.awesome)
        implementation(gson)
        implementation(androidx.core.ktx)
        implementation(androidx.lifecycle.runtime.ktx)
        implementation(androidx.activity.compose)
        implementation(androidx.ui)
        implementation(androidx.ui.graphics)
        implementation(androidx.ui.tooling.preview)
        implementation("androidx.compose.material3:material3-android:1.4.0-beta02")
        implementation(platform(firebase.bom))
        implementation(platform(androidx.compose.bom))

        testImplementation(junit)

        androidTestImplementation(androidx.junit)
        androidTestImplementation(androidx.espresso.core)
        androidTestImplementation(androidx.ui.test.junit4)
        androidTestImplementation(platform(androidx.compose.bom))

        debugImplementation(androidx.ui.tooling)
        debugImplementation(androidx.ui.test.manifest)
    }
}

// TODO: hook up dokka
tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
}


publishing {
    repositories {
        maven {
            name = "staging"
            url = uri(layout.buildDirectory.dir("staging-deploy"))
        }
    }
    publications {
        register<MavenPublication>("release") {
            afterEvaluate {
                from(components["release"])
            }
            artifact(tasks.named("javadocJar"))

            groupId = "io.github.nullpops"
            artifactId = "scaffold-android"
            version = project.version.toString()

            pom {
                name.set("scaffold-android")
                description.set("Android library that spoofs java.awt APIs for compatibility")
                url.set("https://github.com/NullPops/awt-compose-scaffold-mp")
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
                        id.set("zeruth"); name.set("Tyler Bochard"); email.set("tylerbochard@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/NullPops/awt-compose-scaffold-mp.git")
                    developerConnection.set("scm:git:ssh://git@github.com/NullPops/awt-compose-scaffold-mp.git")
                    url.set("https://github.com/NullPops/awt-compose-scaffold-mp")
                }
            }
        }
    }
}

// Ensure the staged repo exists before JReleaser deploys
tasks.withType<JReleaserDeployTask>().configureEach {
    outputs.upToDateWhen { false }
    dependsOn("publishReleasePublicationToStagingRepository")
}

jreleaser {
    project {
        name.set("awt-compose-scaffold-mp")
        version.set(project.version.get())
        description.set("Android library that spoofs java.awt APIs for compatibility")
        links { homepage.set("https://github.com/NullPops/awt-compose-scaffold-mp") }
    }

    signing {
        active.set(Active.ALWAYS)
        armored.set(true)
        mode.set(org.jreleaser.model.Signing.Mode.MEMORY)
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
                create("central") {
                    active.set(Active.ALWAYS)
                    url.set("https://central.sonatype.com/api/v1/publisher")
                    applyMavenCentralRules.set(false)
                    verifyPom.set(false)
                    sign.set(true)
                    checksums.set(true)
                    stagingRepository(layout.buildDirectory.dir("staging-deploy").get().asFile.absolutePath)
                    applyMavenCentralRules.set(true)
                }
            }
        }
    }
}