package com.example.automaticupdate.update

import com.example.automaticupdate.GitHubFile
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {
    @GET("repos/{owner}/{repo}/contents/{path}")
    fun getRepoFile(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String
    ): Call<GitHubFile>
}
