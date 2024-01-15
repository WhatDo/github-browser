package dk.appdo.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class SearchViewModel : ViewModel() {

    private val query = MutableStateFlow("")

    val state = combine(
        query,
        flowOf(123)
    ) { query, _ ->
        SearchViewState(
            query = query,
            results = SearchResultsState.Idle
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SearchViewState())


    fun onSearchValueChanged(value: String) {
        query.value = value
    }
}

data class SearchViewState(
    val query: String = "",
    val results: SearchResultsState = SearchResultsState.Idle
)

sealed class SearchResultsState {
    data object Idle : SearchResultsState()
    data object Loading : SearchResultsState()
    data object Error : SearchResultsState()
    data class Results(
        val results: List<String>
    ) : SearchResultsState()
}