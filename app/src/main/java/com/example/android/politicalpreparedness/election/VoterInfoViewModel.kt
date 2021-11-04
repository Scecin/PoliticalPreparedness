package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoterInfoViewModel(electionId: Int, division: Division, application: Application) : ViewModel() {
    private val database = ElectionDatabase.getInstance(application)

//    private val electionsRepository = ElectionsRepository(database)

    // Live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo


    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    private val _votingLocationsUrl = MutableLiveData<String?>()
    val votingLocationsUrl: LiveData<String?>
        get() = _votingLocationsUrl

    private val _ballotInformationUrl = MutableLiveData<String?>()
    val ballotInformationUrl: LiveData<String?>
        get() = _ballotInformationUrl

    private val _isElectionFollowed = MutableLiveData<Boolean?>()
    val isElectionFollowed: LiveData<Boolean?>
        get() = _isElectionFollowed

    init {
        viewModelScope.launch {
            getVoterInfo(electionId, division)
        }
    }

    // Var and methods to populate voter info
    private fun getVoterInfo(electionId: Int, division: Division) {
        viewModelScope.launch {
            try {
                val election = database.electionDao.getElectionById(electionId)
                _isElectionFollowed.value = election != null
                val address = division.state + ", " + division.country

                _voterInfo.value =
                        CivicsApi.retrofitService.getVoterInfo(electionId, address)

                _election.value = _voterInfo.value?.election

            } catch (e: Exception) {
            }
        }
    }

    // var and methods to support loading URLs
    fun onVotingLocationClick() {
        _votingLocationsUrl.value =
                _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl
    }

    fun onBallotInformationClick() {
        _ballotInformationUrl.value =
                _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl
    }

    fun onVotingLocationNavigated() {
        _votingLocationsUrl.value = null
    }

    fun onBallotInformationNavigated() {
        _ballotInformationUrl.value = null
    }

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */
    // Methods to save and remove elections to local database
    fun followElection() {
        viewModelScope.launch {
            election.value?.let { database.electionDao.insertElection(it) }
        }
        _isElectionFollowed.value = true
    }

    fun unFollowElection() {
        viewModelScope.launch(Dispatchers.IO) {
            election.value?.let { database.electionDao.delete(it) }
        }
        _isElectionFollowed.value = false
    }
}