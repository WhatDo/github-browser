package dk.appdo.github

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dk.appdo.details.RepoDetailsScreen
import dk.appdo.github.ui.theme.DawngithubbrowserTheme
import dk.appdo.navigation.Destinations
import dk.appdo.repo.RepoModule
import dk.appdo.search.SearchModule
import dk.appdo.search.SearchScreen
import org.koin.compose.KoinApplication
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
        }
    ) {
        DawngithubbrowserTheme {
            NavHost(
                navController = rememberNavController(),
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