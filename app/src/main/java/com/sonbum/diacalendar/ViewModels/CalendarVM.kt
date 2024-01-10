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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.concurrent.Flow

class CalendarVM : ViewModel() {

    companion object {
        const val TAG : String = "CalendarVM"
    }

    private val currentUserDateAndTurnListEntityFlow = DiaCalendarApp.instance.repository
        .realm.query<UserDateAndTurnListEntity>().asFlow()

    var currentUserDateAndTurnList : MutableStateFlow<List<UserDateAndTurnListEntity>> = MutableStateFlow(value = emptyList())

    init {
        Log.d(TAG, ": init")

        viewModelScope.launch {
            val asyncCall: Deferred<Unit> = async {
                currentUserDateAndTurnListEntityFlow
                    .map { it.list.toList() }
                    .collect(currentUserDateAndTurnList)

            }
        }
    }

    fun findUserDateAndTurn(date: LocalDate) : UserDateAndTurnListEntity? {
        //val dateString : String = SimpleDateFormat("yyyy-MM-dd").format
        val dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        Log.d(TAG, "findUserDateAndTurn: dateString : $dateString")
        return currentUserDateAndTurnList.value.firstOrNull {
            it._calendarData == dateString
        }
    }


}