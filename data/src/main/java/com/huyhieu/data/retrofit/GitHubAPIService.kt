package com.huyhieu.data.retrofit

import com.huyhieu.data.BuildConfig
import com.huyhieu.domain.enitities.github.CommitForm
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