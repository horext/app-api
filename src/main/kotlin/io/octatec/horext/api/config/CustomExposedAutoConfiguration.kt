package io.octatec.horext.api.config

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.jetbrains.exposed.sql.LongColumnType
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.aot.hint.TypeReference
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints
import java.util.*

@Configuration
@ImportAutoConfiguration(
    value = [ExposedAutoConfiguration::class]
)
@ImportRuntimeHints(CustomExposedAutoConfiguration.ExposedAutoConfigurationRuntimeHints::class)
open class CustomExposedAutoConfiguration {

    class ExposedAutoConfigurationRuntimeHints : RuntimeHintsRegistrar {
        override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
            // see https://github.com/spring-projects/spring-boot/issues/34206
            hints.reflection().registerType(Collections::class.java, *MemberCategory.entries.toTypedArray())
            hints.reflection()
                .registerType(
                    TypeReference.of("java.util.Collections\$Entry"),
                    *MemberCategory.entries.toTypedArray()
                )
            hints.reflection().registerType(LongColumnType::class.java, *MemberCategory.entries.toTypedArray())

        }
    }
}