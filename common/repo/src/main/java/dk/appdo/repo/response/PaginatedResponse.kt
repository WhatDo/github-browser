package dk.appdo.repo.response

import kotlinx.serialization.Serializable

@Suppress("PropertyName")
@Serializable
data class PaginatedResponse<T>(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<T>
)

