package com.example.android.politicalpreparedness.network.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "follow_table")
data class FollowedElection (
            @PrimaryKey
            val electionId : Int
)