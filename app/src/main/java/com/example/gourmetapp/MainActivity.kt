package com.example.gourmetapp

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.example.gourmetapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //検索画面表示

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.support.movementMethod = LinkMovementMethod.getInstance() //webブラウザで開く場合はいる
        val url = "http://webservice.recruit.co.jp/"
        val link: CharSequence = Html.fromHtml("Powered by <a href=\"$url\">ホットペッパー Webサービス</a>")
        binding.support.setText(link) //リンクを押すとブラウザに飛ぶ

    }
}