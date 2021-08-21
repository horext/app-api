package io.octatec.horext.api.util

import org.ktorm.expression.ArgumentExpression
import org.ktorm.expression.FunctionExpression
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.schema.VarcharSqlType




public fun ColumnDeclaring<String>.unaccent(): FunctionExpression<String> {
    // unaccent(str)
    return FunctionExpression(
        functionName = "unaccent",
        arguments = listOf(this.asExpression()),
        sqlType = VarcharSqlType
    )
}

public fun ColumnDeclaring<String>.lower(): FunctionExpression<String> {
    // lower(str)
    return FunctionExpression(
        functionName = "lower",
        arguments = listOf(this.asExpression()),
        sqlType = VarcharSqlType
    )
}

fun concat(separator: String, vararg params: ColumnDeclaring<String>): FunctionExpression<String> {
    return FunctionExpression(
        functionName = "concat_ws",
        arguments = listOf(ArgumentExpression(separator, VarcharSqlType)) + params.map { it.asExpression() },
        sqlType = VarcharSqlType
    )
}

public fun String.unaccent(): FunctionExpression<String> {
    // unaccent(str)
    return FunctionExpression(
        functionName = "unaccent",
        arguments = listOf(
            ArgumentExpression(this, VarcharSqlType)
        ),
        sqlType = VarcharSqlType
    )
}


