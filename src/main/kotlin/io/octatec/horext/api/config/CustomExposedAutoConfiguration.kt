package io.octatec.horext.api.config

import org.jetbrains.exposed.spring.DatabaseInitializer
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.LongColumnType
import org.jetbrains.exposed.sql.VarCharColumnType
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.aot.hint.TypeReference
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.Collections
import javax.sql.DataSource

/**
 * Custom configuration class for Exposed that is compatible with Spring Boot AOT.
 *
 * This is a reimplementation of ExposedAutoConfiguration without the problematic
 * @AutoConfiguration(after = [DataSourceAutoConfiguration::class]) annotation
 * that causes ClassNotFoundException during AOT processing.
 *
 * @property applicationContext The Spring ApplicationContext container responsible for managing beans.
 */
@Configuration
@EnableTransactionManagement
@ImportRuntimeHints(CustomExposedAutoConfiguration.ExposedAutoConfigurationRuntimeHints::class)
class CustomExposedAutoConfiguration(private val applicationContext: ApplicationContext) {

    @Value("\${spring.exposed.excluded-packages:}#{T(java.util.Collections).emptyList()}")
    private lateinit var excludedPackages: List<String>

    @Value("\${spring.exposed.show-sql:false}")
    private var showSql: Boolean = false

    /**
     * Returns a [SpringTransactionManager] instance using the specified [datasource] and [databaseConfig].
     *
     * To enable logging of all transaction queries by the SpringTransactionManager instance, set the property
     * `spring.exposed.show-sql` to `true` in the application.properties file.
     */
    @Bean
    fun springTransactionManager(datasource: DataSource, databaseConfig: DatabaseConfig): SpringTransactionManager {
        return SpringTransactionManager(datasource, databaseConfig, showSql)
    }

    /**
     * Database config with default values
     */
    @Bean
    @ConditionalOnMissingBean(DatabaseConfig::class)
    fun databaseConfig(): DatabaseConfig {
        return DatabaseConfig {}
    }

    /**
     * Returns a [DatabaseInitializer] that auto-creates the database schema, if enabled by the property
     * `spring.exposed.generate-ddl` in the application.properties file.
     *
     * The property `spring.exposed.excluded-packages` can be used to ensure that tables in specified packages are
     * not auto-created.
     */
    @Bean
    @ConditionalOnProperty("spring.exposed.generate-ddl", havingValue = "true", matchIfMissing = false)
    fun databaseInitializer() = DatabaseInitializer(applicationContext, excludedPackages)

    /**
     * Runtime hints for GraalVM native image compilation and Spring AOT processing.
     */
    class ExposedAutoConfigurationRuntimeHints : RuntimeHintsRegistrar {
        override fun registerHints(
            hints: RuntimeHints,
            classLoader: ClassLoader?,
        ) {
            // see https://github.com/spring-projects/spring-boot/issues/34206
            // see https://github.com/JetBrains/Exposed/issues/1274
            hints.reflection().registerType(Collections::class.java, *MemberCategory.entries.toTypedArray())
            hints
                .reflection()
                .registerType(
                    TypeReference.of("java.util.Collections\$Entry"),
                    *MemberCategory.entries.toTypedArray(),
                )
            hints.reflection().registerType(LongColumnType::class.java, *MemberCategory.entries.toTypedArray())
            hints.reflection().registerType(VarCharColumnType::class.java, *MemberCategory.entries.toTypedArray())
        }
    }
}
