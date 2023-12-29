package com.sonbum.diacalendar.Realm

import com.sonbum.diacalendar.shared.makeDate
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId
import java.util.Date

class UserWorkChangeEntity : RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var turn: String = ""// 교번
    var _calendarData: String = ""// 달력날짜

    fun getCalendarDate() : Date = _calendarData.makeDate()

    companion object {
        fun create(date: String,
                   turn: String) : UserWorkChangeEntity = UserWorkChangeEntity().apply {
            this._calendarData = date
            this.turn = turn
        }
    }
}