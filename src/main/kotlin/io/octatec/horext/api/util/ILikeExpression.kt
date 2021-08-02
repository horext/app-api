package io.octatec.horext.api.util

import org.ktorm.expression.ArgumentExpression
import org.ktorm.expression.ScalarExpression
import org.ktorm.schema.BooleanSqlType
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.schema.SqlType
import org.ktorm.schema.VarcharSqlType

data class ILikeExpression(
    val left: ScalarExpression<*>,
    val right: ScalarExpression<*>,
    override val sqlType: SqlType<Boolean> = BooleanSqlType,
    override val isLeafNode: Boolean = false
) : ScalarExpression<Boolean>() {
    override val extraProperties: Map<String, Any>
        get() = TODO("Not yet implemented")
}

infix fun ColumnDeclaring<*>.ilike(argument: String): ILikeExpression {
    return ILikeExpression(asExpression(), ArgumentExpression(argument, VarcharSqlType))
}