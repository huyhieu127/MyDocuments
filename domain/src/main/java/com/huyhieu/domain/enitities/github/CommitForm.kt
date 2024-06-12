package com.huyhieu.domain.enitities.github

data class CommitForm(
    val author: UserGitHubForm,
    val commit: Commit,
    val committer: UserGitHubForm
)