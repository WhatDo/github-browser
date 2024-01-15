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
    val name: String,
    val description: String?,
    val owner: Owner,
    val forks_count: Int,
    val open_issues_count: Int,
    val stargazers_count: Int,
    val language: String?
) {
    @Serializable
    data class Owner(
        val login: String,
        val avatar_url: String
    )
}

internal fun GithubRepositoryResponse.toDomain() = try {
    Repository(
        id = RepoId(id),
        name = name,
        description = description,
        user = Repository.User(
            name = owner.login,
            icon = Url(owner.avatar_url)
        ),
        language = language,
        forks = forks_count,
        issues = open_issues_count,
        stars = stargazers_count
    )
} catch (e: URLParserException) {
    // received invalid url, ignore
    null
}

