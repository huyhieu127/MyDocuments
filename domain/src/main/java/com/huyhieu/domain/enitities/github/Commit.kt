package com.huyhieu.domain.enitities.github

data class Commit(
    val author: CommitDetail, val committer: CommitDetail, val message: String
)