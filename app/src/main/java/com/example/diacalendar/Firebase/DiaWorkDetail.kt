package com.example.diacalendar.Firebase

import androidx.collection.emptyIntObjectMap

data class DiaWorkDetail(var map: Map<String, Any> = emptyMap()) {

    var tableName: String = ""
    var turn : String = ""
    var workTime : String = "-"
    var firstTime : String = "-"
    var secondTime : String = "-"
    var thirdTime : String = "-"
    var totalTime : String = "-"
    var numtr1 : String = "-"
    var numtr2 : String = "-"

    init {
        this.turn = map.get("dia_id") as String
        this.workTime = map.get("workTime") as String
        this.firstTime = map.get("firstTime") as String
        this.secondTime = map.get("secondTime") as String
        this.thirdTime = map.get("thirdTime") as String
        this.totalTime = map.get("totalTime") as String
        this.numtr1 = map.get("num_tr1") as String
        this.numtr2 = map.get("num_tr2") as String

    }

}
