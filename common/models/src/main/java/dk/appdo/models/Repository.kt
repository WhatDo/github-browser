package dk.appdo.models

import io.ktor.http.Url

data class Repository(
    val id: RepoId,
    val name: String,
    val description: String?,
    val user: User,
    val language: String?,
    val forks: Int,
    val issues: Int,
    val stars: Int
) {
    val icon: Url get() = user.icon

    val fullName = "${user.name} / $name"

    data class User(
        val name: String,
        val icon: Url
    )
}

@JvmInline
value class RepoId(val value: Long)

