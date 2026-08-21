package db.csv

enum class ReferencePolicy {
    REQUIRE_EXISTING,
    CREATE_IF_MISSING,
    OPTIONAL,
}

data class ReferenceRule<T, K>(
    val name: String,
    val key: (T) -> K?,
    val policy: ReferencePolicy,
    val maxCreatedPerImport: Int = 0,
) {
    init {
        require(name.isNotBlank())
        require(maxCreatedPerImport >= 0)
        require(policy == ReferencePolicy.CREATE_IF_MISSING || maxCreatedPerImport == 0) {
            "only CREATE_IF_MISSING references may set maxCreatedPerImport"
        }
        require(policy != ReferencePolicy.CREATE_IF_MISSING || maxCreatedPerImport > 0) {
            "CREATE_IF_MISSING references require a positive maxCreatedPerImport"
        }
    }
}
