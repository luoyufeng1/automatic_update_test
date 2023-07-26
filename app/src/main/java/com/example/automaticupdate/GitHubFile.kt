package com.example.automaticupdate

import com.google.gson.annotations.SerializedName

data class GitHubFile(
    @SerializedName("name") val name: String,
    @SerializedName("download_url") val downloadUrl: String
)
