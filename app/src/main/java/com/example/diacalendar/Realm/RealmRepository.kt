package com.example.diacal_android.Realm

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class RealmRepository {

    lateinit var realm: Realm

    init {
        // Creates a realm with default configuration values
        val config = RealmConfiguration.create(
            // Pass object classes for the realm schema
            schema = setOf(DiaItemEntity::class, DiaTableEntity::class)
        )
        // Open the realm with the configuration object
        val realm = Realm.open(config)
        //Log.v("Successfully opened realm: ${realm.configuration.name}")

    }
}