package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    // Insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElection(election: Election)

    // Select all election query
    @Query("select * from election_table ORDER BY electionDay")
    fun getAllElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id = :id ")
    suspend fun getElectionById(id:Int): Election?


    @Query("insert into election_table (id) values(:electionId)")
    suspend fun followElection(electionId: Int)

    // Select a single election query
    @Query("SELECT EXISTS (SELECT 1 FROM election_table WHERE id = :id)")
    fun exists(id: String): Boolean

    // Delete query
    @Delete
    fun unfollow(election: Election)
}