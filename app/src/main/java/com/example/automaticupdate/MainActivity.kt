package com.example.automaticupdate

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.automaticupdate.update.GitHubApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import android.view.View
import android.widget.Button
import com.google.gson.Gson
import java.io.IOException
import java.net.URL
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener(View.OnClickListener {
            matchVersion(this)
        })
    }


    fun matchVersion(context: Context): String? {
        //1.获取本地版本
        val updateInfo = readJsonFromAssets(this, "version_info.json")
        val gson = Gson()
        var versionInfoLocal = gson.fromJson(updateInfo, VersionInfo::class.java)


        //2.获取最新版本
        // 你的 GitHub 仓库信息
        val owner = "luoyufeng1"
        val repo = "automatic_update_test"
        val filePath = "update_info.json"
//        val filePath = "app/src/main/assets/version_info.json"


        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create()) // 如果需要将响应转换为 JSON，使用 GsonConverterFactory
            .build()

        val gitHubService = retrofit.create(GitHubApi::class.java)
        gitHubService.getRepoFile(owner, repo, filePath).enqueue(object : Callback<GitHubFile> {
            override fun onResponse(call: Call<GitHubFile>, response: Response<GitHubFile>) {
                if (response.isSuccessful) {
                    val gitHubFile = response.body()
                    val downloadUrl = gitHubFile?.downloadUrl
                    Log.e(TAG, "onResponse:  " + downloadUrl)
                    // 使用下载链接来获取文件内容
                    // 这里您可以使用下载链接来下载文件，或者使用其他网络请求来获取文件内容
                    var t = URL(downloadUrl).readText()
                    Log.e(TAG, "onResponse:text " + t)

                } else {
                    Log.e(TAG, "onResponse: 失败")

                }
            }

            override fun onFailure(call: Call<GitHubFile>, t: Throwable) {


            }
        })

        return ""
    }


    fun readJsonFromAssets(context: Context, fileName: String): String? {
        val json: String
        try {
            // 从 assets 文件夹中打开文件
            val inputStream = context.assets.open(fileName)

            // 将文件内容读取为字符串
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            // 将字节数组转换为字符串
            json = String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return json
    }


}