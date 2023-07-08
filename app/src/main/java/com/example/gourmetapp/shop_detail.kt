package com.example.gourmetapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gourmetapp.databinding.ActivityMainBinding
import com.example.gourmetapp.databinding.ActivityShopDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.net.URL

class shop_detail : AppCompatActivity() {
    private lateinit var binding: ActivityShopDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_detail)

        binding = ActivityShopDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val name = intent.getStringExtra("name")
        val address = intent.getStringExtra("address")
        val open = intent.getStringExtra("open_time")
        val byteArray = intent.getByteArrayExtra("bitmap")

        // Nullableな型に対してnullチェックを行い、非nullアサーション（!!）を使用する
        if (byteArray != null) {
            val element = byteArray[0]

            val big_photo = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            binding.shopImage.setImageBitmap(big_photo)
        }

        binding.shopName.setText(name)
        binding.address.setText(address)
        binding.openTime.setText(open)

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
}