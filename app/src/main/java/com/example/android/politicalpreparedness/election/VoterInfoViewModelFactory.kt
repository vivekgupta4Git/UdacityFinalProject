package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election

class VoterInfoViewModelFactory(
   private val electionId : Int,
    private val division: Division,
   private val dao: ElectionDao
): ViewModelProvider.Factory {

    @SuppressWarnings("unchecked") // This would be helpful for lint warnings for casts.
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(VoterInfoViewModel::class.java))
            return VoterInfoViewModel(electionId,division,dao) as T
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}