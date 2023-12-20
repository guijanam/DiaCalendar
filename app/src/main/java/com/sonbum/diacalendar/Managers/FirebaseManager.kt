package com.sonbum.diacalendar.Managers

import android.util.Log
import com.sonbum.diacalendar.Firebase.Company
import com.sonbum.diacalendar.Firebase.CompanyListItem
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.internal.resumeCancellableWith
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object FirebaseManager {

    const val TAG = "FirebaseManager"

    val db = Firebase.firestore
    
    val companyListInfoDbRef = db.collection("company_list_info")
        .orderBy("office_name")

    // lambda to coroutine suspend function
    suspend fun fetchCompanyListInfo() : List<CompanyListItem> = suspendCoroutine { continuation: Continuation<List<CompanyListItem>> ->
        companyListInfoDbRef
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                val fetchedCompanyList = CompanyListItem.getCompanyItemList(result)
                continuation.resume(fetchedCompanyList)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)

            }
    }

    suspend fun fetchCertainCompanyInfo(documentRef: DocumentReference) : Company {

        var company : Company =  internalFetchCertainCompanyInfo(documentRef)

        val diaTables = company.fetchDiaTables()

        company.diaTables = diaTables

        return company
    }

    private suspend fun internalFetchCertainCompanyInfo(documentRef: DocumentReference) : Company {
        return suspendCoroutine {

            documentRef
                .get()
                .addOnSuccessListener { result ->
//                for (document in result) {
//                    Log.d(TAG, "${document.id} => ${document.data}")
//                }
                    val test  = result.data
                    Log.d(TAG, "fetchCertainCompanyInfo: $test")
                    val fetchedCompany = Company(documentSnapshot = result, ref = documentRef)
//                    completion.invoke(fetchedCompany)
                    it.resume(fetchedCompany)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
        }
    }


}