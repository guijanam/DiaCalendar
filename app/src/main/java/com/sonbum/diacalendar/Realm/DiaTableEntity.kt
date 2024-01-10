package com.sonbum.diacalendar.Realm

import com.sonbum.diacalendar.DiaCalendarApp
import com.sonbum.diacalendar.Firebase.DiaWorkDetail
import io.realm.kotlin.ext.realmListOf

import io.realm.kotlin.ext.toRealmList

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject

import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId


class DiaTableEntity : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var diaTableType: DiaTableTypeEntity? = null
    var diaItems: RealmList<DiaItemEntity> = realmListOf()

    companion object {
        fun create(typeName: String,
                                 diaWorkDetails: List<DiaWorkDetail>) : DiaTableEntity {
            val entity = DiaTableEntity().apply {
                this.diaTableType = DiaCalendarApp.instance.repository.fetchDiaTableTypeEntity(typeName)
                val items = diaWorkDetails.map { DiaItemEntity.create(diaWorkDetail = it) }
                this.diaItems = items.toRealmList()
            }
            return entity
        }
    }

}

class DiaTableTypeEntity : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
}