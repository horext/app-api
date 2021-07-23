package io.octatec.horext.api.fts

import org.hibernate.boot.MetadataBuilder
import org.hibernate.boot.spi.MetadataBuilderContributor
import org.hibernate.dialect.function.SQLFunctionTemplate
import org.hibernate.type.BooleanType

class SqlFunctionsMetadataBuilderContributor : MetadataBuilderContributor {

   override
   fun contribute(metadataBuilder: MetadataBuilder) {
        metadataBuilder.applySqlFunction(
            "fts",
            SQLFunctionTemplate(
                BooleanType.INSTANCE,
                "to_tsvector( unaccent( ?1 :: text ) ) @@ to_tsquery( 'simple' , unaccent( ?2 :: text ) || ':*' )"
            )
        )
    }
}