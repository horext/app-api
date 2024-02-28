package io.octatec.horext.api.config

import com.fasterxml.jackson.databind.Module
import org.ktorm.database.Database
import org.ktorm.database.SqlDialect
import org.ktorm.expression.SqlFormatter
import org.ktorm.jackson.KtormModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


@Configuration
class KtormConfiguration {
    @Autowired
    lateinit var dataSource: DataSource

    @Bean
    fun database(): Database {
        return Database.connectWithSpringSupport(
            dataSource = dataSource,
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