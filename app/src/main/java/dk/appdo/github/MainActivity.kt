package dk.appdo.github

import android.os.Bundle
import android.widget.SearchView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dk.appdo.details.RepoScreen
import dk.appdo.github.ui.theme.DawngithubbrowserTheme
import dk.appdo.navigation.Destinations
import dk.appdo.search.SearchScreen
import dk.appdo.search.SearchViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}


@Composable
private fun App() {
    DawngithubbrowserTheme {
        NavHost(
            navController = rememberNavController(),
            startDestination = Destinations.SEARCH
        ) {
            composable(Destinations.SEARCH) {
                SearchScreen(remember { SearchViewModel() })
            }

            composable(Destinations.DETAILS) {
                RepoScreen()
            }
        }
    }
}