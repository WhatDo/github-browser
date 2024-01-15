package dk.appdo.repo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dk.appdo.models.Repository
import dk.appdo.repo.response.GithubRepositoryResponse
import dk.appdo.repo.response.PaginatedResponse
import dk.appdo.repo.response.toDomain
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Headers
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import okio.IOException
import timber.log.Timber

class SearchRepositoriesPagingSource internal constructor(
    private val client: HttpClient,
    private val query: String
) : PagingSource<Int, Repository>() {
    override fun getRefreshKey(state: PagingState<Int, Repository>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repository> {
        val pageNumber = params.key
        val url = URLBuilder("$GithubApi/search/repositories").apply {
            parameters.append("q", query)
            parameters.append("per_page", params.loadSize.toString())
            if (pageNumber != null) {
                parameters.append("page", pageNumber.toString())
            }
        }.build()

        return try {
            val response = client.get(url)
            val body = response.body<PaginatedResponse<GithubRepositoryResponse>>()
            val links = response.headers.getLinks()

            LoadResult.Page(
                data = body.items.mapNotNull(GithubRepositoryResponse::toDomain),
                prevKey = links.prev?.parameters?.get("page")?.toInt(),
                nextKey = links.next?.parameters?.get("page")?.toInt()
            )
        } catch (e: IOException) {
            Timber.e(e)
            LoadResult.Error(e)
        }
    }
}

private fun Headers.getLinks(): Links {
    val links = get("links")?.run {
        split(",").mapNotNull(LinkRegex::find)
            .map { val (_, link, rel) = it.destructured; rel to Url(link) }
    } ?: return Links()

    return Links(
        prev = links.find { it.first == "prev" }?.second,
        next = links.find { it.first == "next" }?.second
    )
}

private val LinkRegex = """<(.*)>; rel="(.*)""".toRegex()

private class Links(
    val prev: Url? = null,
    val next: Url? = null
)