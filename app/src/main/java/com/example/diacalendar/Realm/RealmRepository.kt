package com.example.diacalendar.Realm

import android.util.Log
import com.example.diacalendar.Firebase.Company
import com.example.diacalendar.Realm.DiaItemEntity
import com.example.diacalendar.Realm.DiaTableEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.ext.toRealmSet
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RealmRepository(
    private val externalScope : CoroutineScope =
    CoroutineScope(SupervisorJob() + Dispatchers.Default)) {

    companion object {
        const val TAG: String = "RealmRepository"
    }

    private var realm: Realm

    init {
        // Creates a realm with default configuration values
        val config = RealmConfiguration.Builder(
            // Pass object classes for the realm schema
            schema = setOf(DiaItemEntity::class,
                DiaTableEntity::class,
                DiaTableTypeEntity::class)
        ).build()
        // Open the realm with the configuration object
        this.realm = Realm.open(config)
        Log.d(TAG, "Successfully opened realm: ${realm.configuration.name}")
        externalScope.launch {
            this@RealmRepository.createDiaTableTypes()
        }
    }

    suspend fun createDiaTableTypes() {

        // fetch all objects of a type as a results collection, synchronously
        val types: RealmResults<DiaTableTypeEntity> = realm.query<DiaTableTypeEntity>().find()
        val typesCount = types.count()
        if (typesCount > 0) {  return  }

        val typeStringList = listOf<String>("평일","휴일","평평","평휴","휴평","휴휴")

        realm.write {
            val entityList = typeStringList.map { typeString ->
                DiaTableTypeEntity().apply {
                    this.name = typeString
                } }
            entityList.forEach {
                copyToRealm(it)
            }
        }
    }


    suspend fun deleteAndCreateUserCompany(fetchedCompany: Company){
        realm.write {
            // delete existing
            val existingUserCompany = realm.query<UserCompanyEntity>().find()
            delete(existingUserCompany)

            val existingDiaTableEntities = realm.query<DiaTableEntity>().find()
            delete(existingDiaTableEntities)

            val existingDiaItemEntities = realm.query<DiaItemEntity>().find()

            delete(existingDiaItemEntities)

            // create new
            val userCompany = UserCompanyEntity().apply { this.name = fetchedCompany.name }
            val diaTurn = fetchedCompany.diaTurnList
            userCompany.diaTurn = diaTurn.toRealmList()

            val diaSelectList = fetchedCompany.diaSelectedList
            userCompany.diaSelect = diaSelectList.toRealmList()

            val diaTableEntities = fetchedCompany.diaTables.map {
                DiaTableEntity(typeName = it.key, diaWorkDetails = it.value.diaWorkDetails.values.toList())
            }

            userCompany.diaTables = diaTableEntities.toRealmSet()
            copyToRealm(userCompany)
        }
    }

    // fetch single tabletype
    fun fetchDiaTableTypeEntity(name: String) : DiaTableTypeEntity? {
        val diaTableTypes = realm.query<DiaTableTypeEntity>().find()
        if (diaTableTypes.isEmpty()) {
            return null
        }
        return diaTableTypes.filter { it.name == name }.first()
    }

    // create user company
    suspend fun createUserCompany(company: Company) {
        val userCompany = UserCompanyEntity().apply {
            this.name = company.name
        }

        val diaTurn = company.diaTurnList

        val diaSelectList = company.diaSelectedList

        val diaTables = company.diaTables.map {
            DiaTableEntity(typeName = it.key, diaWorkDetails = it.value.diaWorkDetails.values.toList())
        }

        userCompany.diaTables = diaTables.toRealmSet()

        realm.write {
            copyToRealm(userCompany)
        }

    }

    fun fetchUserDateAndTurnList() : List<UserDateAndTurnListEntity> =
        realm.query<UserDateAndTurnListEntity>().find().map { it }

    fun fetchCurrentUserCompannyDiaTurnList(): List<String> {
        val companies = realm.query<UserCompanyEntity>().find()
        val currentcompany = companies.last()
        return currentcompany.diaTurn
    }
    fun fetchCurrentUserCompany(): UserCompanyEntity {
        val companies = realm.query<UserCompanyEntity>().find()
        return companies.last()
    }
}
















