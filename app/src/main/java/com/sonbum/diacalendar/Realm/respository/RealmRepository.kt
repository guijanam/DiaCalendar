package com.sonbum.diacalendar.Realm.respository

import android.util.Log
import com.sonbum.diacalendar.Firebase.Company
import com.sonbum.diacalendar.Realm.DiaItemEntity
import com.sonbum.diacalendar.Realm.DiaTableEntity
import com.sonbum.diacalendar.Realm.DiaTableTypeEntity
import com.sonbum.diacalendar.Realm.UserCompanyEntity
import com.sonbum.diacalendar.Realm.UserDateAndTurnListEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.delete
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.ext.toRealmSet
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

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
            schema = setOf(
                DiaItemEntity::class,
                DiaTableEntity::class,
                DiaTableTypeEntity::class,
                UserCompanyEntity::class)
        ).build()
        // Open the realm with the configuration object
        this.realm = Realm.open(config)
        Log.d(TAG, "Successfully opened realm: ${realm.configuration.name}")
        externalScope.launch {
            this@RealmRepository.createDiaTableTypes()
        }
    }

    private suspend fun createDiaTableTypes() {

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

//            val diaTableEntities = fetchedCompany.diaTables.map {
//                DiaTableEntity(typeName = it.key, diaWorkDetails = it.value.diaWorkDetails.values.toList())
//            }

//            userCompany.diaTables = diaTableEntities.toRealmSet()
//            copyToRealm(userCompany)
        }
    }

    // fetch single tabletype
    fun fetchDiaTableTypeEntity(name: String) : DiaTableTypeEntity? {
        val diaTableTypes = realm.query<DiaTableTypeEntity>().find()
        if (diaTableTypes.isEmpty()) {
            return null
        }
        return diaTableTypes.first { it.name == name }
    }

    // create user company
    suspend fun updateUserCompany(company: Company) {

        realm.write {

//            // 1. delete existings
            this.delete(query<UserCompanyEntity>().find())
            this.delete(query<DiaTableEntity>().find())

            val diaTableEntities = company.diaTables.map {

                val diaTableTypeEntity = this.query<DiaTableTypeEntity>("name = $0", it.key).find().first()

                val diaItemEntities = it.value.diaWorkDetails.values.map{ aDiaWorkDetail ->
                    DiaItemEntity.create(aDiaWorkDetail)
                }

                DiaTableEntity().apply {
                    this.diaTableType = diaTableTypeEntity
                    this.diaItems = diaItemEntities.toRealmSet()
                }
            }

            val userCompanyEntity = UserCompanyEntity().apply {
                this.name = company.name
                this.diaTurn = company.diaTurnList.toRealmList()
                this.diaSelect = company.diaSelectedList.toRealmList()
//                // TODO: diaTables
                this.diaTables = diaTableEntities.toRealmSet()
            }

            this.copyToRealm(userCompanyEntity)
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
















