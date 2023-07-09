package com.example.gourmetapp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.net.Uri.parse
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.SimpleAdapter
import com.example.gourmetapp.databinding.ActivityShopListBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpCookie.parse
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files.list
import java.util.Collections.list
import java.util.logging.Level.parse
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

        val gourmetUrl = "$mainUrl?key=$apiKey&lat=$latitude&lng=$longtitude&range=$radius&count=20&format=json"

        gourmetTask(gourmetUrl)



//        binding.textView.setText(radius.toString())
//        binding.textView3.setText(latitude.toString())
//        binding.textView4.setText(longtitude.toString())
    }

    private fun gourmetTask(gourmetUrl: String){
        lifecycleScope.launch{
            val result = gourmetBackgroundTask(gourmetUrl)
//            gourmetBackgroundTask(gourmetUrl)
//            println("url ok")
//            println("result: $result")
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

    private suspend fun gourmetJsonTask(result: String){
        lateinit var recyclerView: RecyclerView
        lateinit var listAdapter: ListAdapter
        val itemClickListener = MyItemClickListener(this)
        val itemList: MutableList<ListItem> = mutableListOf()

        val jsonObj = JSONObject(result)
        //お店のリストが格納
        val gourmetJSONArray =jsonObj.getJSONObject("results").getJSONArray("shop")

//        recyclerView = findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        listAdapter = ListAdapter(itemList)
//        recyclerView.adapter = listAdapter

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        for (i in 0 until gourmetJSONArray.length()){
            val gourmetJSON = gourmetJSONArray.getJSONObject(i)
            val name = gourmetJSON.getString("name")
            val access = gourmetJSON.getString("mobile_access")
            val address = gourmetJSON.getString("address")
            val open_time = gourmetJSON.getString("open")

            val samnaile = gourmetJSON.getJSONObject("photo").getJSONObject("mobile").getString("s")
            //URLからBitmapにする
            val sambit = CoroutineScope(Dispatchers.Main).async {
                urlToBitmap(samnaile)
            }.await()

            val photo = gourmetJSON.getJSONObject("photo").getJSONObject("mobile").getString("l")
            val big_photo = CoroutineScope(Dispatchers.Main).async {
                urlToBitmap(photo)
            }.await()

            //表示に必要な情報のリストを作る
            val listItem = ListItem(sambit, name, access, open_time, address, big_photo)
            itemList.add(listItem)


//            listAdapter.notifyDataSetChanged()
//            println(name)
        }
        println("itemlist: $itemList")
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager(this).getOrientation())
        recyclerView.addItemDecoration(dividerItemDecoration)

        listAdapter = ListAdapter(this, itemList, itemClickListener)
//        listAdapter.notifyDataSetChanged()
        recyclerView.adapter = listAdapter
//        println(listAdapter.itemCount)
    }

    suspend fun urlToBitmap(urlString: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val url = URL(urlString)
            val connection = url.openConnection()
            val inputStream = connection.getInputStream()
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }



//    fun onButtonClick(view: View){
//        finish()
//    }
}