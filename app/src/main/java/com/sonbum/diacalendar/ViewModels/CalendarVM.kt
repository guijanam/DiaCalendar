package com.sonbum.diacalendar.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel

class CalendarVM : ViewModel() {



    companion object {
        const val TAG : String = "CalendarVM"
    }

    init {
        Log.d(TAG, ": init")
    }

}