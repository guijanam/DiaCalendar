package com.sonbum.diacalendar.Managers

import android.util.Log
import com.sonbum.diacalendar.Firebase.Company
import com.sonbum.diacalendar.Firebase.CompanyListItem
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


object FirebaseManager {

    const val TAG = "FirebaseManager"

    val db = Firebase.firestore
    
    val companyListInfoDbRef = db.collection("company_list_info")
        .orderBy("office_name")


    fun fetchCompanyListInfo(fetched: (companyList: List<CompanyListItem>) -> Unit) {
        companyListInfoDbRef
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                val fetchedCompanyList = CompanyListItem.getCompanyItemList(result)
                fetched(fetchedCompanyList)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun fetchCertainCompanyInfo(documentId: String, completion: (company: Company) -> Unit) {

        val companyRef = Firebase.firestore
            .collection("companies")
            .document(documentId)

        companyRef
            .get()
            .addOnSuccessListener { result ->
//                for (document in result) {
//                    Log.d(TAG, "${document.id} => ${document.data}")
//                }

                val fetchedCompany = Company(documentSnapshot = result, ref = companyRef)
                completion.invoke(fetchedCompany)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }


}