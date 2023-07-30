package com.example.happyplaces.adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.happyplaces.Model.PlaceEntity
import com.example.happyplaces.databinding.ItemBinding
import kotlinx.coroutines.flow.Flow

class placeAdapter(private val context: Context, private var placeList: List<PlaceEntity>) :
    RecyclerView.Adapter<placeAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val title=binding.title
        val description=binding.tvDescription
        val date=binding.tvDate
        val location=binding.tvLocation
        val image=binding.placeImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        /*  return ViewHolder(ItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false ))*/
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = placeList[position]

        holder.title.text = currentItem.title
        holder.description.text=currentItem.description
        holder.date.text=currentItem.date
        holder.location.text=currentItem.location
        Glide.with(context).load(currentItem.image).into(holder.image)
    }
    fun setData(placeList: List<PlaceEntity>) {

        this.placeList = placeList
        notifyDataSetChanged()
    }
}