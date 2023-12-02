package com.example.diacal_android.Realm

import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class UserDateAndTurnListEntity {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var turn: String = ""// 교번
    var calendarData: String = ""// 달력날짜
}

