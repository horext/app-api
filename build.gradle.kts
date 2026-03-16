val ktlint by configurations.creating

plugins {
    kotlin("jvm") version "2.3.10"
    kotlin("plugin.spring") version "2.3.10"
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "0.11.5"
    id("org.flywaydb.flyway") version "12.1.0"
}

group = "io.octatec.horext"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.jetbrains.exposed:exposed-spring-boot4-starter:1.1.1")
    implementation("org.jetbrains.exposed:exposed-java-time:1.1.1")
    implementation("org.flywaydb:flyway-core:12.1.0")
    testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
    testImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
    testImplementation("org.springframework.boot:spring-boot-starter-jdbc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    ktlint("com.pinterest.ktlint:ktlint-cli:1.8.0") {
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
        classpath("org.flywaydb:flyway-database-postgresql:12.1.0")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
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
    schemas = listOfNotNull(getEnv("SPRING_DATASOURCE_SCHEMA")).toTypedArray()
    locations = arrayOf("classpath:db/migration")
    configurations = arrayOf("runtimeClasspath")
}

tasks.withType<org.flywaydb.gradle.task.AbstractFlywayTask>().configureEach {
    dependsOn(tasks.named("classes"))
}

val ktlintTargets =
    listOf(
        "src/**/*.kt",
        "src/**/*.kts",
        "*.kts",
        "!**/build/**",
    )

fun JavaExec.configureKtlintTask(
    extraArgs: List<String> = emptyList(),
    alwaysRun: Boolean = false,
) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")

    inputs.files(
        fileTree(projectDir) {
            include("src/**/*.kt", "src/**/*.kts", "*.kts")
            exclude("**/build/**")
        },
    )

    if (alwaysRun) {
        // Formatting is an explicit command; always run it when requested.
        outputs.upToDateWhen { false }
    }

    // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
    args(extraArgs + ktlintTargets)
}

val ktlintCheck by tasks.registering(JavaExec::class) {
    description = "Check Kotlin code style"
    configureKtlintTask()
}

tasks.check {
    dependsOn(ktlintCheck)
}

tasks.register<JavaExec>("ktlintFormat") {
    description = "Check Kotlin code style and format"
    configureKtlintTask(extraArgs = listOf("-F"), alwaysRun = true)
}
