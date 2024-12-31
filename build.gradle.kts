plugins {
    java
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "dev.samwells"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/software.amazon.awssdk/bom
    runtimeOnly("software.amazon.awssdk:bom:2.29.43")

    // https://mvnrepository.com/artifact/software.amazon.awssdk/dynamodb-enhanced
    implementation("software.amazon.awssdk:dynamodb-enhanced:2.29.43")

    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
