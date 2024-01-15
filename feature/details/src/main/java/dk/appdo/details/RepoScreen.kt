package dk.appdo.details

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import dk.appdo.resources.R
import dk.appdo.ui.ErrorScreen
import dk.appdo.ui.LoadingScreen
import io.ktor.http.Url
import org.koin.androidx.compose.navigation.koinNavViewModel

@Composable
fun RepoDetailsScreen(vm: RepoDetailsViewModel = koinNavViewModel()) {
    val state by vm.state.collectAsStateWithLifecycle()
    when (val state = state) {
        is RepoDetailsViewState.Loading -> LoadingScreen()
        is RepoDetailsViewState.Error -> ErrorScreen()
        is RepoDetailsViewState.Success -> with(state.data) {
            RepoDetailsScreen(
                icon = repo.icon,
                fullName = repo.fullName,
                language = repo.language,
                forks = repo.forks,
                issues = repo.issues,
                stars = repo.stars,
                latestRelease = latestRelease
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RepoDetailsScreen(
    icon: Url,
    fullName: String,
    language: String?,
    forks: Int,
    issues: Int,
    stars: Int,
    latestRelease: String?
) {
    Scaffold(topBar = {
        val backHandler = LocalOnBackPressedDispatcherOwner.current
        TopAppBar(title = {}, navigationIcon = {
            IconButton(onClick = { backHandler?.onBackPressedDispatcher?.onBackPressed() }) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Default.ArrowBack),
                    contentDescription = null
                )
            }
        })
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = icon.toString()),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.size(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = fullName,
                    style = MaterialTheme.typography.titleMedium
                )
                if (language != null) {
                    Text(
                        text = language,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                    )
                }
            }

            Spacer(modifier = Modifier.size(32.dp))

            Column(
                modifier = Modifier
                    .border(
                        1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 20.dp)
            ) {
                ItemDetailRow(
                    name = stringResource(id = R.string.details_forks),
                    value = forks.toString()
                )
                Divider()
                ItemDetailRow(
                    name = stringResource(id = R.string.details_issues),
                    value = issues.toString()
                )
                Divider()
                ItemDetailRow(
                    name = stringResource(id = R.string.details_starred_by),
                    value = stars.toString()
                )
                Divider()
                ItemDetailRow(
                    name = stringResource(id = R.string.details_last_release),
                    value = latestRelease ?: stringResource(id = R.string.details_no_release)
                )
            }
        }
    }
}


@Composable
private fun ItemDetailRow(
    name: String,
    value: String
) {
    CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.bodyMedium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = name)
            Text(text = value)
        }
    }
}
