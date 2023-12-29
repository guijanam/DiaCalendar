package com.sonbum.diacalendar.Realm

import com.sonbum.diacalendar.Firebase.DiaWorkDetail
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class DiaItemEntity : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var diaId : String = ""
    var workTime : String = ""
    var firstTime : String = ""
    var secondTime : String = ""
    var thirdTime : String = ""//야간시간 수당
    var totalTime : String = ""//총근무
    var numtr1 : String = ""//전반 열번
    var numtr2 : String = ""//후반 열번

    companion object {
        fun create(diaWorkDetail: DiaWorkDetail) : DiaItemEntity =
            DiaItemEntity().apply {
            this.diaId = diaWorkDetail.turn
            this.workTime = diaWorkDetail.workTime
            this.firstTime = diaWorkDetail.firstTime
            this.secondTime = diaWorkDetail.secondTime
            this.thirdTime = diaWorkDetail.thirdTime
            this.totalTime = diaWorkDetail.totalTime
            this.numtr1 = diaWorkDetail.numtr1
            this.numtr2 = diaWorkDetail.numtr2
        }
    }

}