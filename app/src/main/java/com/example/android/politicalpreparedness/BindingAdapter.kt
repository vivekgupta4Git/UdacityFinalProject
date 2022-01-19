package com.example.android.politicalpreparedness

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.election.Status
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election
import retrofit2.http.Url

@BindingAdapter("civicStatus")
fun bindStatus(statusImageView: ImageView, status: Status?) {
    when (status) {
        Status.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_img)
        }
        Status.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        Status.DONE -> {
            statusImageView.visibility = View.GONE
        }
        else -> {}
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView,data : List<Election>?){
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}

@BindingAdapter("followedListData")
fun bindFollowedRecyclerView(recyclerView: RecyclerView,data: List<Election>?){
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}