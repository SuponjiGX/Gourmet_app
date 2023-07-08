package com.example.gourmetapp

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.gourmetapp.databinding.ActivityShopListBinding
import androidx.lifecycle.lifecycleScope
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ShopList : AppCompatActivity() {
    private lateinit var binding: ActivityShopListBinding
    val grov_item = GrovalItem.getInstance()
    val radius = grov_item.rad
    val latitude = grov_item.Lati
    val longtitude = grov_item.Loc
    val apiKey = "d81118db21e5cc0c"
    val mainUrl = "http://webservice.recruit.co.jp/hotpepper/gourmet/v1/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_list)

        binding = ActivityShopListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val gourmetUrl = "$mainUrl?key=$apiKey&lat=$latitude&lng=$longtitude&range$radius&format=json"

        gourmetTask(gourmetUrl)

//        binding.textView.setText(radius.toString())
        binding.textView3.setText(latitude.toString())
        binding.textView4.setText(longtitude.toString())
    }

    private fun gourmetTask(gourmetUrl: String){
        lifecycleScope.launch{
            val result = gourmetBackgroundTask(gourmetUrl)
//            gourmetBackgroundTask(gourmetUrl)
            println("url ok")
            println("result: $result")
            gourmetJsonTask(result)
        }
    }


    private suspend fun gourmetBackgroundTask(gourmetUrl: String): String = suspendCoroutine { continuation ->
        /// GETリクエスト送信！
        Fuel.get(gourmetUrl).responseJson { request, response, result ->
            when (result) {
                is com.github.kittinunf.result.Result.Success -> {
                    /// レスポンス正常取得
                    val data = result.get().obj().toString()
                    Log.d(
                        TAG, "Responsed JSON : $data"
                    )
                    continuation.resume(data)
                }
                is Result.Failure -> {
                    /// リクエスト失敗・エラー
                    val ex = result.getException()
                    Log.d(TAG, "Failure : " + ex.toString())
                    continuation.resumeWithException(ex)
                }
            }
        }
    }


    private fun gourmetJsonTask(result: String){
        val jsonObj = JSONObject(result)
        val gourmetJSONArray =jsonObj.getJSONObject("results").getJSONArray("shop")
        val gourmetJSON = gourmetJSONArray.getJSONObject(1)
        val name = gourmetJSON.getString("name")

        binding.textView.setText(name)
    }


    fun onButtonClick(view: View){
        finish()
    }
}