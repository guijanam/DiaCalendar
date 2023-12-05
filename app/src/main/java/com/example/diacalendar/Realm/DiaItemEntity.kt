package com.example.diacalendar.Realm

import com.example.diacalendar.Firebase.DiaWorkDetail
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class DiaItemEntity : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var diaId : String = "1"
    var workTime : String = ""
    var firstTime : String = ""
    var secondTime : String = ""
    var thirdTime : String = ""
    var totalTime : String = ""
    var numtr1 : String = ""
    var numtr2 : String = ""

    constructor(diaWorkDetail : DiaWorkDetail) {
        this.diaId = diaWorkDetail.turn
        this.workTime = diaWorkDetail.workTime
        this.firstTime = diaWorkDetail.firstTime
        this.secondTime = diaWorkDetail.secondTime
        this.thirdTime = diaWorkDetail.thirdTime
        this.totalTime = diaWorkDetail.totalTime
        this.numtr1 = diaWorkDetail.numtr1
        this.numtr2 = diaWorkDetail.numtr2

    }
    fun toDiaWorkDetail(): DiaWorkDetail {
        return DiaWorkDetail(entity = this)
    }
}