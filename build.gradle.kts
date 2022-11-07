plugins {
    id("java")
}

group = "ua.pz33"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:20.1.0")
    implementation("org.junit.jupiter:junit-jupiter:5.9.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}