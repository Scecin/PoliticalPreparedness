package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch
import java.util.ArrayList


class ElectionsViewModel(application: Application): AndroidViewModel(application) {

    val apiService: CivicsApiService = CivicsApi.retrofitService

    // Construct ViewModel and provide election datasource
    private val database = ElectionDatabase.getInstance(application)
    private val electionsRepository = ElectionsRepository(database)

    private val _navigateToElection = MutableLiveData<Election?>()
    val navigateToElection : LiveData<Election?>
        get() = _navigateToElection


    // Live data val for upcoming elections
    val upcomingElections = MutableLiveData<List<Election>>()
    // Live data val for saved elections
    val savedElections = electionsRepository.savedElections



    init {
            getElections()
    }

    private fun getElections() {
        viewModelScope.launch {
            try {
                val electionList = apiService.getElections()
                upcomingElections.value = electionList.elections
            } catch (e: Exception) {
                upcomingElections.value = ArrayList()
            }
        }
    }

    // Functions to navigate to saved or upcoming election voter info
    fun onElectionClicked(election: Election) {
        _navigateToElection.value = election
    }

    fun onElectionNavigated() {
        _navigateToElection.value = null
    }
}