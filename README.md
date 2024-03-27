# Horext App API

> Horext App API built using [Spring Boot](https://spring.io/projects/spring-boot), [Gradle](https://gradle.org/), [Java JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html), [Exposed](https://github.com/JetBrains/Exposed/wiki), and [PostgreSQL](https://www.postgresql.org/).

## Getting Started

### Development setup

#### Prerequisites

Requirements to run the project:

- [Java JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Gradle](https://gradle.org/)
- [PostgreSQL](https://www.postgresql.org/)

Optional requirements:

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Dev Container](https://code.visualstudio.com/docs/remote/containers)

#### Running the project

> For Windows use: `gradlew` instead of `./gradlew`

Initialize the database by running the following command:

```bash
./gradlew flywayMigrate -i
```

For live reload, run the following command (recommended):

```bash
./gradlew compileKotlin --continuous --parallel --build-cache --configuration-cache
```

Next, run the following command to start the application:

```bash
./gradlew bootRun
```

### Reference Documentation

For further reference, please consider the following sections:

- [Official Gradle documentation](https://docs.gradle.org)
- [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.3/gradle-plugin/reference/html/)
- [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.3/gradle-plugin/reference/html/#build-image)
- [GraalVM Native Image Support](https://docs.spring.io/spring-boot/docs/3.2.3/reference/html/native-image.html#native-image)
- [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.2.3/reference/htmlsingle/index.html#using.devtools)
- [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.3/reference/htmlsingle/index.html#web)
- [JDBC API](https://docs.spring.io/spring-boot/docs/3.2.3/reference/htmlsingle/index.html#data.sql)

### Guides

The following guides illustrate how to use some features concretely:

- [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
- [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
- [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
- [Accessing Relational Data using JDBC with Spring](https://spring.io/guides/gs/relational-data-access/)
- [Managing Transactions](https://spring.io/guides/gs/managing-transactions/)

### Additional Links

These additional references should also help you:

- [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
- [Configure AOT settings in Build Plugin](https://docs.spring.io/spring-boot/docs/3.2.3/gradle-plugin/reference/htmlsingle/#aot)
