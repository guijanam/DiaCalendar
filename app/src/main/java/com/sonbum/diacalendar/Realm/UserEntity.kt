package com.sonbum.diacalendar.Realm

import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class UserEntity {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var todayDiaNumber: String = ""

}