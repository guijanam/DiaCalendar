package com.example.diacalendar.Firebase

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class DiaTableItem(var tableName: String = "",
                        var documentId: String = "") {

    val docRef: DocumentReference
        get() = Firebase.firestore.document(this.documentId)

    var diaWorkDetails : MutableMap<String, DiaWorkDetail> = mutableMapOf()

    constructor(querySnapshot: QueryDocumentSnapshot) : this() {
        querySnapshot.data.forEach { data ->
            if (data.key == "table_name") {
                this.tableName = data.key
            }
            (data.value as Map<String, Any>)?.let {
//                .set(key: K, value: V)
//                (source)
                val value = DiaWorkDetail(map = it)
                diaWorkDetails.set(key = data.key, value = value)
            }
        }
    }
}
