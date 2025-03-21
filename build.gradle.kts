val ktlint by configurations.creating

plugins {
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "0.10.6"
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.spring") version "2.1.10"
    id("org.flywaydb.flyway") version "11.4.0"
}

group = "io.octatec.horext"
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
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:0.53.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.53.0")
    implementation("org.flywaydb:flyway-core:11.4.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    ktlint("com.pinterest.ktlint:ktlint-cli:1.5.0") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:11.4.1")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

fun getEnvLines(): List<String> {
    val envFile = file(".env")
    return if (envFile.exists()) {
        envFile.readLines()
    } else {
        emptyList()
    }
}

tasks.withType<JavaExec> {
    doFirst {
        val envLines = getEnvLines()
        for (envLine in envLines) {
            val (key, value) = envLine.split("=", limit = 2)
            environment[key] = value
        }
    }
}

fun getEnv(key: String): String? {
    val envLines = getEnvLines()
    for (envLine in envLines) {
        val (envKey, envValue) = envLine.split("=", limit = 2)
        if (envKey == key) {
            return envValue
        }
    }
    return System.getenv(key)
}

flyway {	
    password = getEnv("SPRING_DATASOURCE_PASSWORD")
    url = getEnv("SPRING_DATASOURCE_URL")
    user = getEnv("SPRING_DATASOURCE_USERNAME")
    driver = "org.postgresql.Driver"
    schemas = arrayOf(getEnv("SPRING_DATASOURCE_SCHEMA"))
    locations = arrayOf("classpath:db/migration")
}

val ktlintCheck by tasks.registering(JavaExec::class) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
    args(
        "**/src/**/*.kt",
        "**.kts",
        "!**/build/**",
    )
}

tasks.check {
    dependsOn(ktlintCheck)
}

tasks.register<JavaExec>("ktlintFormat") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style and format"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
    args(
        "-F",
        "**/src/**/*.kt",
        "**.kts",
        "!**/build/**",
    )
}
