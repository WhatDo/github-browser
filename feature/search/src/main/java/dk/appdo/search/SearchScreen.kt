package dk.appdo.search

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dk.appdo.resources.R

@Composable
fun SearchScreen(vm: SearchViewModel) {
    val state by vm.state.collectAsStateWithLifecycle()
    SearchScreen(
        searchValue = state.query,
        onSearchValueChanged = vm::onSearchValueChanged,
        results = state.results
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun SearchScreen(
    searchValue: String,
    onSearchValueChanged: (String) -> Unit,
    results: SearchResultsState
) {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
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

                SearchResultsState.Error -> ErrorScreen(
                    modifier = Modifier.padding(innerPadding)
                )

                SearchResultsState.Loading -> TODO()
                is SearchResultsState.Results -> TODO()
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
private fun EmptyContentLayout(
    icon: Painter,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(52.dp)
        )
        Spacer(modifier = Modifier.size(28.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(200.dp),
            color = MaterialTheme.colorScheme.secondary
        )
    }
}