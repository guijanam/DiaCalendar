package com.sonbum.diacalendar.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonbum.diacalendar.Firebase.Company
import com.sonbum.diacalendar.Firebase.CompanyListItem
import com.sonbum.diacalendar.Managers.FirebaseManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WorkSettingVM : ViewModel() {

    // original 승무소선택
    private var fetchedCompaniesFlow : MutableStateFlow<List<CompanyListItem>> = MutableStateFlow(emptyList())

    // original 승무소선택에 따른 근무선택리스트
    private var fetcheddiaSelectListFlow : MutableStateFlow<List<String>> = MutableStateFlow(
        emptyList()
    )

    var fetcheddiaSelectListStateFlow = fetcheddiaSelectListFlow
        .stateIn(scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList())

    // original +
    var fetchedCompanyNamesFlow = fetchedCompaniesFlow
        .map { it.mapNotNull { it.officeName } }
        .stateIn(scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList())


    var selectedCompany : MutableStateFlow<CompanyListItem?> = MutableStateFlow(null)

    var selectedDia : MutableStateFlow<String> = MutableStateFlow("다이아를 선택하세요")

    var selectedCompanyName : StateFlow<String> = selectedCompany.map { it?.officeName }.mapNotNull { it }
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = "소속을 선택하세요")

    // 근무선택리스트



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
                .flatMapLatest {
                    // suspend
                    flow<Company> {
                        it.ref?.let {doc ->
                            emit(FirebaseManager.fetchCertainCompanyInfo(doc))
                        }
                    }
                }
                .collectLatest {
                    Log.d(TAG, "fetchedCompany: ${it.toString()}")
                    this@WorkSettingVM.fetcheddiaSelectListFlow.emit(it.diaSelectedList)
                    Log.d(TAG, "selectedCompanyName: $it")
            }
        }

        viewModelScope.launch {
            this@WorkSettingVM.fetcheddiaSelectListFlow.collectLatest {
                Log.d(TAG, "fetcheddiaSelectListFlow it: $it")

            }
        }

    }

    fun companySelected(name: String) {
        val selectedCompanyEntity = this.fetchedCompaniesFlow.value.filter { it.officeName == name }.first()
        viewModelScope.launch {
            selectedCompany.emit(selectedCompanyEntity)

        }
    }

    fun diaSelected(name: String) {
        val selectedDiaEntity = this.fetcheddiaSelectListStateFlow.value.filter { it == name }.first()
        viewModelScope.launch {
            selectedDia.emit(selectedDiaEntity)

        }
    }

}