package com.sonbum.diacalendar.Firebase

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import java.util.UUID

data class Company(val documentSnapshot: DocumentSnapshot,
                   var ref : DocumentReference){

    var id: UUID = UUID.randomUUID()
    var documentId: String
    var name: String

    var diaSelectedList : List<String> = emptyList()
    var diaTurnList : List<String> = emptyList()
    var diaTables : Map<String, DiaTableItem> = emptyMap()
    init {
        this.ref = ref
        this.documentId = documentSnapshot.id
        this.name = documentSnapshot.data?.get("name") as String
        this.diaSelectedList = documentSnapshot.data?.get("dia_select") as List<String>
        this.diaTurnList = documentSnapshot.data?.get("dia_turn") as List<String>
    }

}
