package com.example.gourmetapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.ContextCompat.startActivity
import java.io.ByteArrayOutputStream

class MyItemClickListener(private val context: Context) : ListAdapter.OnItemClickListener {
    override fun onItemClick(item: ListItem) {
        // リストがタップされたときの処理を実装する
        // item を使用して必要な操作を行う

        val intent = Intent(context, shop_detail::class.java)
        intent.putExtra("name", item.name)
        intent.putExtra("address", item.address)
        intent.putExtra("open_time", item.open)

        val bitmap = item.big_photo

        val width = bitmap?.width
        val height = bitmap?.height
        // Bitmapをバイト配列に変換
        val stream = ByteArrayOutputStream()
        if (bitmap != null) {
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width!!, height!!, false)
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        }
        val byteArray = stream.toByteArray()

// Intentにバイト配列を追加
        intent.putExtra("bitmap", byteArray)


        context.startActivity(intent)
    }
}
