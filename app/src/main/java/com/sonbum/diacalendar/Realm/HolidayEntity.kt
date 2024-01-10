package com.sonbum.diacalendar.Realm


import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId


class HolidayEntity : RealmObject {
    @PrimaryKey
    var _id: ObjectId = BsonObjectId()
    var holidayName: String = ""
    var _calendarData: String = ""
    //var diaTurn: RealmList<String> = realmListOf()

//    _calendarData: "2023/01/01"
//    holidayName: "신정"

}
