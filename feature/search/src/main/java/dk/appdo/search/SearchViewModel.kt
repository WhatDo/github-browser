package dk.appdo.search

import androidx.core.text.trimmedLength
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dk.appdo.models.Repository
import dk.appdo.navigation.Destinations
import dk.appdo.navigation.NavigationManager
import dk.appdo.repo.GithubRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cache
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SearchViewModel(
    private val repo: GithubRepository,
    private val navManager: NavigationManager
) : ViewModel() {

    private val query = MutableStateFlow("")

    private val pager = query.map { query ->
        if (query.trimmedLength() >= MinQueryLength) {
            SearchResultsState.Results(
                Pager(
                    config = PagingConfig(pageSize = 30)
                ) {
                    repo.searchRepositories(query)
                }.flow
                    .cachedIn(viewModelScope)
                    .onStart { emit(PagingData.empty()) }
                    .stateIn(viewModelScope)
            )
        } else {
            SearchResultsState.Idle
        }
    }

    val state = combine(
        query,
        pager
    ) { query, pager ->
        SearchViewState(
            query = query,
            results = pager
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SearchViewState())


    fun onSearchValueChanged(value: String) {
        query.value = value
    }

    fun onRepositorySelected(repository: Repository) {
        viewModelScope.launch {
            // track
            // navigate
            // validate
            navManager.navigateTo(
                Destinations.DETAILS
                    .replace("{user}", repository.user.name)
                    .replace("{repo}", repository.name)
            )
        }
    }
}

data class SearchViewState(
    val query: String = "",
    val results: SearchResultsState = SearchResultsState.Idle
)

sealed class SearchResultsState {
    data object Idle : SearchResultsState()
    data class Results(
        val results: StateFlow<PagingData<Repository>>
    ) : SearchResultsState()
}

private const val MinQueryLength = 3


interface SearchNavigation {
    fun viewRepoWeb()
}

class GithubNavgiation
class GitlabNavgiation
class BitbucketNavgiation
