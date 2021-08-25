package io.octatec.horext.api.config

import com.fasterxml.jackson.databind.Module
import org.ktorm.database.Database
import org.ktorm.database.SqlDialect
import org.ktorm.expression.SqlFormatter
import org.ktorm.jackson.KtormModule
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


@Configuration
class KtormConfiguration {
    @Autowired
    lateinit var dataSource: DataSource

    /**
     * Register the [Database] instance as a Spring bean.
     */
    @Bean
    fun database(): Database {
        return Database.connectWithSpringSupport(
            dataSource = dataSource,
            //logger = ConsoleLogger(threshold = LogLevel.DEBUG)
        )
    }

    /**
     * Register Ktorm's Jackson extension to the Spring's container
     * so that we can serialize/deserialize Ktorm entities.
     */
    @Bean
    fun ktormModule(): Module {
        return KtormModule()
    }
}