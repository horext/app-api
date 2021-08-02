package io.octatec.horext.api.util

import org.ktorm.database.Database
import org.ktorm.expression.QueryExpression
import org.ktorm.expression.SqlExpression
import org.ktorm.expression.SqlFormatter
import org.ktorm.support.postgresql.PostgreSqlFormatter

class CustomSqlFormatter(database: Database, beautifySql: Boolean, indentSize: Int)
    : PostgreSqlFormatter(database, beautifySql, indentSize) {

    override fun visitUnknown(expr: SqlExpression): SqlExpression {
        if (expr is ILikeExpression) {
            if (expr.left.removeBrackets) {
                visit(expr.left)
            } else {
                write("(")
                visit(expr.left)
                removeLastBlank()
                write(") ")
            }

            write("ilike ")

            if (expr.right.removeBrackets) {
                visit(expr.right)
            } else {
                write("(")
                visit(expr.right)
                removeLastBlank()
                write(") ")
            }

            return expr
        } else {
           return super.visitUnknown(expr)
        }
    }
}