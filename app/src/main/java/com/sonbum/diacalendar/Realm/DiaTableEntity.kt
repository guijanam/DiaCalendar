package com.sonbum.diacalendar.Realm

import io.realm.kotlin.ext.realmSetOf
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmSet
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId


class DiaTableEntity : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var diaTableType: DiaTableTypeEntity? = null
    var diaItems: RealmSet<DiaItemEntity> = realmSetOf()


//    constructor(typeName: String, diaWorkDetails: List<DiaWorkDetail>) {
//        this.diaTableType = DiaCalendarApp.instance.repository.fetchDiaTableTypeEntity(typeName)
//        val items = diaWorkDetails.map { DiaItemEntity(diaWorkDetail = it) }
//        this.diaItems = items.toRealmSet()
//    }

}

class DiaTableTypeEntity : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
}