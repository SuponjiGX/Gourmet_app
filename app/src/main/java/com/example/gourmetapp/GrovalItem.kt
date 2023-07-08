package com.example.gourmetapp

import android.app.Application

class GrovalItem: Application() {
    var rad: Int? = 1
    var Lati: Double? = 0.0
    var Loc: Double? = 0.0

    companion object {
        private var instance : GrovalItem? = null

        fun  getInstance(): GrovalItem {
            if (instance == null)
                instance = GrovalItem()

            return instance!!
        }
    }
}