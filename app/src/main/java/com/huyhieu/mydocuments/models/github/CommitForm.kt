package com.huyhieu.mydocuments.models.github

data class CommitForm(
    val author: UserGitHubForm,
    val commit: Commit,
    val committer: UserGitHubForm
)