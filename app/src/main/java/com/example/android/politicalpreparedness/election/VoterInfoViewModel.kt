package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.AdministrationBody
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoterInfoViewModel(electionId: Int, division: Division, application: Application) : ViewModel() {
    private val database = ElectionDatabase.getInstance(application)

    private val electionsRepository = ElectionsRepository(database)

    //DONE: Add live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    private val _address = MutableLiveData<String>()
    val voterInfoAddress: LiveData<String>
        get() = _address

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

    private val _electionAdministrationBody = MutableLiveData<AdministrationBody>()
    val electionAdministrationBody: LiveData<AdministrationBody>
        get() = _electionAdministrationBody

    init {
        viewModelScope.launch {
            isElectionFollowed(electionId.toString())
            getVoterInfo(electionId, division)
        }
    }


    //DONE: Add var and methods to populate voter info
    private fun getVoterInfo(electionId: Int, division: Division) {
        viewModelScope.launch {

            try {
                val election = database.electionDao.getElectionById(electionId)
                _isElectionFollowed.value = election != null
                val address = division.state + ", " + division.country

                _voterInfo.value =
                        CivicsApi.retrofitService.getVoterInfo(electionId, address)

                _election.value = _voterInfo.value?.election
                _electionAdministrationBody.value =
                        _voterInfo.value?.state?.first()?.electionAdministrationBody
            } catch (e: Exception) {
                // ignore the error, we assume there's nothing good to show
            }
        }
    }

    // Loading URLs
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
    fun followElection(election: Election) {
        viewModelScope.launch {
            electionsRepository.followElection(election)
            _isElectionFollowed.postValue(true)
        }
    }

    fun unfollowElection(election: Election) {
        viewModelScope.launch {
            electionsRepository.unfollowElection(election)
            _isElectionFollowed.postValue(false)
        }
    }

    fun isElectionFollowed(electionId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val isFollowed = database.electionDao.exists(electionId)
                _isElectionFollowed.postValue(isFollowed)
            }
        }
    }


    //TODO: Add live data to hold voter info

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}