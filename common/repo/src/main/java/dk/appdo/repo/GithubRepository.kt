package dk.appdo.repo

import io.ktor.client.HttpClient
import org.koin.core.annotation.Single

@Single
class GithubRepository(
    private val client: HttpClient
) {

    fun searchRepositories(query: String): SearchRepositoriesPagingSource {
        return SearchRepositoriesPagingSource(client, query)
    }
}

internal const val GithubApi = "https://api.github.com"


