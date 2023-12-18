package com.sonbum.diacalendar.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonbum.diacalendar.Firebase.CompanyListItem
import com.sonbum.diacalendar.Managers.FirebaseManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WorkSettingVM : ViewModel() {

    // original
    private var fetchedCompaniesFlow : MutableStateFlow<List<CompanyListItem>> = MutableStateFlow(emptyList())

    // original +
    var fetchedCompanyNamesFlow = fetchedCompaniesFlow.map { it.mapNotNull { it.officeName } }
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = emptyList())

    var selectedCompany : MutableStateFlow<CompanyListItem?> = MutableStateFlow(null)

    var selectedCompanyName : StateFlow<String> = selectedCompany.map { it?.officeName }.mapNotNull { it }
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = "승무소를 선택하세요")


    companion object {
        const val TAG : String = "WorkSettingVM"
    }

    init {
        Log.d(TAG, ": init")
        viewModelScope.launch {
            val fetchedCompanyList : List<CompanyListItem> = FirebaseManager.fetchCompanyListInfo()

            //val fetchedCompanyNames : List<String> = fetchedCompanyList.mapNotNull { it.officeName }

            fetchedCompaniesFlow.emit(fetchedCompanyList)
        }


        viewModelScope.launch {
            selectedCompany
                .mapNotNull { it }
                .collectLatest {
                Log.d(TAG, "selectedCompanyName: $it")
                //val fetchedCompany = FirebaseManager.fetchCertainCompanyInfo(it.diaSelectedList)

            }
        }

    }

    fun companySelected(name: String) {
        val selectedCompanyEntity = this.fetchedCompaniesFlow.value.filter { it.officeName == name }.first()
        viewModelScope.launch {
            selectedCompany.emit(selectedCompanyEntity)

        }

    }

}