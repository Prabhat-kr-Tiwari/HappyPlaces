package com.example.happyplaces.New.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyplaces.DAO.PlaceDao
import com.example.happyplaces.Model.PlaceEntity
import com.example.happyplaces.R
import com.example.happyplaces.adpater.placeAdapter
import com.example.happyplaces.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: placeAdapter
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        val view = binding.root
//        setContentView(view)

//        lifecycleScope.launch {
//            showAdapterData()
//
//        }
        /*val placeDao = (application as PlaceApplicationClass).db.placedao()
        lifecycleScope.launch {

            placeDao.getAll().collect {

                val list = ArrayList(it)
                setUpListOfDataInRecyclerView(list, placeDao)
            }
        }*/

        binding.fabAddHappyPlace.setOnClickListener {
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivity(intent)
        }
    }
/*
    private suspend fun showAdapterData() {
        val placeDao = (application as PlaceApplicationClass).db.placedao()
        val list = placeDao.getAll()

        adapter = placeAdapter(this@MainActivity, list)
        binding.rvList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            this.adapter = adapter
        }
    }*/
    private fun setUpListOfDataInRecyclerView(
        placeList: ArrayList<PlaceEntity>, placeDao: PlaceDao
    ) {


        if (placeList.isNotEmpty()) {


            val placeAdapter=placeAdapter(this,placeList)
           /* binding.rv_list?.layoutManager = LinearLayoutManager(this)
            binding?.rvItemsList?.adapter = itemAdapter*/
            binding.rvList.layoutManager=LinearLayoutManager(this)
            binding.rvList.adapter=placeAdapter



        } else {

            Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show()


        }


    }
}