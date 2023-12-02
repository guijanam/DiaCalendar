package com.example.diacal_android.Firebase

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class DiaTableItem(var tableName: String = "",
                        var documentId: String = "") {

}
