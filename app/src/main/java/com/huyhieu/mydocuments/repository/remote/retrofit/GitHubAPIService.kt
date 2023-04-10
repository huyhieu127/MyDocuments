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