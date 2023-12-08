package com.sonbum.diacalendar

import android.app.Application
import com.sonbum.diacalendar.Realm.RealmRepository

class DiaCalendarApp : Application() {

    lateinit var repository: RealmRepository

    companion object {
        lateinit var instance: DiaCalendarApp
            private set
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        this.repository = RealmRepository()
    }
}

