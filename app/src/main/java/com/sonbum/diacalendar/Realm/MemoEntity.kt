package com.sonbum.diacalendar.Realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class MemoEntity : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var title: String = ""
    var content: String = ""
    var time: String = ""
    var hexCode: String = ""
    var dateString: String = ""

//    var timeDate: Date
//    {
//        return Date.makeTimeDate(timeString: time)
//    }

}