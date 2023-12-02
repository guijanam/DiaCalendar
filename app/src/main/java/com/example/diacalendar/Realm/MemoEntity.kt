package com.example.diacal_android.Realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class MemoEntity : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var title: String = "제목입니다"
    var content: String = "내용입니다"
    var time: String = "오전 10:00"
    var hexCode: String = "#5AC4BD"
    var dateString: String = ""

//    var timeDate: Date
//    {
//        return Date.makeTimeDate(timeString: time)
//    }
}