package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRepository(private val database: ElectionDatabase) {

    val savedElections = database.electionDao.getAllElections()

    suspend fun getElection(id: Int): Election? {
        return database.electionDao.getElectionById(id)
    }

    suspend fun getVoterInfo(electionId: Int, division: Division): VoterInfoResponse {
        val state = division.state

        return if (division.state == "") {
            CivicsApi.retrofitService.getVoterInfo(electionId, "fl")
        } else {
            CivicsApi.retrofitService.getVoterInfo(electionId, state)
        }
    }


    suspend fun followElection(election: Election) {
        withContext(Dispatchers.IO) {
            database.electionDao.followElection(election.id)
        }
    }

    suspend fun unfollowElection(election: Election) {
        withContext(Dispatchers.IO) {
            database.electionDao.unfollow(election)
        }
    }
}