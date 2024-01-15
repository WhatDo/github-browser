package dk.appdo.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.appdo.models.Repository
import dk.appdo.repo.GithubRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class RepoDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val githubRepo: GithubRepository
) : ViewModel() {
    private val user = savedStateHandle.get<String>("user")!!
    private val repo = savedStateHandle.get<String>("repo")!!

    private val _state = MutableStateFlow<RepoDetailsViewState>(RepoDetailsViewState.Loading)
    val state = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = RepoDetailsViewState.Loading
            val repository = async { githubRepo.getRepository(user, repo) }
            val latestRelease = async { githubRepo.getLatestRelease(user, repo) }
            _state.value = repository.await().fold(
                ifLeft = { RepoDetailsViewState.Error },
                ifRight = { repo ->
                    RepoDetailsViewState.Success(
                        RepoDetails(
                            repo,
                            latestRelease.await().getOrNull()
                        )
                    )
                }
            )
        }
    }
}

sealed class RepoDetailsViewState() {
    data object Loading : RepoDetailsViewState()
    data object Error : RepoDetailsViewState()
    data class Success(val data: RepoDetails) : RepoDetailsViewState()
}

data class RepoDetails(
    val repo: Repository,
    val latestRelease: String?
)