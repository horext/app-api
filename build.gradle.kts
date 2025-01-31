plugins {
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.graalvm.buildtools.native") version "0.10.4"
	kotlin("jvm") version "2.1.10"
	kotlin("plugin.spring") version "2.1.10"
	id("org.flywaydb.flyway") version "11.2.0"
}

group = "io.octatec.horext"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
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
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.jetbrains.exposed:exposed-spring-boot-starter:0.58.0")
	implementation("org.jetbrains.exposed:exposed-java-time:0.58.0")
	implementation("org.flywaydb:flyway-core:11.3.0")
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:11.2.0")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

graalvmNative {
    binaries.all {
        resources.autodetect()
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
