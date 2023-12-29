package com.sonbum.diacalendar.Realm

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.realmSetOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmSet
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId


// Implements the `RealmObject` interface
class UserCompanyEntity : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var diaTurn: RealmList<String> = realmListOf()
    var diaSelect: RealmList<String> = realmListOf()
    var diaTables: RealmList<DiaTableEntity> = realmListOf()

}
