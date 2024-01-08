package com.sonbum.diacalendar.Realm.respository

import android.util.Log
import com.sonbum.diacalendar.Firebase.Company
import com.sonbum.diacalendar.Firebase.DiaWorkDetail
import com.sonbum.diacalendar.Realm.DiaItemEntity
import com.sonbum.diacalendar.Realm.DiaTableEntity
import com.sonbum.diacalendar.Realm.DiaTableTypeEntity
import com.sonbum.diacalendar.Realm.UserCompanyEntity
import com.sonbum.diacalendar.Realm.UserDateAndTurnListEntity
import com.sonbum.diacalendar.shared.getDateString
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.TemporalQueries.localDate
import java.util.Calendar
import java.util.Date
import java.util.Locale


class RealmRepository(
    private val externalScope : CoroutineScope =
    CoroutineScope(SupervisorJob() + Dispatchers.Default)
)
{

    companion object {
        const val TAG: String = "RealmRepository"
    }

    var realm: Realm

    init {
        // Creates a realm with default configuration values
        val config = RealmConfiguration.Builder(
            // Pass object classes for the realm schema
            schema = setOf(
                UserCompanyEntity::class,
                DiaTableEntity::class,
                DiaItemEntity::class,
                DiaTableTypeEntity::class,
                UserDateAndTurnListEntity::class,
                )
        ).build()
        // Open the realm with the configuration object
        this.realm = Realm.open(config)
        Log.d(TAG, "Successfully opened realm: ${realm.configuration.name}")
        externalScope.launch {
            this@RealmRepository.createDiaTableTypes()
        }
    }

    private suspend fun createDiaTableEntity(
        typeName: String,
        diaWorkDetails: List<DiaWorkDetail>
    ) : DiaTableEntity
    {

        val diaTableEntity = DiaTableEntity().apply {
            this.diaTableType = this@RealmRepository.fetchDiaTableTypeEntity(typeName)
            val items = diaWorkDetails.map {
                this@RealmRepository.createDiaItemEntity(it)
            }
            this.diaItems = items.toRealmList()
        }

        realm.write {
            this.copyToRealm(diaTableEntity)
        }
        return diaTableEntity
    }

    private suspend fun createDiaItemEntity(diaWorkDetail: DiaWorkDetail) : DiaItemEntity {
        val diaItemEntity = DiaItemEntity.create(diaWorkDetail)
        realm.write {
            this.copyToRealm(diaItemEntity)
        }
        return diaItemEntity
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

            // TODO: DiaItemEntiy 안지워지고 계속 추가됨
            val existingDiaItemEntities = realm.query<DiaItemEntity>().find()
            delete(existingDiaItemEntities)

            val existingDiaTableTypeEntities = realm.query<DiaTableTypeEntity>().find()
            delete(existingDiaTableTypeEntities)

            val existingUserDateAndTurnListEntities = realm.query<UserDateAndTurnListEntity>().find()
            delete(existingUserDateAndTurnListEntities)

//            val existingUserEntities = realm.query<UserEntity>().find()
//            delete(existingUserEntities)



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
            this.delete(query<DiaItemEntity>().find())
            val diaTableEntities = company.diaTables.map { aDiaTable ->
                val diaWorkDetails : List<DiaWorkDetail> = aDiaTable.value.diaWorkDetails.map { it.value }
                DiaTableEntity().apply {
                    val diaTableType = this@write.query<DiaTableTypeEntity>().find().first()
                    val items = diaWorkDetails.map { aDiaWorkDetail ->
                        DiaItemEntity().apply {
                            this.diaId = aDiaWorkDetail.turn
                            this.workTime = aDiaWorkDetail.workTime
                            this.firstTime = aDiaWorkDetail.firstTime
                            this.secondTime = aDiaWorkDetail.secondTime
                            this.thirdTime = aDiaWorkDetail.thirdTime
                            this.totalTime = aDiaWorkDetail.totalTime
                            this.numtr1 = aDiaWorkDetail.numtr1
                            this.numtr2 = aDiaWorkDetail.numtr2
                        }
                    }
                    this.diaTableType = diaTableType
                    this.diaItems = items.toRealmList()
                }
            }

            val userCompanyEntity = UserCompanyEntity().apply {
                this.name = company.name
                this.diaTurn = company.diaTurnList.toRealmList()
                this.diaSelect = company.diaSelectedList.toRealmList()
                // TODO: diaTables
                this.diaTables = diaTableEntities.toRealmList()
            }
            this.copyToRealm(userCompanyEntity)
        }

    }

    /// 사용자 (날짜 & 교번) 세팅
    /// - Parameters:
    ///   - selectedDate: 세팅시 기준이 되는 날짜
    ///   - selectedDia:  세팅시 기준이 되는 Dia
    ///   - turnList:     교번 목록 - companyEntity - diaTurns
    suspend fun updateUserDateAndTurnList(
        selectedDate: String,
        seletedDia: String,
        turnList: List<String>
    )
    {
        val todayDia = seletedDia
        val multiplyCount = 11
        val leftCycleCount = (multiplyCount - 1) / 2
        val tripleDiaTurnList = List(multiplyCount) { turnList }.flatten()

        val foundIndex = turnList.indexOfFirst { it == todayDia }

        val centerIndex = (turnList.count() * leftCycleCount) + foundIndex - 1

        // 오늘 기준 왼쪽
        val (trimedTurnListLeft, rest) = tripleDiaTurnList
            .withIndex()
            .partition { it.index <= centerIndex }

        Log.d(TAG, "updateUserDateAndTurnList: ")
        val dateArray = this.dates(trimedTurnListLeft.count()
            , dayCount = tripleDiaTurnList.count())

        val dateAndTurnList : List<Pair<String, String>> = dateArray.zip(tripleDiaTurnList)

        Log.d(TAG, "dateAndTurnList.size: ${dateAndTurnList.size}")
        realm.write {
            // 1. delete existing
            this.delete(query<UserDateAndTurnListEntity>().find())

            // 2. create data
            dateAndTurnList.forEach {
                val entity = UserDateAndTurnListEntity.create(it.first, it.second)
                this.copyToRealm(entity)
            }
        }
    }

    fun dates(reverseCount: Int, dayCount: Int) : List<String> {
        val today = Date()

        val startDateCalendar = Calendar.getInstance()
        startDateCalendar.time = today
        startDateCalendar.add(Calendar.DATE, -reverseCount)

        var startDate = startDateCalendar.time

        val endDateCalendar = Calendar.getInstance()
        endDateCalendar.time = today
        endDateCalendar.add(Calendar.DATE, dayCount)

        val endDate = endDateCalendar.time

        var dates = mutableListOf<String>()

        while (startDate <= endDate) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val aDateString: String = dateFormat.format(startDate)
            dates.add(aDateString)

            val addingDateCalendar = Calendar.getInstance()
            addingDateCalendar.time = startDate
            addingDateCalendar.add(Calendar.DATE, 1)
            startDate = addingDateCalendar.time
        }
        return dates
    }

//    suspend fun fetchCurrentDiaAndTurnList() :

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

    // userDiaEntity
//    fun findUserDiaAndTurn(date: LocalDate) : UserDateAndTurnListEntity? {
//
//        val dateString : String = SimpleDateFormat("yyyy-MM-dd").format(date)
//
//
//        val result = realm.query<UserDateAndTurnListEntity>("_calendarData == $0", dateString).first().find()
//
//        return result
//    }


}
















