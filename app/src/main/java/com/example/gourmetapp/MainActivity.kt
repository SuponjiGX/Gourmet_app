package com.example.gourmetapp

//import android.location.LocationCallback

import android.Manifest
import android.accessibilityservice.GestureDescription
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.gourmetapp.databinding.ActivityMainBinding
import android.app.Application


class MainActivity : AppCompatActivity(), LocationListener, AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var locationManager: LocationManager
    val grov_item = GrovalItem.getInstance()

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



        //gps
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            startLocationUpdates()
        }

        val spinner: Spinner = findViewById(R.id.spinner)
        spinner.onItemSelectedListener = this
    }

    private fun startLocationUpdates() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 1F, this)
//        println("yaaa")
    }



    override fun onLocationChanged(location: Location) {
//        println("yaaa")
        // 緯度の表示
        val lati = location.getLatitude()
        grov_item.Lati = lati
//        binding.textView1.setText(lati.toString())

        // 経度の表示
        val loc = location.getLongitude()
        grov_item.Loc = loc
//        binding.textView2.setText(loc.toString())

        println("緯度: $lati")
        println("経度: $loc")
    }


    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(this)
    }

    // 検索ボタン //
    fun onButtonClick(view: View){
        val intent = Intent(this@MainActivity, ShopList::class.java)
        startActivity(intent)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
//         parent.getItemAtPosition(pos)
//        pos.inc()
        grov_item.rad = pos.inc()
        println(pos.inc().toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}
