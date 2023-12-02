package com.example.diacalendar.Realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class DiaItemEntity : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var workTime : String = ""
    var firstTime : String = ""
    var secondTime : String = ""
    var thirdTime : String = ""
    var totalTime : String = ""
    var numter1 : String = ""
    var numter2 : String = ""
}