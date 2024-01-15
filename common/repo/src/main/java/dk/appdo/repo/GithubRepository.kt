package dk.appdo.repo

import arrow.core.Either
import dk.appdo.models.Repository
import dk.appdo.repo.response.GithubReleaseResponse
import dk.appdo.repo.response.GithubRepositoryResponse
import dk.appdo.repo.response.toDomain
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Single

@Single
class GithubRepository(
    private val client: HttpClient
) {

    fun searchRepositories(query: String): SearchRepositoriesPagingSource {
        return SearchRepositoriesPagingSource(client, query)
    }

    suspend fun getRepository(user: String, name: String): Either<Throwable, Repository> = Either.catch {
        client.get("$GithubApi/repos/$user/$name")
            .body<GithubRepositoryResponse>()
            .toDomain()
            ?: throw RuntimeException("Something went wrong with the response")
    }

    suspend fun getLatestRelease(user: String, name: String): Either<Throwable, String?> = Either.catch {
        client.get("$GithubApi/repos/$user/$name/releases")
            .body<List<GithubReleaseResponse>>()
            .firstOrNull()
            ?.name
    }
}

internal const val GithubApi = "https://api.github.com"


