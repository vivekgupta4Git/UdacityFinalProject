package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.FollowedElection
import kotlinx.serialization.PrimitiveKind

@Dao
interface ElectionDao {

    /**
     * Insert list into Election Table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg election: Election)


    /**
     * insert single election into Election Table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(election: Election)


        /*
     * Get all the saved Election from Database using method [allElectionSaved]
     * No need to put suspend keyword for livedata
         */
    @Query("Select * from election_table")
    fun getAllElectionSaved() : LiveData<List<Election>>





/**
     * Get a single Election Detail from database using method [getElectionById]
     * @param key election to find
     * @return Election or null
     */

    @Query("Select * from election_table WHERE id=:key")
    suspend  fun getElectionById( key:Int) : Election



/**
     * We need an extra query to get only those saved election
     * which are followed in follow_table to do that we create a query
    */

    @Query("Select * FROM election_table where election_table.id IN (Select follow_table.electionId FROM follow_table)")
    fun getFollowedSavedElectionsOnly()  : LiveData<List<Election>>

/**
    * Delete particular election from table
     */

    @Query("Delete FROM election_table Where id=:key")
    suspend fun deleteElection(key : Int)

/*
    * Clear all the data from election Table
     */

    @Query("Delete from ELECTION_TABLE")
    suspend fun clearElectionTable()

/**
     ************* Second Table[FollowedElection]'s queries *************
     *//*


    */
/**
     * Insert values into follow_table using method [insertIntoFollowElection]
     */

   @Query("INSERT INTO follow_table VALUES(:idElection)")
    suspend fun insertIntoFollowElection(idElection: Int)


/**
     * Unfollow particular election using method [deleteFromFollow]
     * @param key election id to be unfollowed
     */

    @Query("DELETE FROM follow_table WHERE electionId= :key")
    suspend fun deleteFromFollow(key: Int)


/**
     * Query to get all followed id follow table
     */

    @Query("SELECT * from FOLLOW_TABLE")
    fun getAllFollowedElection() : LiveData<List<FollowedElection>>


/**
     * Query to check whether given id is present in the db or not using method
     * [isElectionFollowedOrNot]
     * @param key id of the election
     * @return livedata
     *//*

    @Query("select CASE electionId when NULL THEN 0 ELSE 1 END from FOLLOW_TABLE where electionId=:key ORDER BY electionId DESC LIMIT 1")
    fun isElectionFollowed(key: Int)  : LiveData<Int>
*/
    @Query("select * from follow_table WHERE electionId=:key ORDER by electionId DESC LIMIT 1")
    fun isElectionFollowedOrNot(key : Int) : LiveData<FollowedElection>

}
