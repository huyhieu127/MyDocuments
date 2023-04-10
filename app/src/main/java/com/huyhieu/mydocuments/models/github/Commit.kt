package com.huyhieu.mydocuments.models.github

data class Commit(
    val author: CommitDetail, val committer: CommitDetail, val message: String
)