package com.example.diacalendar.Realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class UserDateAndTurnListEntity : RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var turn: String = ""// 교번
    var calendarData: String = ""// 달력날짜
}

