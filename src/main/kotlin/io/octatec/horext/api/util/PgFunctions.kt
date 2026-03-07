package io.octatec.horext.api.util

import org.jetbrains.exposed.v1.core.ComparisonOp
import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.ExpressionWithColumnType
import org.jetbrains.exposed.v1.core.Function
import org.jetbrains.exposed.v1.core.IColumnType
import org.jetbrains.exposed.v1.core.LikePattern
import org.jetbrains.exposed.v1.core.QueryBuilder
import org.jetbrains.exposed.v1.core.TextColumnType
import org.jetbrains.exposed.v1.core.VarCharColumnType
import org.jetbrains.exposed.v1.core.append
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.stringParam

class Unaccent<T : String?>(
    private val expression: Expression<T>,
) : Function<String>(TextColumnType()) {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        queryBuilder { append("unaccent(", expression, ")") }
    }
}

fun <T : String?> Expression<T>.unaccent(): Unaccent<T> = Unaccent(this)

class UnaccentString(
    val value: String,
) : Function<String>(TextColumnType()) {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        queryBuilder { append("unaccent(", stringParam(value), ")") }
    }

    override val columnType: IColumnType<String> = VarCharColumnType()
}

fun String.unaccent(): ExpressionWithColumnType<String> = UnaccentString(this)

class IlikeEscapeOp(
    expr1: Expression<*>,
    expr2: Expression<*>,
    like: Boolean,
    private val escapeChar: Char?,
) : ComparisonOp(expr1, expr2, if (like) "ILIKE" else "NOT ILIKE") {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        super.toQueryBuilder(queryBuilder)
        if (escapeChar != null) {
            with(queryBuilder) {
                append(" ESCAPE ", stringParam(escapeChar.toString()))
            }
        }
    }
}

// Pattern Matching

/** Checks if this expression matches the specified [pattern]. */
infix fun <T : String?> Expression<T>.ilike(pattern: String) = ilike(LikePattern(pattern))

/** Checks if this expression matches the specified [pattern]. */
infix fun <T : String?> Expression<T>.ilike(pattern: LikePattern): IlikeEscapeOp =
    IlikeEscapeOp(this, stringParam(pattern.pattern), true, pattern.escapeChar)

/** Checks if this expression matches the specified [pattern]. */
@JvmName("ilikeWithEntityID")
infix fun Expression<EntityID<String>>.ilike(pattern: String) = ilike(LikePattern(pattern))

/** Checks if this expression matches the specified [pattern]. */
@JvmName("ilikeWithEntityID")
infix fun Expression<EntityID<String>>.ilike(pattern: LikePattern): IlikeEscapeOp =
    IlikeEscapeOp(this, stringParam(pattern.pattern), true, pattern.escapeChar)

/** Checks if this expression matches the specified [expression]. */
infix fun <T : String?> Expression<T>.ilike(expression: ExpressionWithColumnType<String>): IlikeEscapeOp =
    IlikeEscapeOp(this, expression, true, null)

/** Checks if this expression matches the specified [expression]. */
@JvmName("ilikeWithEntityIDAndExpression")
infix fun Expression<EntityID<String>>.ilike(expression: ExpressionWithColumnType<String>): IlikeEscapeOp =
    IlikeEscapeOp(this, expression, true, null)
