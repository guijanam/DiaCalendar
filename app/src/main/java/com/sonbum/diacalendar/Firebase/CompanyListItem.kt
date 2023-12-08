package com.sonbum.diacalendar.Firebase

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

data class CompanyListItem(
    val snapshot: DocumentSnapshot
    ) {

    var documentId: String
    var officeName: String?
    var ref: DocumentReference?

    init {
        this.documentId = snapshot.id
        this.officeName = snapshot.data?.get("office_name") as String?
        this.ref = snapshot.data?.get("ref") as DocumentReference?
    }

    companion object {
        fun getCompanyItemList(querySnapshot: QuerySnapshot) : List<CompanyListItem> {
            var fetchedCompanyList = mutableListOf<CompanyListItem>()

            querySnapshot.documents.forEach {
                val aCompanyListItem = CompanyListItem(snapshot = it)
                fetchedCompanyList.add(aCompanyListItem)
            }
            return fetchedCompanyList
        }
    }
}
