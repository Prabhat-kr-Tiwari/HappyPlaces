package com.example.happyplaces.ApplicationClass

import android.app.Application
import com.example.happyplaces.Database.PlaceDatabase

class PlaceApplicationClass:Application() {

    val db by lazy { PlaceDatabase.getInstance(this) }
}