package dk.appdo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dk.appdo.resources.R


@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
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
fun EmptyContentLayout(
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
