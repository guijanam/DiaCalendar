package com.example.diacal_android.Realm

import io.realm.kotlin.ext.realmSetOf
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmSet
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId


class DiaTableEntity : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var diaTableType: String = ""
    var diaItems: RealmSet<DiaItemEntity> = realmSetOf()
}

class DiaTableTypeEntity : RealmObject {
    var _id: ObjectId = ObjectId()
    var name : String = ""

}