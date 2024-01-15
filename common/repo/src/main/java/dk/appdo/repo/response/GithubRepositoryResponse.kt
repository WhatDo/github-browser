@file:Suppress("PropertyName")

package dk.appdo.repo.response

import dk.appdo.models.RepoId
import dk.appdo.models.Repository
import io.ktor.http.URLParserException
import io.ktor.http.Url
import kotlinx.serialization.Serializable

@Serializable
internal data class GithubRepositoryResponse(
    val id: Long,
    val full_name: String,
    val description: String?,
    val owner: Owner
) {
    @Serializable
    data class Owner(
        val avatar_url: String
    )
}

internal fun GithubRepositoryResponse.toDomain() = try {
    Repository(
        id = RepoId(id),
        icon = Url(owner.avatar_url),
        name = full_name,
        description = description
    )
} catch (e: URLParserException) {
    // received invalid url, ignore
    null
}