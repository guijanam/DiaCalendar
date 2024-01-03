package com.sonbum.diacalendar.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonbum.diacalendar.DiaCalendarApp
import com.sonbum.diacalendar.Realm.UserDateAndTurnListEntity
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.Flow

class CalendarVM : ViewModel() {

    companion object {
        const val TAG : String = "CalendarVM"
    }

    private val currentUserDateAndTurnListEntityFlow = DiaCalendarApp.instance.repository
        .realm.query<UserDateAndTurnListEntity>().asFlow()

//    var currentUserDateAndTurnList : MutableStateFlow<List<UserDateAndTurnListEntity>> = MutableStateFlow(value = emptyList())

//    val wordFlow = MutableStateFlow("Hi")
//    val pointFlow = MutableStateFlow(5)
//
//    val stateString = wordFlow.combine(pointFlow) { word, points ->
//        "$word is worth $points points"
//    }.stateIn(viewModelScope, SharingStarted.Eagerly, "Default is worth 0 points")

    init {
        Log.d(TAG, ": init")

        viewModelScope.launch {
            val asyncCall: Deferred<Unit> = async {
                currentUserDateAndTurnListEntityFlow
//                    .map { it.list.toList() }
//                    .collect(currentUserDateAndTurnList)

                    .collect { results ->
                        when (results) {
                            // print out initial results
                            is InitialResults<UserDateAndTurnListEntity> -> {
                                for (anUserDateAndTurnList in results.list) {
                                    Log.d(TAG, "initial: anUserDateAndTurnList: $anUserDateAndTurnList")
                                }
                            } else -> {
                            // do nothing on changes
                                for (anUserDateAndTurnList in results.list) {
                                    Log.d(TAG, "changed: anUserDateAndTurnList: $anUserDateAndTurnList")
                                }
                            }
                        }
                }
            }
        }
    }

}