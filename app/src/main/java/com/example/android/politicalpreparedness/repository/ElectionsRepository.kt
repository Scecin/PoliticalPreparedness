package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRepository(private val database: ElectionDatabase) {

//    val savedElections = database.electionDao.getAllElections()

//    suspend fun followElection(election: Election) {
//        withContext(Dispatchers.IO) {
//            database.electionDao.insertElection(election)
//        }
//    }

//    suspend fun unFollowElection(election: Election) {
//        withContext(Dispatchers.IO) {
//            database.electionDao.delete(election)
//        }
//    }
}