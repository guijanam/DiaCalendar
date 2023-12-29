package com.sonbum.diacalendar.Firebase

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class DiaTableItem(
    var documentId : String = "",
    val querySnapshot: QueryDocumentSnapshot
)
{

//    val docRef: DocumentReference
//        get() = Firebase.firestore.document(this.documentId)

    var tableName : String = ""
    var diaWorkDetails : MutableMap<String, DiaWorkDetail> = mutableMapOf()

    init {
        querySnapshot.data.forEach { data ->
            if (data.key == "table_name") {
                this.tableName = data.key
            }
            (data.value as? Map<String, Any>?)?.let {
                val value = DiaWorkDetail(map = it)
                diaWorkDetails.set(key = data.key, value = value)
            }
        }
    }
}
