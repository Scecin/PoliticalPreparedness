package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    // Insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElection(election: Election)

    //DONE: Add select all election query
    @Query("select * from election_table ORDER BY electionDay")
    fun getAllElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table ORDER BY electionDay")
    suspend fun getElections(): List<Election>

    //TODO: Add select single election query


    @Query("insert into election_table (id) values(:electionId)")
    suspend fun followElection(electionId: Int)

    // Delete query
    @Query("DELETE from election_table WHERE id = :id")
    fun unfollow(id: String)

    //TODO: Add clear query

}