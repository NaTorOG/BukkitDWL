plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

group = "net.pino"
val version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
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
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = "17"
    targetCompatibility = "17"
    options.encoding = "UTF-8"
}

