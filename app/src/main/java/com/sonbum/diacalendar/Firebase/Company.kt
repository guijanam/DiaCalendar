package com.sonbum.diacalendar.Firebase

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

data class Company(
    val documentSnapshot: DocumentSnapshot,
    var ref : DocumentReference,
)
{

    var id: UUID = UUID.randomUUID()
    var documentId: String
    var name: String

    var diaSelectedList : List<String> = emptyList()
    var diaTurnList : List<String> = emptyList()
    var diaTables : Map<String, DiaTableItem> = emptyMap()
    init {
        this.ref = ref
        this.documentId = documentSnapshot.id
        this.name = documentSnapshot.data?.get("name") as String? ?: ""
        this.diaSelectedList = documentSnapshot.data?.get("dia_select") as List<String>? ?: emptyList()
        this.diaTurnList = documentSnapshot.data?.get("dia_turn") as List<String>? ?: emptyList()
    }

    companion object {
        const val TAG = "Company"
    }



    suspend fun fetchDiaTables() : Map<String, DiaTableItem> {

        var fetchedDiaTables = emptyMap<String, DiaTableItem>()

        return suspendCoroutine { continuation ->
            this.ref
                .collection("dia_tables")
                .get()
                .addOnSuccessListener { result ->
                    var resultMap = mutableMapOf<String, DiaTableItem>()
                    for (document in result) {
                        Log.d(TAG, "${document.id} => ${document.data}")

                        val tableName = document.data.get("table_name") as String? ?: ""
                        val diaTableItem = DiaTableItem(documentId = document.id, querySnapshot = document)
                        resultMap[tableName] = diaTableItem
                    }
                    continuation.resume(resultMap)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                    continuation.resume(fetchedDiaTables)
                }
        }


    }

//    suspend fun fetchCertainCompanyInfo(documentRef: DocumentReference) : Company {
//        return suspendCoroutine {
//
//            documentRef
//                .get()
//                .addOnSuccessListener { result ->
////                for (document in result) {
////                    Log.d(TAG, "${document.id} => ${document.data}")
////                }
//
//                    val test  = result.data
//                    Log.d(TAG, "fetchCertainCompanyInfo: $test")
//                    val fetchedCompany = Company(documentSnapshot = result, ref = documentRef)
////                    completion.invoke(fetchedCompany)
//                    it.resume(fetchedCompany)
//                }
//                .addOnFailureListener { exception ->
//                    Log.w(TAG, "Error getting documents.", exception)
//                }
//        }
//    }

//    self.ref
//        .collection("dia_tables")
//        .getDocuments() { [weak self] (querySnapshot, err) in
//                guard self != nil else {
//            completion(nil)
//            return
//        }
//
//            if let err = err {
//                print("Error getting documents: \(err)")
//                completion(nil)
//                return
//            } else {
//
//                for document in querySnapshot!.documents {
//                    //                        print("⭐️⭐️⭐️\(document.documentID) => \(document.data())")
//                    if let tableName = document.data()["table_name"] as? String {
//                        fetchedDiaTables[tableName] = DiaTableItem(querySnapShot: document)
//                    }
//                }
//            }
//
//            completion(fetchedDiaTables)
//        }
//}

}
