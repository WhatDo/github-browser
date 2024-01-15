package dk.appdo.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.annotation.Single

interface NavigationManager {
    suspend fun navigateTo(path: String)

    suspend fun onNavigateTo(block: (String) -> Unit)
}

@Single(binds = [NavigationManager::class])
internal class NavigationManagerImpl : NavigationManager {
    private val shared = MutableSharedFlow<String>()

    override suspend fun navigateTo(path: String) {
        shared.emit(path)
    }

    override suspend fun onNavigateTo(block: (String) -> Unit) {
        shared.collect {
            block(it)
        }
    }
}

