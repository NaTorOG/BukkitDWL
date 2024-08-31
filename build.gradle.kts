plugins {
    id("java")
    id("maven-publish")
}

group = "net.pino"
val version = "1.0.0"

repositories {
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.1-R0.1-SNAPSHOT")
}
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "com.github.NaTorOG"
            artifactId = project.name
            version = "1.0.0"
        }
    }
}
tasks.named("publishToMavenLocal").configure {
    dependsOn("assemble")
}
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

