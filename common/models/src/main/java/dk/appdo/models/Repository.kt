package dk.appdo.models

import io.ktor.http.Url

data class Repository(
    val id: RepoId,
    val name: String,
    val description: String?,
    val icon: Url
)

@JvmInline
value class RepoId(val value: Long)

