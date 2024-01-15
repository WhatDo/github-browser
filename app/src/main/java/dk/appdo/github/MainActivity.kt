package dk.appdo.github

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dk.appdo.details.RepoDetailsModule
import dk.appdo.details.RepoDetailsScreen
import dk.appdo.github.ui.theme.DawngithubbrowserTheme
import dk.appdo.navigation.Destinations
import dk.appdo.navigation.NavigationManager
import dk.appdo.navigation.NavigationModule
import dk.appdo.repo.RepoModule
import dk.appdo.search.SearchModule
import dk.appdo.search.SearchScreen
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.ksp.generated.module

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
    KoinApplication(
        application = {
            modules(networkModule)
            modules(RepoModule().module)
            modules(SearchModule().module)
            modules(RepoDetailsModule().module)
            modules(NavigationModule().module)
        }
    ) {
        val navManager = koinInject<NavigationManager>()
        val navController = rememberNavController()

        LaunchedEffect(Unit) {
            navManager.onNavigateTo { path ->
                navController.navigate(path)
            }
        }

        DawngithubbrowserTheme {
            NavHost(
                navController = navController,
                startDestination = Destinations.SEARCH
            ) {
                composable(Destinations.SEARCH) {
                    SearchScreen()
                }

                composable(Destinations.DETAILS) {
                    RepoDetailsScreen()
                }
            }
        }
    }
}