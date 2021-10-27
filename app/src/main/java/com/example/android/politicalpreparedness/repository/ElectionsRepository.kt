package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.database.ElectionDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRepository(private val database: ElectionDatabase) {

    val savedElections = database.electionDao.getAllElections()


    suspend fun followElection(electionId: Int) {
        withContext(Dispatchers.IO) {
            database.electionDao.followElection(electionId)
        }
    }

    suspend fun unfollowElection(electionId: String) {
        withContext(Dispatchers.IO) {
            database.electionDao.unfollow(electionId)
        }
    }
}