package dk.appdo.search

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ReportFragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import dk.appdo.models.Repository
import dk.appdo.resources.R
import io.ktor.http.Url
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.compose.navigation.koinNavViewModel

@Composable
fun SearchScreen(vm: SearchViewModel = koinNavViewModel()) {
    val state by vm.state.collectAsStateWithLifecycle()
    SearchScreen(
        searchValue = state.query,
        onSearchValueChanged = vm::onSearchValueChanged,
        results = state.results,
        onRepositorySelected = vm::onRepositorySelected
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SearchScreen(
    searchValue: String,
    onSearchValueChanged: (String) -> Unit,
    results: SearchResultsState,
    onRepositorySelected: (Repository) -> Unit
) {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 20.dp)
                    .windowInsetsPadding(
                        WindowInsets.systemBars.only(WindowInsetsSides.Top)
                    )
            ) {
                Text(
                    text = stringResource(id = R.string.search_title),
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.size(20.dp))
                SearchBar(searchValue, onSearchValueChanged)
            }
        }
    ) { innerPadding ->
        val contentState =
            updateTransition(targetState = results, label = "Search Content Transition")

        contentState.Crossfade(contentKey = { it::class }) { resultsState ->
            when (resultsState) {
                SearchResultsState.Idle -> EmptySearchScreen(
                    modifier = Modifier.padding(innerPadding)
                )

                is SearchResultsState.Results -> {
                    val lazyItems = resultsState.results.collectAsLazyPagingItems()
                    when (lazyItems.loadState.refresh) {
                        is LoadState.Loading -> {
                            LoadingScreen()
                        }

                        is LoadState.Error -> {
                            ErrorScreen(
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        is LoadState.NotLoading -> {
                            SearchResultsList(
                                lazyItems,
                                onRepositorySelected,
                                contentPadding = innerPadding
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        decorationBox = { inner ->
            Row(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null
                )

                Box(modifier = Modifier.weight(1f)) {
                    Crossfade(
                        targetState = value.isEmpty(), label = "Search Hint Crossfade",
                    ) { visible ->
                        if (visible) {
                            Text(
                                text = stringResource(id = R.string.search_hint),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    inner()
                }
            }
        }
    )
}

@Composable
private fun SearchResultsList(
    items: LazyPagingItems<Repository>,
    onRepositorySelected: (Repository) -> Unit,
    contentPadding: PaddingValues
) {
    LazyColumn(
        contentPadding = contentPadding
    ) {
        items(items.itemCount) { idx ->
            val repo = items[idx]
            if (repo != null) {
                RepositoryRow(repository = repo, onRepositorySelected = onRepositorySelected)
            }
        }
    }
}

@Composable
fun RepositoryRow(
    repository: Repository,
    onRepositorySelected: (Repository) -> Unit,
    modifier: Modifier = Modifier
) {
    RepositoryRow(
        icon = repository.icon,
        name = repository.name,
        description = repository.description,
        onRepositorySelected = { onRepositorySelected(repository) },
        modifier = modifier
    )
}

@Composable
private fun RepositoryRow(
    icon: Url,
    name: String,
    description: String?,
    onRepositorySelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onRepositorySelected)
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(icon.toString()),
            contentDescription = null,
            modifier = Modifier.size(44.dp).clip(RoundedCornerShape(8.dp))
        )

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium
            )
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun EmptySearchScreen(modifier: Modifier = Modifier) {
    EmptyContentLayout(
        icon = painterResource(id = R.drawable.ic_empty_folder),
        title = stringResource(id = R.string.search_empty),
        subtitle = stringResource(id = R.string.search_empty_subtitle),
        modifier = modifier
    )
}

@Composable
private fun ErrorScreen(modifier: Modifier = Modifier) {
    EmptyContentLayout(
        icon = rememberVectorPainter(image = Icons.Default.Clear),
        title = stringResource(id = R.string.search_error),
        subtitle = stringResource(id = R.string.search_error_subtitle),
        modifier = modifier
    )
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    EmptyContentLayout(
        icon = { CircularProgressIndicator() },
        modifier = modifier
    )
}

@Composable
fun EmptyContentLayout(
    icon: Painter,
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
) {
    EmptyContentLayout(
        icon = {
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(52.dp)
            )
        },
        modifier = modifier,
        title = title,
        subtitle = subtitle
    )
}

@Composable
private fun EmptyContentLayout(
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        icon()
        Spacer(modifier = Modifier.size(28.dp))

        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.size(8.dp))
        }

        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(200.dp),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}