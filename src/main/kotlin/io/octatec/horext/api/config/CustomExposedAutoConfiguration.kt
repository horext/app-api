package io.octatec.horext.api.config

import org.jetbrains.exposed.v1.core.IntegerColumnType
import org.jetbrains.exposed.v1.core.LongColumnType
import org.jetbrains.exposed.v1.core.VarCharColumnType
import org.jetbrains.exposed.v1.core.dao.id.EntityIDColumnType
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints

@Configuration(proxyBeanMethods = false)
@ImportRuntimeHints(CustomExposedRuntimeHints::class)
class CustomExposedAutoConfiguration

class CustomExposedRuntimeHints : RuntimeHintsRegistrar {
    override fun registerHints(
        hints: RuntimeHints,
        classLoader: ClassLoader?,
    ) {
        val members =
            arrayOf(
                MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
            )

        // Exposed 1.1.1 native image gap: these types are used via reflection during table cloning/reference.
        hints.reflection().registerType(LongColumnType::class.java, *members)
        hints.reflection().registerType(IntegerColumnType::class.java, *members)
        hints.reflection().registerType(
            VarCharColumnType::class.java,
            *members,
            MemberCategory.INVOKE_PUBLIC_METHODS,
        )
        hints.reflection().registerType(EntityIDColumnType::class.java, *members)
    }
}
