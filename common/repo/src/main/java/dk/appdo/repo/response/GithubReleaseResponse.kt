package dk.appdo.repo.response

import kotlinx.serialization.Serializable

@Serializable
internal class GithubReleaseResponse(
    val name: String
)