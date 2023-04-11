package com.huyhieu.mydocuments.repository.remote.retrofit

import com.huyhieu.mydocuments.BuildConfig
import com.huyhieu.mydocuments.models.github.CommitForm
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface GitHubAPIService {

    companion object {
        val url: String
            get() = BuildConfig.GITHUB_API_URL
    }

    @Headers(
        "Accept: application/vnd.github+json",
        "Authorization: Bearer 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",
        "X-GitHub-Api-Version: 2022-11-28"
    )
    @GET("repos/huyhieu127/MyDocuments/commits")
    suspend fun getCommit(): Response<List<CommitForm>>
}