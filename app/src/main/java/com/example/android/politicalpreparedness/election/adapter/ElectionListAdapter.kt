package com.example.android.politicalpreparedness.election.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.databinding.ListitemBinding
//import com.example.android.politicalpreparedness.databinding.ViewholderElectionBinding
import com.example.android.politicalpreparedness.network.models.Election

/*
This class implements [RecyclerView][ListAdapter]
 */
class ElectionListAdapter(private val clickListener: ElectionListener):
    ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback) {

    /**
     * The ElectionViewHolder constructor takes the binding variable from the associated
     * ListViewItem, which nicely gives it access to the full [MarsProperty] information.
     */
    class ElectionViewHolder(private var binding: ListitemBinding )
        : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(election: Election){
            binding.election = election
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }

    }


/*
Allows the recycler view to determine which items have changed when the [List] of [Elections]
has been updated
 */
    companion  object ElectionDiffCallback : DiffUtil.ItemCallback<Election>(){
        override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem===newItem
        }

        override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * Create new [RecyclerView] item views
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ElectionViewHolder {

        return ElectionViewHolder(ListitemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    //TODO: Bind ViewHolder
    /**
     * Replaces the contents of a view
     */
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {

        val election = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(election)
        }
        holder.bind(election)
    }

    class ElectionListener (val electionClickListener: (election:Election) -> Unit) {
        fun onClick(election: Election) = electionClickListener(election)
    }

    //TODO: Add companion object to inflate ViewHolder (from)
}


//TODO: Create ElectionViewHolder

//TODO: Create ElectionDiffCallback

//TODO: Create ElectionListener